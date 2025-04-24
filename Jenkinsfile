pipeline {
    agent any

    tools {
        maven 'Maven 3.9.4'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/DRAKOrar/DocentesBack.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline finalizado exitosamente.'
        }
        failure {
            echo '❌ Falló una etapa del pipeline.'
        }
    }
}
