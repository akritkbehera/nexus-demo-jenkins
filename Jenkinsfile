pipeline {
    agent any
    
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "nexus-service.nexus-system:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "nexus-credentials"
    }
    
    stages {
        stage("Clone code") {
            steps {
                script {
                    // If you're using Git
                    git 'https://github.com/akritkbehera/nexus-demo-jenkins.git'
                    // Or just copy the files to workspace if testing locally
                }
            }
        }
        
        stage("Build") {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage("Push to Nexus") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml"
                    filesByGlob = findFiles(glob: "target/*.jar")
                    artifactPath = filesByGlob[0].path
                    
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: pom.groupId,
                        version: pom.version,
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            [artifactId: pom.artifactId,
                             classifier: '',
                             file: artifactPath,
                             type: 'jar'],
                            [artifactId: pom.artifactId,
                             classifier: '',
                             file: "pom.xml",
                             type: "pom"]
                        ]
                    )
                }
            }
        }
    }
}
