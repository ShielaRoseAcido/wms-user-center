variable "aws_region" {
  type    = string
  default = "ap-southeast-1"
}

variable "project" {
  type    = string
  default = "wucs"
}

variable "env" {
  type    = string
  default = "dev"
}

# Reuse existing VPC/Subnets (copy from AWS console)
variable "vpc_id" {
  type = string
}

variable "alb_subnet_ids" {
  type = list(string)
}

variable "ecs_subnet_ids" {
  type = list(string)
}

# App settings
variable "app_port" {
  type    = number
  default = 8080
}

variable "health_path" {
  type    = string
  default = "/actuator/health"
}

variable "desired_count" {
  type    = number
  default = 1
}

# Placeholder image for first infra test (pipeline later replaces with your app image)
variable "container_image" {
  type    = string
  default = "nginxdemos/hello:latest"
}

variable "container_name" {
  type    = string
  default = "wms-app"
}

variable "db_password" {
  type      = string
  sensitive = true
}