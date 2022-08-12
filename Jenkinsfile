pipeline {
  agent {
    dockerfile true
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '20'))
  }

  triggers {
    cron '@midnight'
  }

  parameters {
    choice(name: 'deployProfile',
      description: 'Choose where the built plugin should be deployed to',
      choices: ['sonatype.snapshots', 'maven.central.release'])

    string(name: 'revision',
      description: 'Revision for this release, e.g. newest version "8.0.0" revison should be "1" (-> 8.0.1). Note: This is only used for release target!',
      defaultValue: '0' )
  }

  stages {
    stage('build') {
      steps {
        script {
          withCredentials([string(credentialsId: 'gpg.password.axonivy', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.axonivy', variable: 'GPG_FILE')]) {
            sh "gpg --batch --import ${env.GPG_FILE}"
            def phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            def mavenProps = ""
            if (params.deployProfile == 'maven.central.release') {
              mavenProps = "-Drevision=${params.revision}" 
            }
            maven cmd: "clean ${phase} " +
              "-P ${params.deployProfile} " +
              "-Dgpg.passphrase='${env.GPG_PWD}' " +
              "${mavenProps}"
          }
        }
        archiveArtifacts '**/target/*.jar'
        junit '**/target/surefire-reports/**/*.xml'
      }
    }
  }
}
