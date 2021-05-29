package _buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildTypeSettings
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger
import _dto.*

class DeployToEnvironment(deploySettings: DeployToEnvironmentDto): BuildType({
    val id = "DeployToEnvironment${deploySettings.Env}"
    id(id)
    
    name = "Deploy to ${deploySettings.Env}"

    enablePersonalBuilds = false
    type = BuildTypeSettings.Type.DEPLOYMENT
    maxRunningBuilds = 1

    params {
        password("service-principal-id", "credentialsJSON:4baaaddb-aef6-478f-97c2-19b06d9677c4")
        password("service-principal-password", "credentialsJSON:e218e9a3-4fa6-4294-acb6-7c949043f7a0")
        password("azure-tenant", "credentialsJSON:77e469c2-b099-464f-ad03-8f7c72d5723d")
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
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
            scriptContent = "az storage blob upload-batch --account-name ${deploySettings.StorageAccountName} -d ${'$'}web -s ./target --auth-mode login"
        }
        script {
            name = "Azure logout"
            scriptContent = "az logout"
        }
    }

    if (!deploySettings.ManualDeployment){
        triggers {
            finishBuildTrigger {
                buildType = "${deploySettings.TriggeredByBuild?.id}"
                successfulOnly = true
                branchFilter = "+:main"
            }
        }
    }

    dependencies {
        dependency(deploySettings.SnapshotDependencyBuild) {
            snapshot{
                onDependencyFailure = FailureAction.CANCEL
                onDependencyCancel = FailureAction.CANCEL
            }
        }
        dependency(deploySettings.ArtifactDependencyBuild) {
            artifacts {
                cleanDestination = true
                artifactRules = "**/*.*=>target"
            }
        }
    }
})