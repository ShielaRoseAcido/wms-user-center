locals {
  # Keep names short to avoid AWS name length limits
  base = substr("${var.project}-${var.env}", 0, 20)
}

# --------------------------
# 1) CloudWatch Log Group
# --------------------------
resource "aws_cloudwatch_log_group" "app" {
  name              = "/ecs/${local.base}"
  retention_in_days = 7

  tags = {
    Project = var.project
    Env     = var.env
  }
}

# --------------------------
# 2) Security Groups
# --------------------------
resource "aws_security_group" "alb_sg" {
  name        = "${local.base}-alb-sg"
  description = "ALB security group"
  vpc_id      = var.vpc_id

  ingress {
    description = "HTTP from Internet"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "All outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Project = var.project
    Env     = var.env
  }
}

resource "aws_security_group" "ecs_sg" {
  name        = "${local.base}-ecs-sg"
  description = "ECS tasks security group"
  vpc_id      = var.vpc_id

  ingress {
    description     = "App port from ALB only"
    from_port       = var.app_port
    to_port         = var.app_port
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    description = "All outbound (DB, ECR, Logs, etc.)"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Project = var.project
    Env     = var.env
  }
}

# --------------------------
# 3) ALB + Target Group + Listener
# --------------------------
resource "aws_lb" "alb" {
  name               = "${local.base}-alb"
  load_balancer_type = "application"
  subnets            = var.alb_subnet_ids
  security_groups    = [aws_security_group.alb_sg.id]

  tags = {
    Project = var.project
    Env     = var.env
  }
}

resource "aws_lb_target_group" "tg" {
  # IMPORTANT: use name_prefix (not name) so Terraform can create a new TG first
  name_prefix = "${var.env}tg-"

  port        = var.app_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 2
    interval            = 15
    matcher             = "200-399"
    path                = var.health_path
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    unhealthy_threshold = 3
  }

  tags = {
    Project = var.project
    Env     = var.env
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tg.arn
  }
}

# --------------------------
# 4) ECS Cluster
# --------------------------
resource "aws_ecs_cluster" "cluster" {
  name = "${local.base}-cluster"

  tags = {
    Project = var.project
    Env     = var.env
  }
}

# --------------------------
# 5) IAM Task Execution Role
# --------------------------
data "aws_iam_policy_document" "ecs_task_assume_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "task_execution" {
  name               = "${local.base}-task-exec-role"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role.json

  tags = {
    Project = var.project
    Env     = var.env
  }
}

resource "aws_iam_role_policy_attachment" "task_exec_attach" {
  role       = aws_iam_role.task_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# --------------------------
# 6) ECS Task Definition + Service
# --------------------------
resource "aws_ecs_task_definition" "task" {
  family                   = "${local.base}-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"

  execution_role_arn = aws_iam_role.task_execution.arn

  container_definitions = jsonencode([
    {
      name  = var.container_name
      image = var.container_image

      # ✅ DB environment variables added here
      environment = [
        {
          name  = "SPRING_DATASOURCE_URL"
          value = "jdbc:postgresql://wms-db.c7gywe88chjv.ap-southeast-1.rds.amazonaws.com:5432/wms"
        },
        {
          name  = "SPRING_DATASOURCE_USERNAME"
          value = "wms"
        },
        {
          name  = "SPRING_DATASOURCE_PASSWORD"
          value = var.db_password
        }
      ]

      portMappings = [
        {
          containerPort = var.app_port
          hostPort      = var.app_port
          protocol      = "tcp"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = aws_cloudwatch_log_group.app.name
          awslogs-region        = var.aws_region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])

  tags = {
    Project = var.project
    Env     = var.env
  }
}

resource "aws_ecs_service" "service" {
  name            = "${local.base}-service"
  cluster         = aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.task.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.ecs_subnet_ids
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.tg.arn
    container_name   = var.container_name
    container_port   = var.app_port
  }

  depends_on = [aws_lb_listener.http]

  # Sprint 4 tip:
  # Let Sprint 3 pipeline update the task definition/image without Terraform "fighting back".
  lifecycle {
    ignore_changes = [task_definition]
  }

  tags = {
    Project = var.project
    Env     = var.env
  }
}