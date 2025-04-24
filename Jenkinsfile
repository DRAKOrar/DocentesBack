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
                sh 'mvn clean install'
            }
        }
    }
}