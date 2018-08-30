package com.github.sghill.gradle

import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.Action
import org.gradle.api.tasks.wrapper.Wrapper
import java.util.concurrent.TimeUnit

open class ConfigureWrapperAction : Action<Wrapper> {
    private companion object {
        val client: OkHttpClient by lazy {
            OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .build()
        }
    }
    override fun execute(w: Wrapper) {
        w.doFirst({
            if (!w.distributionSha256Sum.isNullOrBlank()) {
                logger.warn("Distribution sha256 is provided - not attempting to fetch")
                return@doFirst
            }
            if (w.distributionUrl.isNullOrBlank()) {
                logger.warn("No distribution url set - not attempting to fetch sha256")
                return@doFirst
            }
            val req = Request.Builder()
                    .url("${w.distributionUrl}.sha256")
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
                w.distributionSha256Sum = sha
                logger.info("Distribution sha256 successfully fetched for ${w.distributionType}")
            }
        })
    }
}