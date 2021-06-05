package _vcs

import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.*

object KotlinExperimentsVcsRoot: GitVcsRoot ({
    name = "Kotlin Experiments VCS Root"
    url = "https://github.com/ClydeDz/kotlin-experiments.git"
    useTagsAsBranches = true
    authMethod = password {
        userName = "clydedz"
        password = "credentialsJSON:80a76f0e-2030-480e-b2e9-fd00165980bb"
    }
    branch = "refs/heads/main"
    branchSpec = """
        +:refs/heads/*
        +:refs/(tags/*)
    """.trimIndent()
})