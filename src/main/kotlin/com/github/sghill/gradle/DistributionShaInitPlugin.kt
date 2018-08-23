package com.github.sghill.gradle

import org.gradle.api.Plugin
import org.gradle.api.invocation.Gradle
import org.gradle.kotlin.dsl.apply

open class DistributionShaInitPlugin : Plugin<Gradle> {
    override fun apply(target: Gradle) {
        target.rootProject({
            apply<DistributionShaPlugin>()
        })
    }
}
