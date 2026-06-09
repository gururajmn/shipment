pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = "shipment-service"
        TAG = "${BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS = "dockerhub-creds"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Trivy Scan') {
            steps {
                sh 'trivy fs . > trivy-report.txt || true'
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                docker build -t ${IMAGE_NAME}:${TAG} .
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS,
                                                 usernameVariable: 'USER',
                                                 passwordVariable: 'PASS')]) {
                    sh """
                    echo $PASS | docker login -u $USER --password-stdin
                    docker tag ${IMAGE_NAME}:${TAG} $USER/${IMAGE_NAME}:${TAG}
                    docker push $USER/${IMAGE_NAME}:${TAG}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sh """
                docker stop shipment || true
                docker rm shipment || true
                docker run -d --name shipment -p 8084:8080 $USER/${IMAGE_NAME}:${TAG}
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            echo 'Pipeline execution completed'
        }
    }
}
