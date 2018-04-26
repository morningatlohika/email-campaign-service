node {
    // Get Artifactory server instance, defined in the Artifactory Plugin administration page.
    def server = Artifactory.server "Artifactory"
    // Create an Artifactory Gradle instance.
    def rtGradle = Artifactory.newGradleBuild()

    stage 'Clone sources'
        git url: 'https://github.com/morningatlohika/email-campaign-service.git'

    stage 'Artifactory configuration'
        // Tool name from Jenkins configuration
        rtGradle.tool = "Gradle-2.4"
        // Set Artifactory repositories for dependencies resolution and artifacts deployment.
        rtGradle.deployer repo:'ext-release-local', server: server
        rtGradle.resolver repo:'remote-repos', server: server

    stage 'Gradle build'
        def buildInfo = rtGradle.run rootDir: "/", buildFile: 'build.gradle', tasks: 'clean build -x text'

    stage 'Publish build info'
        server.publishBuildInfo buildInfo
}