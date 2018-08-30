package com.github.sghill.gradle

import com.github.sghill.gradle.Logger.info
import com.github.sghill.gradle.Logger.warn
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
                warn("no-op: sha256 already provided")
                return@doFirst
            }
            if (w.distributionUrl.isNullOrBlank()) {
                warn("no-op: distributionUrl missing")
                return@doFirst
            }
            val sha256Url = "${w.distributionUrl}.sha256"
            val req = Request.Builder()
                    .url(sha256Url)
                    .build()
            client.newCall(req).execute().use {
                if (!it.isSuccessful) {
                    info("fetching sha256 resulted in ${it.code()} ${it.message()}")
                    return@doFirst
                }
                val sha = it.body()?.string()
                if (sha?.length != 64) {
                    warn("no-op: fetched sha256 (was ${sha?.length ?: 0} characters; expected 64)")
                    return@doFirst
                }
                w.distributionSha256Sum = sha
                info("sha256 successfully fetched at ${sha256Url}")
            }
        })
    }
}