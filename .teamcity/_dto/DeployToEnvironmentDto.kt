package _dto

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType

class DeployToEnvironmentDto {
    constructor(env: String,
                storageAccountName: String,
                manualDeployment: Boolean,
                triggeredByBuild: BuildType? = null,
                triggeredByBranchFilter: String? = null,
                snapshotDependencyBuild: BuildType,
                artifactDependencyBuild: BuildType){
        Env = env;
        StorageAccountName = storageAccountName;
        ManualDeployment = manualDeployment;
        TriggeredByBuild = triggeredByBuild;
        TriggeredByBranchFilter = triggeredByBranchFilter;
        SnapshotDependencyBuild = snapshotDependencyBuild;
        ArtifactDependencyBuild = artifactDependencyBuild;
    }
    
    var Env: String = "";
    var StorageAccountName: String = "";
    var ManualDeployment: Boolean = false;
    var TriggeredByBuild: BuildType? = null;
    var TriggeredByBranchFilter: String? = null;
    var SnapshotDependencyBuild: BuildType = BuildType();
    var ArtifactDependencyBuild: BuildType = BuildType();
}