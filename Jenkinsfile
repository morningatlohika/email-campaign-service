#!/usr/bin/env groovy Jenkinsfile

def server = Artifactory.server "Artifactory"
def gradle = Artifactory.newGradleBuild()
def buildInfo = Artifactory.newBuildInfo()

pipeline() {

    agent any

    triggers {
        pollSCM('H/10 * * * *')
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    environment {
        SLACK_AUTOMATION_CHANNEL = "#automation"
        SLACK_AUTOMATION_TOKEN = credentials("jenkins-ci-integration-token")
        JENKINS_HOOKS = credentials("morning-at-lohika-jenkins-ci-hooks")
    }

    stages {

        stage('Pre configuration') {
            steps {
                script {
                    gradle.useWrapper = true
                    gradle.deployer.deployMavenDescriptors = true
                    gradle.deployer.deployIvyDescriptors = true
                    gradle.deployer.mavenCompatible = true
                    gradle.deployer server: server, repo: 'morning-at-lohika-snapshots'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    buildInfo = gradle.run rootDir: "./", buildFile: 'build.gradle', tasks: 'clean build'
                }
            }
        }

        stage('Build and publish SNAPSHOT') {
            when { branch 'master' }
            steps {
                script {
                    buildInfo = gradle.run rootDir: "./", buildFile: 'build.gradle', tasks: 'clean build  artifactoryPublish'
                }
            }
        }

        stage('Collect build info') {
            steps {
                script {
                    buildInfo.env.filter.addExclude("*TOKEN*")
                    buildInfo.env.filter.addExclude("*HOOK*")
                    buildInfo.env.collect()
                }
            }
        }

        stage('Deploy') {
            when { buildingTag() }
            steps {
                echo 'Deploying only because this commit is tagged...'
                sh 'make deploy'
            }
        }
    }

    post {
        always {
            script {
                publishHTML(target: [
                    allowMissing         : true,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : 'build/reports/tests/test',
                    reportFiles          : 'index.html',
                    reportName           : "Test Summary"
                ])
                junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
                server.publishBuildInfo buildInfo
            }
        }

        success {
            script {
                dir("${env.WORKSPACE}") {
                    archiveArtifacts 'build/libs/*.jar'
                }

                slackSend(
                    baseUrl: "${env.JENKINS_HOOKS}",
                    token: "${env.SLACK_AUTOMATION_TOKEN}",
                    channel: "${env.SLACK_AUTOMATION_CHANNEL}",
                    botUser: true,
                    color: "good",
                    message: "BUILD SUCCESS: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}]\nCheck console output at: ${env.BUILD_URL}"
                )
            }
        }

        failure {
            script {
                slackSend(
                    baseUrl: "${env.JENKINS_HOOKS}",
                    token: "${env.SLACK_AUTOMATION_TOKEN}",
                    channel: "${env.SLACK_AUTOMATION_CHANNEL}",
                    botUser: true,
                    color: "danger",
                    message: "BUILD FAILURE: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}]\nCheck console output at: ${env.BUILD_URL}"
                )
            }
        }
    }
}
