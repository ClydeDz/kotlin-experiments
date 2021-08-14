package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import _vcs.*
import _buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object ProdBuild : BuildType ({
    name = "Prod build & test"

    artifactRules = "prod-version.txt"
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
            scriptContent = "echo 'Hello prod build world'"
        }
        script {
            name = "Create text file"
            scriptContent = """echo "Prod build %build.counter%" > "prod-version.txt"""
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${RegularBuild.id}"
            successfulOnly = true
            branchFilter = "+:*"
        }
    }
})