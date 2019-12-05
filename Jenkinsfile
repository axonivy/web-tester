pipeline {
  agent {
    dockerfile true
  }
  parameters {
    booleanParam(defaultValue: false, description: 'If checked the plugin does not sign the plugin', name: 'skipGPGSign')
    choice(
      name: 'engineSource',
      description: 'Engine to use for build',
      choices: 'Linux_Trunk_DesignerAndServer\nTrunk_DesignerAndServer\nTrunk_All'
    )
    choice(
      name: 'DEPLOY_PROFILES',
      description: 'Choose where the built plugin should be deployed to',
      choices: 'build\ncentral'
    )
  }
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '20'))
  }
  triggers {
    pollSCM 'H/30 * * * *'
    cron '@midnight'
  }
  stages {    
    stage('build and deploy') {
      steps {
        withCredentials([string(credentialsId: 'gpg.password.supplements', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.supplements', variable: 'GPG_FILE')]) {
          script {
            script {
              sh "gpg --batch --import ${env.GPG_FILE}"
              maven cmd: "clean deploy -f primeui-tester/pom.xml --activate-profiles ${DEPLOY_PROFILES} -Dgpg.passphrase='${env.GPG_PWD}' -Dgpg.skip=${params.skipGPGSign} -Dmaven.test.failure.ignore=true"
            }
            archiveArtifacts '*/target/*.jar'
          }
        }
      }
      post {
        success {
          junit '**/target/surefire-reports/**/*.xml' 
        }
      }
    }
  }
}
