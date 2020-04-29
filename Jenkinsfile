// Replace the projectName and projectKey with your SonarQube values
def projectName = 'template-microservice'
def projectKey = 'io.digital.supercharger:template-microservice'
pipeline {
    agent any
    /* I advise to add these tools in pipeline in order to avoid installing it globally through Dockerfile which will increases image size and cause latency in deployments. 
    But I have to do it as a work around to fix `mvn not present error`, ocurring frequently..*/   
    tools {
        maven 'Maven 3'
        jdk 'open-jdk8'
    }
    stages {
        stage ('Compile') {
            steps {
               sh 'printenv'
               sh 'mvn clean compile'
            }
        }
        stage ('Code analysis') {
            steps {
               sh 'mvn checkstyle:check pmd:check spotbugs:check'
            }
        }
        stage ('Unit Tests') {
            steps {
                sh 'mvn test'
                jacoco(
                      execPattern: 'target/*.exec',
                      classPattern: 'target/classes',
                      sourcePattern: 'src/main/java',
                      exclusionPattern: 'src/test*'
                )
            }
        }
        stage ('Integration Tests') {
            steps {
                // Skip everything apart from the integration tests
                sh 'mvn verify -Dskip.surefire.tests -Dspotbugs.skip=true -Dpmd.skip=true -Dcheckstyle.skip=true -Ddockerfile.skip'
            }
        }
        stage("build & SonarQube analysis") {
            steps {
              withSonarQubeEnv('sonar') {
                sh 'mvn clean package sonar:sonar'
              }
            }
        }
        stage('SQ Quality Gate') {
          steps {
            timeout(time: 1, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: false
            }
          }
        }
        stage ('Package') {
            when {
                expression {
                    return env.BRANCH_NAME.equals("master") || env.BRANCH_NAME.equals("develop")
                }
            }
            steps {
                // package will tag with git-hash and also latest
                sh 'mvn package -DskipTests=true -Dskip.surefire.tests'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
            deleteDir() /* clean up our workspace */
        }
           /* This will send email in case of failure with logs in compress format.
           with Job Name,Build Number and Build Result */
        failure{
            echo 'Sending email'
        
            emailext subject: "${currentBuild.result}: Job '${env.JOB_NAME} - Build #(${env.BUILD_NUMBER})'",
            body: """<p>${env.JOB_NAME} - Build #(${env.BUILD_NUMBER}) - ${currentBuild.result}</p>""",
            recipientProviders: [[$class: 'CulpritsRecipientProvider']],
            attachLog: true, compressLog: true,
            mimeType: 'text/html',
            to: 'safderqasim@yahoo.com'
        }
    }
}
