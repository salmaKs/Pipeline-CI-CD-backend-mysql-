pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'git_credentials'
        SONAR_TOKEN = 'sonar_tok'
        DOCKER_CREDENTIALS_ID = 'dockerhub_credentials'
        SONAR_CREDENTIALS_ID = 'SONAR_CREDENTIALS'
        SONAR_HOST_URL = 'http://localhost:9000'
        DOCKER_IMAGE_NAME = 'salmaksantini-g4-station-ski'
        DOCKER_IMAGE_TAG = 'latest'
        NEXUS_REPO_URL = 'localhost:8082'
        NEXUS_CREDENTIALS = 'nexus_credentials'
    }

    stages {
       stage('Checkout') {
            steps {
                deleteDir()
                git credentialsId: "${GIT_CREDENTIALS_ID}", branch: 'SalmaKsantini-5ARCTIC1-G4', url: 'https://github.com/salmaKs/5ARCTIC1-G4-StationSKI.git'
            }
        }

       stage('Build') {
            steps {
                sh 'mvn clean compile package'
            }
        }
        
       stage('Jacoco') {
            steps {
                script {
                    sh 'mvn test jacoco:report'
                }
            }
        }
        
        stage('SonarQube') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.token=${SONAR_TOKEN}'

            }
        }
        
       stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus_credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                  sh "mvn deploy -Dnexus.username=${NEXUS_USERNAME} -Dnexus.password=${NEXUS_PASSWORD}"
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    sh 'docker build -t $DOCKER_IMAGE_NAME .'
                    
                    sh'docker tag $DOCKER_IMAGE_NAME $NEXUS_REPO_URL/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG '
                    
                    
                     withCredentials([usernamePassword(credentialsId: 'nexus_credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh 'echo $NEXUS_PASSWORD | docker login ${NEXUS_REPO_URL} --username $NEXUS_USERNAME --password-stdin'
                    }
                    
                    sh'docker push $NEXUS_REPO_URL/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG'
                }
            }
        }
        
        
       
    }
    
      post {
    always {
        emailext(
            subject: "Pipeline Status: ${currentBuild.result}",
            body: "<html>" +
                  "<body>" +
                  "<p>Build Status: ${currentBuild.result}</p>" +
                  "<p>Build Number: ${currentBuild.number}</p>" +
                  "<p>Check the <a href='${env.BUILD_URL}'>console output</a>.</p>" +
                  "</body>" +
                  "</html>",
            to: 'salma.ksantini@esprit.tn',
            from: 'salmaksantini338@gmail.com',
            replyTo: 'salmaksantini338@gmail.com',
            mimeType: 'text/html'
        )
    }
}

  
}

