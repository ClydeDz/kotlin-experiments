import _buildTypes.RegularBuild
import _buildTypes.DeployToEnvironment
import _buildTypes.ProdBuild
import _buildTypes.UatBuild
import _dto.DeployToEnvironmentDto
import _vcs.KotlinExperimentsVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.*

object KotlinExperiments: Project({

    params {
        param("teamcity.vcsTrigger.runBuildInNewEmptyBranch", "true")
    }

    vcsRoot(KotlinExperimentsVcsRoot)

    buildType(RegularBuild)
    buildType(UatBuild)
    buildType(ProdBuild)

//    val testDeploymentDto = DeployToEnvironmentDto(
//            env = "test",
//            storageAccountName = "craazstoragedemo48765",
//            manualDeployment = false,
//            triggeredByBuild = buildAndTest,
//            triggeredByBranchFilter = "+:*",
//            snapshotDependencyBuild = buildAndTest,
//            artifactDependencyBuild = buildAndTest
//    )
//    val testDeployment = DeployToEnvironment(testDeploymentDto)
//
//    val productionDeploymentDto = DeployToEnvironmentDto(
//            env = "production",
//            storageAccountName = "craazstoragedemo78050",
//            manualDeployment = false,
//            triggeredByBuild = testDeployment,
//            triggeredByBranchFilter = "+:*",
//            snapshotDependencyBuild = testDeployment,
//            artifactDependencyBuild = buildAndTest
//    )
//    val productionDeployment = DeployToEnvironment(productionDeploymentDto)
    buildTypesOrder = listOf(RegularBuild, UatBuild, ProdBuild)
})