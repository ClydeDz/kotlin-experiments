package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import _vcs.*
import _buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildTypeSettings
import jetbrains.buildServer.configs.kotlin.v2019_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object UatDeploy : BuildType ({
    name = "Uat deploy"
    type = BuildTypeSettings.Type.DEPLOYMENT

    vcs {
        root(KotlinExperimentsVcsRoot)
    }

    steps {
        script {
            name = "Say hello world"
            scriptContent = "echo 'Deploy from uat build'"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${UatBuild.id}"
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        dependency(UatBuild) {
            snapshot{
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }
        }
        dependency(UatBuild) {
            artifacts {
                cleanDestination = true
                artifactRules = "**/*.*=>target"
            }
        }
    }
})