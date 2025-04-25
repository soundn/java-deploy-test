resource "aws_instance" "app_server2" {
  ami                         = var.ami
  vpc_security_group_ids      = [aws_security_group.web_sg2.id]
  subnet_id                   = data.aws_subnet.default.id
  associate_public_ip_address = true
  instance_type               = var.instance_type
  key_name                    = "new"



  tags = {
    Name = "java-test"
  }
}