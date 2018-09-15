package com.github.sghill.gradle

import io.github.glytching.junit.extension.folder.TemporaryFolder
import io.github.glytching.junit.extension.folder.TemporaryFolderExtension
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TemporaryFolderExtension::class)
class DistributionShaPluginCompatibilityTest {

    /**
     * Any task can be called to verify this, because in Gradle versions < 4.9 the task
     * had to be eagerly configured.
     */
    @Test
    fun `should warn user of no-op on less than Gradle 4_5`(projectDir: TemporaryFolder) {
        // given
        projectDir.createFile("build.gradle").appendText("""
            plugins {
                id 'com.github.sghill.distribution-sha'
            }
            """.trimIndent())

        // when
        val result = GradleRunner.create()
                .withGradleVersion("4.4")
                .withArguments("tasks")
                .withProjectDir(projectDir.root)
                .withPluginClasspath()
                .build()

        // then
        result.output.contains("[dist-sha] no-op: running on Gradle < 4.5")
    }

    @Test
    fun `should display eager config executed in debug for Gradle 4_5 up to Gradle 4_9`(projectDir: TemporaryFolder) {
        // given
        projectDir.createFile("build.gradle").appendText("""
            plugins {
                id 'com.github.sghill.distribution-sha'
            }
            """.trimIndent())

        // when
        val result = GradleRunner.create()
                .withGradleVersion("4.5")
                .withArguments("tasks", "--debug")
                .withProjectDir(projectDir.root)
                .withPluginClasspath()
                .build()

        // then
        result.output.contains("[dist-sha] added eager configuration to org.gradle.api.tasks.wrapper.Wrapper")
    }

    @Test
    fun `should lazily add config showing nothing in debug on greater than or equal to Gradle 4_9`(projectDir: TemporaryFolder) {
        // given
        projectDir.createFile("build.gradle").appendText("""
            plugins {
                id 'com.github.sghill.distribution-sha'
            }
            """.trimIndent())

        // when
        val result = GradleRunner.create()
                .withGradleVersion("4.9")
                .withArguments("tasks", "--debug")
                .withProjectDir(projectDir.root)
                .withPluginClasspath()
                .build()

        // then
        !result.output.contains("[dist-sha] added eager configuration to org.gradle.api.tasks.wrapper.Wrapper")
    }

    @Test
    fun `should lazily add config showing only in debug on current gradle`(projectDir: TemporaryFolder) {
        // given
        projectDir.createFile("build.gradle").appendText("""
            plugins {
                id 'com.github.sghill.distribution-sha'
            }
            """.trimIndent())

        // when
        val result = GradleRunner.create()
                .withArguments("wrapper", "--gradle-version=4.9", "--debug")
                .withProjectDir(projectDir.root)
                .withPluginClasspath()
                .build()

        // then
        result.output.contains("[dist-sha] added eager configuration to org.gradle.api.tasks.wrapper.Wrapper")
    }
}
