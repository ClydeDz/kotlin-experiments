package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import _vcs.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object UatBuild : BuildType ({
    name = "Uat build & test"

    artifactRules = "uat-version.txt"
    publishArtifacts = PublishMode.SUCCESSFUL

    vcs {
        root(KotlinExperimentsVcsRoot)
    }

    params {
        password("github-repo-token", "credentialsJSON:80a76f0e-2030-480e-b2e9-fd00165980bb")
    }

    steps {
        script {
            name = "Say hello world"
            scriptContent = "echo 'Hello uat build world'"
        }
        script {
            name = "Create text file"
            scriptContent = """echo "Uat build %build.counter%" > "uat-version.txt"""
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${RegularBuild.id}"
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        dependency(RegularBuild){
        }
        snapshot(RegularBuild){
        }
    }
})