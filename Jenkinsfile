pipeline {
  agent any

  options {
    skipDefaultCheckout(false)
  }

  environment {
    PATH       = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
    JAVA_HOME  = "/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home"
    IMAGE_NAME = "fuelconsumption"
    IMAGE_TAG  = "latest"
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
      }
    }

    stage('Build') {
      steps {
        sh 'mvn -B clean compile'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn -B test'
      }
      post {
        always {
          junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
      }
    }

    stage('Package') {
      steps {
        sh 'mvn -B package -DskipTests'
      }
    }

    stage('Build Docker Image') {
        steps {
          sh 'docker build --platform=linux/amd64 -t "${IMAGE_NAME}:${IMAGE_TAG}" .'
        }
}
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
    }
  }
}