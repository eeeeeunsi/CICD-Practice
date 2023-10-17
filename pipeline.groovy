pipeline {
    agent any
    
    stages{
        stage('git clone') {
            steps{
                git branch: 'main', credentialsId: 'eeeeeunsi', url: 'https://github.com/eeeeeunsi/CICD-Practice.git'
            }
        }
        stage('build') {
            steps {
                dir('nginx') {
                    sh 'docker build -t p1 .'
                }
            }
        }
    }
}
