[webservers]
${instance_public_ip} ansible_user=ec2-user ansible_ssh_private_key_file="${ssh_private_key_path}" ansible_python_interpreter=/usr/bin/python3.8
