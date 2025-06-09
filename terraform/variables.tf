variable "aws_region" {
  description = "The AWS region to deploy resources in."
  type        = string
  default     = "us-west-2"
}

variable "instance_type" {
  description = "The EC2 instance type."
  type        = string
  default     = "t2.micro"
}

variable "ami_id" {
  description = "The AMI ID for the EC2 instance. Default is latest Amazon Linux 2."
  type        = string
  default     = ""
}

variable "key_name" {
  description = "The name of the EC2 key pair to use for SSH access. Ensure this key pair exists in your AWS account and region."
  type        = string
  // This variable is required. User must provide a value.
}

variable "project_name" {
  description = "A name prefix for resources."
  type        = string
  default     = "team-evil"
}

variable "vpc_id" {
  description = "The ID of the VPC to deploy the EC2 instance into. If empty, the default VPC will be used."
  type        = string
  default     = ""
}

variable "subnet_id" {
  description = "The ID of the subnet to deploy the EC2 instance into. If empty, a default subnet in the default VPC will be chosen."
  type        = string
  default     = ""
}

variable "ssh_private_key_path" {
  description = "Path to the SSH private key file for Ansible connection (e.g., ~/.ssh/your-key.pem)."
  type        = string
  // This variable is required for Ansible provisioning.
}

variable "ghcr_username" {
  description = "GitHub username for GHCR login."
  type        = string
  sensitive   = true
  // This variable is required for Ansible to pull images from GHCR.
}

variable "ghcr_pat" {
  description = "GitHub Personal Access Token (PAT) with read:packages scope for GHCR login."
  type        = string
  sensitive   = true
  // This variable is required for Ansible to pull images from GHCR.
}
