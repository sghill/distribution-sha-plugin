package com.github.sghill.gradle

import com.github.sghill.gradle.Logger.debug
import com.github.sghill.gradle.Logger.warn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.kotlin.dsl.withType
import org.gradle.util.GradleVersion

open class DistributionShaPlugin : Plugin<Project> {
    private companion object {
        val gradle45 = GradleVersion.version("4.5")
        val gradle49 = GradleVersion.version("4.9")
    }

    override fun apply(target: Project) {
        val gradle = GradleVersion.version(target.gradle.gradleVersion)
        if (gradle < gradle45) {
            warn("no-op: running on Gradle < 4.5")
            return
        }
        val root = target.findRoot()
        if (gradle < gradle49) {
            root.tasks.withType(Wrapper::class, ConfigureWrapperAction())
            debug("added eager configuration to org.gradle.api.tasks.wrapper.Wrapper")
        } else {
            root.tasks.withType(Wrapper::class).configureEach(ConfigureWrapperAction())
            debug("added delayed configuration to org.gradle.api.tasks.wrapper.Wrapper")
        }
    }

    private tailrec fun Project.findRoot(): Project {
        return if (this == rootProject) this else rootProject.findRoot()
    }
}
