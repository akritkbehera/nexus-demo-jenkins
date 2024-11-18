pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                metadata:
                  labels:
                    app: jenkins-agent
                spec:
                  containers:
                  - name: maven
                    image: maven:3.9-amazoncorretto-17
                    command:
                    - cat
                    tty: true
                    volumeMounts:
                    - name: m2
                      mountPath: /root/.m2
                  volumes:
                  - name: m2
                    emptyDir: {}
            '''
        }
    }
    
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
                checkout scm
            }
        }
        
        stage("Build") {
            steps {
                container('maven') {
                    sh 'mvn clean package'
                }
            }
        }
        
        stage("Push to Nexus") {
            steps {
                container('maven') {
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
}
