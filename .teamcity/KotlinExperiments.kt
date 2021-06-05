import _buildTypes.Build
import _buildTypes.DeployToEnvironment
import _dto.DeployToEnvironmentDto
import jetbrains.buildServer.configs.kotlin.v2019_2.*

object KotlinExperiments: Project({

    params {
        param("teamcity.vcsTrigger.runBuildInNewEmptyBranch", "true")
    }

    val buildAndTest = Build()

    val testDeploymentDto = DeployToEnvironmentDto(
            env = "test",
            storageAccountName = "craazstoragedemo48765",
            manualDeployment = false,
            triggeredByBuild = buildAndTest,
            triggeredByBranchFilter = "+:*",
            snapshotDependencyBuild = buildAndTest,
            artifactDependencyBuild = buildAndTest
    )
    val testDeployment = DeployToEnvironment(testDeploymentDto)

    val productionDeploymentDto = DeployToEnvironmentDto(
            env = "production",
            storageAccountName = "craazstoragedemo78050",
            manualDeployment = false,
            triggeredByBuild = testDeployment,
            triggeredByBranchFilter = "+:tags/release-v*",
            snapshotDependencyBuild = testDeployment,
            artifactDependencyBuild = buildAndTest
    )
    val productionDeployment = DeployToEnvironment(productionDeploymentDto)

    buildType(buildAndTest)
    buildType(testDeployment)
    buildType(productionDeployment)
    buildTypesOrder = listOf(buildAndTest, testDeployment, productionDeployment)
})