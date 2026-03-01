output "alb_dns_name" {
  value       = aws_lb.alb.dns_name
  description = "Public ALB DNS name (your entry URL)"
}

output "ecs_cluster_name" {
  value       = aws_ecs_cluster.cluster.name
  description = "ECS cluster name"
}

output "ecs_service_name" {
  value       = aws_ecs_service.service.name
  description = "ECS service name"
}

output "task_execution_role_arn" {
  value       = aws_iam_role.task_execution.arn
  description = "Task execution role ARN (pull from ECR + write logs)"
}

output "log_group_name" {
  value       = aws_cloudwatch_log_group.app.name
  description = "CloudWatch log group receiving container logs"
}

output "alb_security_group_id" {
  value       = aws_security_group.alb_sg.id
  description = "ALB security group ID"
}

output "ecs_security_group_id" {
  value       = aws_security_group.ecs_sg.id
  description = "ECS tasks security group ID"
}