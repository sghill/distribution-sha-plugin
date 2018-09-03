plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.10.0"
    id("nebula.maven-resolved-dependencies") version "8.2.1"
    id("nebula.release") version "6.3.5"
}

group = "com.github.sghill.gradle"

dependencyLocking {
    lockAllConfigurations()
}

repositories {
    mavenCentral()
}

dependencies {
    components.withModule("org.junit:junit-bom", JUnitBomStatusSelector::class.java)
    implementation("com.squareup.okhttp3:okhttp:latest.release")

    testImplementation("io.github.glytching:junit-extensions:latest.release")
    testImplementation("org.junit:junit-bom:latest.release")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType(Test::class).configureEach {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("distributionSha") {
            id = "com.github.sghill.distribution-sha"
            implementationClass = "com.github.sghill.gradle.DistributionShaPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/sghill/distribution-sha-plugin"
    vcsUrl = "https://github.com/sghill/distribution-sha-plugin"
    tags = listOf("sha", "distributionSha256Sum")
    (plugins) {
        "distributionSha" {
            displayName = "Automatic Distribution SHA"
            description = "Automatically discover the distribution's sha256sum when running wrapper task"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "dist"
            setUrl("$buildDir/repo")
        }
    }
}

open class JUnitBomStatusSelector : ComponentMetadataRule {
    private companion object {
        val SCHEME = listOf("unknown", "alpha", "milestone", "candidate", "release")
        val RELEASE = "[\\d+\\.]{5}".toRegex()
        val CANDIDATE = "\\S+-RC\\d+$".toRegex()
        val MILESTONE = "\\S+-M\\d+$".toRegex()
    }

    override fun execute(t: ComponentMetadataContext) {
        with(t.details) {
            val v = id.version
            status = when {
                v.matches(RELEASE) -> "release"
                v.matches(CANDIDATE) -> "candidate"
                v.matches(MILESTONE) -> "milestone"
                v.endsWith("-ALPHA") -> "alpha"
                else -> "unknown"
            }
            statusScheme = SCHEME
        }
    }
}
