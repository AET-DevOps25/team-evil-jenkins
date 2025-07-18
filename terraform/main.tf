provider "aws" {
  region = var.aws_region
}

data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

resource "aws_security_group" "web_sg" {
  name        = "team-evil-jenkins-web-sg"
  description = "Allow SSH, HTTP, and application ports"
  vpc_id      = var.vpc_id == "" ? data.aws_vpc.default.id : var.vpc_id

  ingress {
    description = "SSH from anywhere"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "HTTP from anywhere"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Client app (Port 3000)"
    from_port   = 3000
    to_port     = 3000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "User Service (Port 8080)"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Location Service (Port 8081)"
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Messaging Service (Port 8082)"
    from_port   = 8082
    to_port     = 8082
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Matching Service (Port 8083)"
    from_port   = 8083
    to_port     = 8083
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1" // Allow all outbound traffic
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.project_name}-web-sg"
  }
}

resource "aws_instance" "web_server" {
  ami           = var.ami_id == "" ? data.aws_ami.amazon_linux_2.id : var.ami_id
  instance_type = var.instance_type
  key_name      = var.key_name

  subnet_id = var.subnet_id == "" ? sort(data.aws_subnets.default.ids)[0] : var.subnet_id

  vpc_security_group_ids      = [aws_security_group.web_sg.id]
  associate_public_ip_address = true

  tags = {
    Name = "${var.project_name}-ec2-instance"
  }

  user_data = <<-EOF
              #!/bin/bash
              # Update and install Docker
              sudo yum update -y
              sudo amazon-linux-extras install docker -y
              sudo service docker start
              sudo usermod -a -G docker ec2-user
              sudo chkconfig docker on
              sudo amazon-linux-extras install -y python3.8
sudo yum install -y python3-pip
sudo pip3.8 install "urllib3<2.0" requests docker
              # Ansible will handle the rest
              EOF
}

resource "local_file" "ansible_inventory" {
  content = templatefile("${path.module}/inventory.ini.tpl", {
    instance_public_ip   = aws_instance.web_server.public_ip,
    ssh_private_key_path = var.ssh_private_key_path
  })
  filename = "${path.module}/../ansible/inventory.ini"
}

resource "null_resource" "ansible_provisioner" {
  depends_on = [aws_instance.web_server]

  triggers = {
    instance_id      = aws_instance.web_server.id
    inventory_file   = local_file.ansible_inventory.id # Ensures inventory is updated before running Ansible
    playbook_content = filemd5("${path.module}/../ansible/playbook.yml") # Rerun if playbook changes
  }

  provisioner "local-exec" {
    # Wait for SSH to become available. Increase timeout if needed.
    command = <<-EOT
      echo "Waiting for instance ${aws_instance.web_server.public_ip} to be ready..."
      for i in {1..30}; do # timeout after 5 minutes (30 * 10 seconds)
        nc -w 5 -z ${aws_instance.web_server.public_ip} 22 && break
        echo "Still waiting for SSH on ${aws_instance.web_server.public_ip}... attempt $i"
        sleep 10
      done
      if ! nc -w 5 -z ${aws_instance.web_server.public_ip} 22; then
        echo "Timeout waiting for SSH on ${aws_instance.web_server.public_ip}"
        exit 1
      fi
      echo "Instance ${aws_instance.web_server.public_ip} is ready. Proceeding with Ansible."

      # Wait for SSH to be available
      until ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -i "${var.ssh_private_key_path}" ec2-user@${aws_instance.web_server.public_ip} exit; do
        echo "Waiting for SSH on ${aws_instance.web_server.public_ip}..."
        sleep 5
      done
      echo "SSH is available on ${aws_instance.web_server.public_ip}."

      # Run the Ansible playbook
      # Suppress sensitive output by default, but allow verbose output if needed for debugging
      ansible-playbook -i ../ansible/inventory.ini ../ansible/playbook.yml --extra-vars "ghcr_username=${var.ghcr_username} ghcr_pat=${var.ghcr_pat}" || \
      (echo 'Ansible playbook failed. Rerun with -vvv for details.' && exit 1)
    EOT
    interpreter = ["bash", "-c"]
    working_dir = path.module # Run from the terraform directory
  }
}
