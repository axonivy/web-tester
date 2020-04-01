pipeline {
  agent {
    dockerfile true
  }

  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '20'))
  }

  triggers {
    pollSCM '@hourly'
    cron '@midnight'
  }

  stages {
    stage('build and deploy') {
      steps {
        withCredentials([string(credentialsId: 'gpg.password.supplements', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.supplements', variable: 'GPG_FILE')]) {
          script {
            sh "gpg --batch --import ${env.GPG_FILE}"

            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            maven cmd: "clean ${phase} -Dgpg.passphrase='${env.GPG_PWD}' -Dgpg.skip=false -Dmaven.test.failure.ignore=true"
            junit 'target/surefire-reports/**/*.xml'     
            archiveArtifacts 'target/*.jar'
          }
        }
      }
    }
  }
}
