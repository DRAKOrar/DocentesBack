pipeline {
    agent any

    stages {
        stage('Clonar Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/DRAKOrar/DocentesBack.git'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean test'
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
