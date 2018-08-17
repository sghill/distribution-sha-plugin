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
    implementation("com.squareup.okhttp3:okhttp:latest.release")
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
