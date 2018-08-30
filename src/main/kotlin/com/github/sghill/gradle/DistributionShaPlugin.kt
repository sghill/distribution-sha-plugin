package com.github.sghill.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.kotlin.dsl.withType
import org.gradle.util.GradleVersion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class DistributionShaPlugin : Plugin<Project> {
    private companion object {
        val gradle45 = GradleVersion.version("4.5")
        val gradle49 = GradleVersion.version("4.9")
        val logger: Logger = LoggerFactory.getLogger(DistributionShaPlugin::class.java)
    }

    override fun apply(target: Project) {
        val gradle = GradleVersion.version(target.gradle.gradleVersion)
        if (gradle < gradle45) {
            logger.warn("Distribution sha methods not available until Gradle 4.5 - skipping")
            return
        }
        val root = target.findRoot()
        if (gradle < gradle49) {
            root.tasks.withType(Wrapper::class, ConfigureWrapperAction())
        } else {
            root.tasks.withType(Wrapper::class).configureEach(ConfigureWrapperAction())
        }
    }

    private tailrec fun Project.findRoot(): Project {
        return if (this == rootProject) this else rootProject.findRoot()
    }
}
