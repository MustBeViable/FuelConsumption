pipeline {
  agent any

  options {
    skipDefaultCheckout(false)
  }

  environment {
    PATH              = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
    JAVA_HOME         = "/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home"
    APP_IMAGE         = "fuelconsumption-app"
    DB_IMAGE          = "fuelconsumption-db"
    IMAGE_TAG         = "latest"
    SONARQUBE_SERVER  = "SonarQubeServer"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Verify Tools') {
      steps {
        sh 'echo "PATH=$PATH"'
        sh 'echo "JAVA_HOME=$JAVA_HOME"'
        sh 'which java && java -version'
        sh 'which mvn && mvn -v'
        sh 'which docker && docker --version'
        sh 'docker compose version || true'
      }
    }

    stage('Build') {
      steps {
        sh 'mvn -B clean compile'
      }
    }

    stage('Test + JaCoCo') {
      steps {
        sh 'mvn -B test verify'
      }
      post {
        always {
          junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
          archiveArtifacts artifacts: 'target/site/jacoco/**/*', allowEmptyArchive: true
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv("${SONARQUBE_SERVER}") {
          sh '''
            mvn -B sonar:sonar \
              -Dsonar.projectKey=fuelconsumption \
              -Dsonar.projectName=FuelConsumption
          '''
        }
      }
    }

    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    stage('Package') {
      steps {
        sh 'mvn -B package -DskipTests'
      }
    }

    stage('Build DB Image') {
      steps {
        sh 'docker build --platform=linux/amd64 -t "${DB_IMAGE}:${IMAGE_TAG}" ./db'
      }
    }

    stage('Build App Image') {
      steps {
        sh 'docker build --platform=linux/amd64 -t "${APP_IMAGE}:${IMAGE_TAG}" .'
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/*.jar,target/site/jacoco/**/*,db/schema.sql,docker-compose.yml,Dockerfile,Jenkinsfile,README.md', allowEmptyArchive: true
    }
  }
}