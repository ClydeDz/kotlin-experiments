import jetbrains.buildServer.configs.kotlin.v2019_2.*
import _buildTypes.*
import _dto.*

version = "2020.2"

project {
    val build = Build()

    val testDeploymentDto = DeployToEnvironmentDto(
            env = "test",
            storageAccountName = "craazstoragedemo48765",
            manualDeployment = false,
            triggeredByBuild = build,
            snapshotDependencyBuild = build,
            artifactDependencyBuild = build
    )
    val testDeployment = DeployToEnvironment(testDeploymentDto)

    val productionDeploymentDto = DeployToEnvironmentDto(
            env = "production",
            storageAccountName = "craazstoragedemo78050",
            manualDeployment = true,
            snapshotDependencyBuild = testDeployment,
            artifactDependencyBuild = build
    )
    val productionDeployment = DeployToEnvironment(productionDeploymentDto)

    buildType(build)
    buildType(testDeployment)
    buildType(productionDeployment)
}
