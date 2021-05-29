package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

class Build : BuildType ({
    name = "Build & test"

    artifactRules = "build"
    publishArtifacts = PublishMode.SUCCESSFUL

    vcs {
        root(DslContext.settingsRoot)
    }

    params {
        password("github-repo-token", "credentialsJSON:80a76f0e-2030-480e-b2e9-fd00165980bb")
    }

    steps {
        script {
            name = "Create build directory"
            scriptContent = """echo "Build %build.counter%" > "build\version.txt"""
        }
        script {
            name = "Create text file"
            scriptContent = """echo "Build %build.counter%" > "build\version.txt"""
        }
    }

    triggers {
        vcs {
            perCheckinTriggering = true
            groupCheckinsByCommitter = true
            enableQueueOptimization = true
        }
    }

    features {
        pullRequests {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            provider = github {
                authType = token {
                    token = "%github-repo-token%"
                }
                filterTargetBranch = "+:main"
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
        commitStatusPublisher {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "%github-repo-token%"
                }
            }
        }
    }
})