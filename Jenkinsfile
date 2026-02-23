pipeline {
  agent any

  options {
    skipDefaultCheckout(true)
    timestamps()
  }

  environment {
    SPRING_DATASOURCE_URL      = 'jdbc:postgresql://postgres-ci:5432/wms'
    SPRING_DATASOURCE_USERNAME = 'wms'
    SPRING_DATASOURCE_PASSWORD = 'wms'
    AWS_REGION                 = 'ap-southeast-1'
  }

  stages {

    stage('Checkout') {
      steps {
        git branch: 'feature-practice',
            credentialsId: 'github-pat-wms',
            url: 'https://github.com/ShielaRoseAcido/wms-user-center.git'
      }
    }

    stage('Build + Unit Tests') {
      steps {
        sh 'chmod +x mvnw || true'
        withCredentials([usernamePassword(
          credentialsId: 'app-basic-auth',
          usernameVariable: 'APP_BASIC_USER',
          passwordVariable: 'APP_BASIC_PASS'
        )]) {
          sh './mvnw test'
        }
      }
    }

    stage('Package JAR') {
      steps {
        sh './mvnw -B -DskipTests package'
      }
    }

    stage('Docker Sanity') {
      steps {
        sh '''
          set -e
          docker version
          docker ps
        '''
      }
    }

    stage('AWS Sanity') {
      steps {
        sh '''
          set -e
          echo "Who am I?"; whoami
          echo "OS:"; uname -a
          echo "AWS CLI:"; aws --version
          echo "Where is aws?"; which aws || true
        '''
      }
    }

    stage('AWS Identity') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'aws-creds',
          usernameVariable: 'AWS_ACCESS_KEY_ID',
          passwordVariable: 'AWS_SECRET_ACCESS_KEY'
        )]) {
          sh 'aws sts get-caller-identity --region $AWS_REGION'
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        sh '''
          set -e
          echo "BUILD_NUMBER=$BUILD_NUMBER"
          docker build -t wms-app:${BUILD_NUMBER} .
          docker image ls wms-app:${BUILD_NUMBER}
        '''
      }
    }

    stage('Deploy Container') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'app-basic-auth',
          usernameVariable: 'APP_BASIC_USER',
          passwordVariable: 'APP_BASIC_PASS'
        )]) {
          sh '''
            set -e

            docker network create wms-net >/dev/null 2>&1 || true
            docker network connect wms-net postgres-ci >/dev/null 2>&1 || true
            docker network connect wms-net jenkins-local >/dev/null 2>&1 || true

            docker rm -f wms-app >/dev/null 2>&1 || true

            docker run -d --name wms-app --network wms-net \
              -p 8082:8080 \
              -e SPRING_DATASOURCE_URL="$SPRING_DATASOURCE_URL" \
              -e SPRING_DATASOURCE_USERNAME="$SPRING_DATASOURCE_USERNAME" \
              -e SPRING_DATASOURCE_PASSWORD="$SPRING_DATASOURCE_PASSWORD" \
              -e APP_BASIC_USER="$APP_BASIC_USER" \
              -e APP_BASIC_PASS="$APP_BASIC_PASS" \
              wms-app:${BUILD_NUMBER}

            docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}" | grep wms-app || true
          '''
        }
      }
    }

    stage('Smoke Test (Auth)') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'app-basic-auth',
          usernameVariable: 'APP_BASIC_USER',
          passwordVariable: 'APP_BASIC_PASS'
        )]) {
          sh '''
            set -e
            echo "Waiting for app to be ready (Auth smoke test)..."

            for i in $(seq 1 30); do
              if docker run --rm --network wms-net curlimages/curl:8.5.0 \
                -u "$APP_BASIC_USER:$APP_BASIC_PASS" \
                -fsS http://wms-app:8080/actuator/health; then
                echo "✅ App is UP (Auth OK)"
                exit 0
              fi
              echo "Attempt $i failed, waiting..."
              sleep 2
            done

            echo "❌ App never became ready"
            docker logs --tail 200 wms-app || true
            exit 1
          '''
        }
      }
    }

    stage('Push to ECR') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'aws-creds',
          usernameVariable: 'AWS_ACCESS_KEY_ID',
          passwordVariable: 'AWS_SECRET_ACCESS_KEY'
        )]) {
          sh '''
            set -e

            ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text --region "$AWS_REGION")
            ECR="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
            REPO="wms-app"

            aws ecr describe-repositories --repository-names "$REPO" --region "$AWS_REGION" >/dev/null 2>&1 \
              || aws ecr create-repository --repository-name "$REPO" --region "$AWS_REGION" >/dev/null

            aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$ECR"

            docker tag wms-app:${BUILD_NUMBER} "$ECR/$REPO:${BUILD_NUMBER}"
            docker push "$ECR/$REPO:${BUILD_NUMBER}"

            echo "✅ Pushed to ECR: $ECR/$REPO:${BUILD_NUMBER}"
          '''
        }
      }
    }

  } // end stages

  post {
    always {
      junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
      sh 'docker logs --tail 300 wms-app > container.log 2>&1 || true'
      archiveArtifacts artifacts: 'target/*.jar,target/surefire-reports/**,container.log',
                       allowEmptyArchive: true,
                       fingerprint: true
    }
  }
}