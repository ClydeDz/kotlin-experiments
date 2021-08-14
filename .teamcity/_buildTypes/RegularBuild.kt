package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import _vcs.*

object RegularBuild : BuildType ({
    name = "Build & test"

    vcs {
        root(KotlinExperimentsVcsRoot)
    }

    params {
        password("github-repo-token", "credentialsJSON:80a76f0e-2030-480e-b2e9-fd00165980bb")
    }

    steps {
        script {
            name = "Say hello world"
            scriptContent = "echo 'Hello world'"
        }
    }

    triggers {
        vcs {
            id = "${KotlinExperimentsVcsRoot.id}"
            perCheckinTriggering = true
            groupCheckinsByCommitter = true
            enableQueueOptimization = true
        }
    }

    features {
        pullRequests {
            vcsRootExtId = "${KotlinExperimentsVcsRoot.id}"
            provider = github {
                authType = token {
                    token = "%github-repo-token%"
                }
                filterTargetBranch = "+:main"
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
        commitStatusPublisher {
            vcsRootExtId = "${KotlinExperimentsVcsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "%github-repo-token%"
                }
            }
        }
    }
})