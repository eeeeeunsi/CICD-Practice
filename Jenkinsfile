pipeline {
    agent any

    stages {
        stage('git clone') {
            steps {
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
        
        stage('test run') {
            steps {
                sh 'docker run -d -p 80:80 --rm --name p1 p1'
            }
        }
        
        stage('application test') {
            steps {
                script {
                    def status = sh(script: "curl -sLI -w '%{http_code}' localhost -o /dev/null", returnStdout: true).trim()
                    if (status != '200' && status != '201') {
                        error("Returned status code = $status when calling test")
                    }
                }
            }
            post {
                failure {
                    sh 'docker stop p1'
                }
            }
        }
        
        stage('cleanup test') {
            steps {
                sh 'docker stop p1'
            }
        }
                                         
        stage('push to ecr') {
            steps {
                script {
                    sh 'aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com'
                    sh 'docker tag p1:latest 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com/jenkins:latest'
                    sh 'docker push 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com/jenkins:latest'
                }
            }
        }
        
        stage('deploy script run') {
            steps {
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        sh 'ssh -o StrictHostKeyChecking=no -i $JENKINS_HOME/test.pem ec2-user@172.31.50.255 "docker ps -q | xargs docker stop"'
                    }
                    sh 'ssh -o StrictHostKeyChecking=no -i $JENKINS_HOME/test.pem ec2-user@172.31.50.255 \
                    "aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com; \
                    docker rmi -f $(docker images -aq) \
                    docker run -d --rm -p 80:80 --name nginx 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com/jenkins:latest"'
                }
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'SUCCESS') {
                        sh 'ssh -o StrictHostKeyChecking=no -i $JENKINS_HOME/test.pem ubuntu@172.31.32.10 "docker ps -q | xargs docker stop"'
                    }
                    sh 'ssh -o StrictHostKeyChecking=no -i $JENKINS_HOME/test.pem ubuntu@172.31.32.10 \
                    "aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com; \
                    docker rmi -f $(docker images -aq) \
                    docker run -d --rm -p 80:80 --name nginx 560971842042.dkr.ecr.ap-northeast-2.amazonaws.com/jenkins:latest"'
                }
            }
        }
    }
}