plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.10.0"
    id("nebula.release") version "6.3.5"
    id("org.sonarqube") version "2.6.2"
}

group = "com.github.sghill.gradle"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp") {
        version {
            strictly("[3.0.0,4.0.0[")
        }
    }

    testImplementation("io.github.glytching:junit-extensions:latest.release")
    testImplementation(enforcedPlatform("org.junit:junit-bom:5.4.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.register("testForJava8", Test::class) {
    tasks.named("check").get().dependsOn(this)
    onlyIf { JavaVersion.current().isJava8 }
    useJUnitPlatform {
        includeTags("java8")
    }
}

tasks.named("test", Test::class).configure {
    useJUnitPlatform {
        excludeTags("java8")
    }
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
