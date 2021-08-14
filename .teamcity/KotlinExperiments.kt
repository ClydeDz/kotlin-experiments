import _buildTypes.*
import _vcs.KotlinExperimentsVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.*

object KotlinExperiments: Project({

    params {
        param("teamcity.vcsTrigger.runBuildInNewEmptyBranch", "true")
    }

    vcsRoot(KotlinExperimentsVcsRoot)

    val buildChain = sequential {
        buildType(RegularBuild)
        parallel (options = { onDependencyFailure = FailureAction.CANCEL }) {
            buildType(UatBuild)
            buildType(ProdBuild)
        }
        buildType(UatDeploy)
        buildType(ProdDeploy)
    }

    buildChain.buildTypes().forEach { buildType(it) }
})