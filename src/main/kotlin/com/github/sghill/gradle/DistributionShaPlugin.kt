package com.github.sghill.gradle

import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.kotlin.dsl.withType
import org.gradle.util.GradleVersion
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

open class DistributionShaPlugin : Plugin<Project> {
    private companion object {
        val client: OkHttpClient by lazy {
            OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .build()
        }
        val gradle45 = GradleVersion.version("4.5")
        val logger: Logger = LoggerFactory.getLogger(DistributionShaPlugin::class.java)
    }

    override fun apply(target: Project) {
        if (GradleVersion.version(target.gradle.gradleVersion) < gradle45) {
            logger.warn("Distribution sha methods not available until Gradle 4.5 - skipping")
            return
        }
        val root = target.findRoot()
        root.tasks.withType(Wrapper::class, {
            doFirst({
                if (!distributionSha256Sum.isNullOrBlank()) {
                    logger.warn("Distribution sha256 is provided - not attempting to fetch")
                    return@doFirst
                }
                if (distributionUrl.isNullOrBlank()) {
                    logger.warn("No distribution url set - not attempting to fetch sha256")
                    return@doFirst
                }
                val req = Request.Builder()
                        .url("$distributionUrl.sha256")
                        .build()
                client.newCall(req).execute().use {
                    if (!it.isSuccessful) {
                        logger.info("Fetching distribution sha256 resulted in ${it.code()} ${it.message()}")
                        return@doFirst
                    }
                    val sha = it.body()?.string()
                    if (sha?.length != 64) {
                        logger.warn("Not using fetched distribution sha256 (was ${sha?.length ?: 0} characters; expected 64)")
                        return@doFirst
                    }
                    distributionSha256Sum = sha
                    logger.info("Distribution sha256 successfully fetched for $distributionType")
                }
            })
        })
    }

    private tailrec fun Project.findRoot(): Project {
        return if (this == rootProject) this else rootProject.findRoot()
    }
}
