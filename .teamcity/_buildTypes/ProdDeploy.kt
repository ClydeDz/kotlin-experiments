package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.PublishMode
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import _vcs.*
import _buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildTypeSettings
import jetbrains.buildServer.configs.kotlin.v2019_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

object ProdDeploy : BuildType ({
    name = "Prod deploy"
    type = BuildTypeSettings.Type.DEPLOYMENT

    vcs {
        root(KotlinExperimentsVcsRoot)
    }

    params {
        password("service-principal-id", "credentialsJSON:4baaaddb-aef6-478f-97c2-19b06d9677c4")
        password("service-principal-password", "credentialsJSON:e218e9a3-4fa6-4294-acb6-7c949043f7a0")
        password("azure-tenant", "credentialsJSON:77e469c2-b099-464f-ad03-8f7c72d5723d")
    }

    steps {
        script {
            name = "Say hello world"
            scriptContent = "echo 'Deploy from prod build'"
        }
        script {
            name = "Check installed Azure CLI version"
            scriptContent = "az --version"
        }
        script {
            name = "Azure login"
            scriptContent = "az login --service-principal -u %service-principal-id% -p %service-principal-password% --tenant %azure-tenant%"
        }
        script {
            name = "Update blob storage"
            scriptContent = "az storage blob upload-batch --account-name craazstoragedemo78050 -d ${'$'}web -s ./target --auth-mode login"
        }
        script {
            name = "Azure logout"
            scriptContent = "az logout"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${UatDeploy.id}"
            successfulOnly = true
            branchFilter = "+:*"
        }
    }

    dependencies {
        dependency(ProdBuild) {
            snapshot{
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }
        }
        dependency(ProdBuild) {
            artifacts {
                cleanDestination = true
                artifactRules = "**/*.*=>target"
            }
        }
    }
})