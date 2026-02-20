pipeline {
  agent any

  environment {
    SPRING_DATASOURCE_URL      = 'jdbc:postgresql://postgres-ci:5432/wms'
    SPRING_DATASOURCE_USERNAME = 'wms'
    SPRING_DATASOURCE_PASSWORD = 'wms'
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

    stage('Build Docker Image') {
      steps {
        sh '''
          set -e
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

            docker rm -f wms-app || true

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

    stage('Smoke Test (Container)') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'app-basic-auth',
          usernameVariable: 'APP_BASIC_USER',
          passwordVariable: 'APP_BASIC_PASS'
        )]) {
          sh '''
            set -e
            docker run --rm --network wms-net curlimages/curl:8.5.0 \
              -u "$APP_BASIC_USER:$APP_BASIC_PASS" \
              -f http://wms-app:8080/actuator/health
          '''
        }
      }
    }
  }
}
