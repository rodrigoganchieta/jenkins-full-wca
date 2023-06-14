pipeline {
    agent any
    stages {
        stage ('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests=true'
            }
        }
        stage ('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }
        stage ('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://192.168.15.15:9000 -Dsonar.login=cf6826d57f1e453e08ecbd6cf862496472061f66 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
                }
            }
        }
        stage ('Quality Gate') {
            steps {
                sleep(5)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage ('Deploy Backend') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://192.168.15.15:8001/')], contextPath: 'jenkins-backend-wca', war: 'target/jenkins-backend-wca.war'
            }
        }
        stage ('API Test') {
            steps {
                dir('jenkins-api-test-wca') {
                    git credentialsId: 'github_login', url: 'https://github.com/rodrigoganchieta/jenkins-api-test-wca'
                    bat 'mvn test'
                }
            }
        }
        stage ('Deploy Frontend') {
            steps {
                dir('jenkins-frontend-wca') {
                    git credentialsId: 'github_login', url: 'https://rodrigoganchieta/jenkins-frontend-wca'
                    bat 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://192.168.15.15:8001/')], contextPath: 'tasks', war: 'target/jenkins-frontend-wca.war'
                }
            }
        }
        stage ('Functional Test') {
            steps {
                dir('tasks-functional-tests') {
                    git credentialsId: 'github_login', url: 'https://github.com/rodrigoganchieta/jenkins-functional-tests-wca'
                    bat 'mvn test'
                }
            }
        }
        stage('Deploy Prod') {
            steps {
                bat 'docker-compose build'
                bat 'docker-compose up -d'
            }
        }
        stage ('Health Check') {
            steps {
                sleep(5)
                dir('tasks-functional-tests') {
                    bat 'mvn verify -Dskip.surefire.tests'
                }
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
            archiveArtifacts artifacts: 'target/jenkins-backend-wca.war, jenkins-frontend-wca/target/jenkins-frontend-wca.war', onlyIfSuccessful: true
        }
        unsuccessful {
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build $BUILD_NUMBER has failed', to: 'rodrigoganchieta+jenkins@gmail.com'
        }
        fixed {
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build is fine!!!', to: 'rodrigoganchieta+jenkins@gmail.com'
        }
    }
}


