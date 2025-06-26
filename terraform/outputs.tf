output "instance_public_ip" {
  description = "The public IP address of the EC2 instance."
  value       = aws_instance.web_server.public_ip
}

output "instance_id" {
  description = "The ID of the EC2 instance."
  value       = aws_instance.web_server.id
}

output "security_group_id" {
  description = "The ID of the security group."
  value       = aws_security_group.web_sg.id
}
