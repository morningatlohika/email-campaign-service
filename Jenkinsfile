node {
    def server = Artifactory.server "Artifactory"
    def gradle = Artifactory.newGradleBuild()

    stage 'Clone sources'
        git url: 'https://github.com/morningatlohika/email-campaign-service.git'

    stage 'Artifactory configuration'
        gradle.useWrapper = true

        gradle.deployer repo:'ext-release-local', server: server
        gradle.resolver repo:'remote-repos', server: server

    stage 'Gradle build'
        def buildInfo = gradle.run rootDir: "./", buildFile: 'build.gradle', tasks: 'clean build -x text'

    stage 'Publish build info'
        server.publishBuildInfo buildInfo
    
    stage 'Slack send'
        slackSend color: 'good', message: 'Message from Jenkins Pipeline'
}
