pipeline {

    agent any

    environment {
        IMAGE_NAME = "shipment-service"
        TAG = "${BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS = "dockerhub"
        DOCKERHUB_USER = "gurugowdamn"
        SONAR_SERVER = "SonarQube"
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
                withSonarQubeEnv("${SONAR_SERVER}") {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Trivy FS Scan') {
            steps {
                sh 'trivy fs . --format table -o trivy-fs-report.txt || true'
            }
        }

        stage('Docker Build & Tag') {
            steps {
                sh """
                docker build -t ${IMAGE_NAME}:${TAG} .

                docker tag ${IMAGE_NAME}:${TAG} ${DOCKERHUB_USER}/${IMAGE_NAME}:${TAG}
                docker tag ${IMAGE_NAME}:${TAG} ${DOCKERHUB_USER}/${IMAGE_NAME}:latest
                """
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh """
                trivy image ${DOCKERHUB_USER}/${IMAGE_NAME}:${TAG} \
                --format table -o trivy-image-report.txt || true
                """
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${DOCKERHUB_CREDENTIALS}",
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {
                    sh """
                    echo $PASS | docker login -u $USER --password-stdin

                    docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${TAG}
                    docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sh """
                docker stop shipment || true
                docker rm shipment || true

                docker run -d \
                --name shipment \
                -p 8084:8080 \
                ${DOCKERHUB_USER}/${IMAGE_NAME}:${TAG}
                """
            }
        }

        stage('Verify Deployment') {
            steps {
                sh """
                sleep 10
                curl -f http://localhost:8084/api/shipments || true
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/*.txt', fingerprint: true
            echo 'Pipeline execution completed'
        }

        success {
            echo 'SUCCESS: Deployment completed successfully'
        }

        failure {
            echo 'FAILED: Check logs for errors'
        }
    }
}
