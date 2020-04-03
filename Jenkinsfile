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

  parameters {
    choice(name: 'deployProfile',
      description: 'Choose where the built plugin should be deployed to',
      choices: ['sonatype.snapshots', 'maven.central.release'])

    choice(name: 'deployArtifact',
      description: "Choose which project should be released (onyl if you choose 'maven.central.release' as deployProfile)",
      choices: ['web-tester', 'primeui-tester'])

    string(name: 'revision',
      description: 'Revision for this release, e.g. newest version "8.0.0" revison should be "1". Note: This is only used for release target!',
      defaultValue: '0' )
  }

  stages {
    stage('snapshot build') {
      when {
        expression { params.deployProfile != 'maven.central.release' }
      }
      steps {
        script {
          withCredentials([string(credentialsId: 'gpg.password.supplements', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.supplements', variable: 'GPG_FILE')]) {
            sh "gpg --batch --import ${env.GPG_FILE}"
            def phase = 'package'
            if (params.deployProfile != 'maven.central.release') {
              phase = env.BRANCH_NAME == 'master' ? 'deploy' : 'verify'
            }
            maven cmd: "clean ${phase} " +
              "-P ${params.deployProfile} " +
              "-Dgpg.passphrase='${env.GPG_PWD}' " +
              "-Dgpg.skip=false " +
              "-Dmaven.test.failure.ignore=true"
          }
        }
        archiveArtifacts '**/target/*.jar'
        junit '**/target/surefire-reports/**/*.xml'
      }
    }

    stage('release') {
      when {
        //branch 'master'
        expression { params.deployProfile == 'maven.central.release' }
      }
      steps {

        script {
          sh "git config --global user.name 'ivy-team'"
          sh "git config --global user.email 'nobody@axonivy.com'"
          
          withCredentials([string(credentialsId: 'gpg.password.axonivy', variable: 'GPG_PWD'), file(credentialsId: 'gpg.keystore.axonivy', variable: 'GPG_FILE')]) {
            sh "gpg --batch --import ${env.GPG_FILE}"
            withEnv(['GIT_SSH_COMMAND=ssh -o StrictHostKeyChecking=no']) {
              sshagent(credentials: ['github-axonivy']) {
                dir("${params.deployArtifact}"){
                  maven cmd: "clean deploy scm:tag " +
                    "-P ${params.deployProfile} " +
                    "-Dgpg.passphrase='${env.GPG_PWD}' " +
                    "-Dgpg.skip=false " +
                    "-Drevision=${params.revision} "
                }
              }
            }
          }
        }
        archiveArtifacts '**/target/*.jar'
        junit '**/target/surefire-reports/**/*.xml'
      }
    }
  }
}
