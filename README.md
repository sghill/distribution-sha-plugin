Automatic Distribution Sha Plugin
=================================

[![CircleCI](https://circleci.com/gh/sghill/distribution-sha-plugin.svg?style=svg)](https://circleci.com/gh/sghill/distribution-sha-plugin)

This plugin makes discovering the [sha256-sum of a gradle distribution][sha256]
automatic.

Use
---

Apply to the root project:

    plugins {
        id "com.github.sghill.distribution-sha" version "0.1.0"
    }

Or to apply to every project you run, create an [init script][init]
`~/.gradle/init.d/auto-sha.gradle`:

    initscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath 'com.github.sghill.gradle:distribution-sha-plugin:0.1.0'
        }
    }

    rootProject {
        apply plugin: com.github.sghill.gradle.DistributionShaPlugin
    }


How
---

With the plugin applied, the wrapper task is configured to fetch distributionUrl + ".sha256"
and provide its contents as the `gradle-distribution-sha256-sum` arg automatically:

    $ ./gradlew wrapper --gradle-version 4.9 --distribution-type all
    $ cat gradle/wrapper/gradle-wrapper.properties | grep distribution

    distributionBase=GRADLE_USER_HOME
    distributionPath=wrapper/dists
    distributionSha256Sum=39e2d5803bbd5eaf6c8efe07067b0e5a00235e8c71318642b2ed262920b27721
    distributionUrl=https\://services.gradle.org/distributions/gradle-4.9-all.zip


Previously
----------

To get this feature on wrapper upgrades without the plugin you'd need to get the contents
of the .sha256 file and add it to the command:

    ./gradlew wrapper --gradle-version 4.9 --distribution-type all --gradle-distribution-sha256-sum 39e2d5803bbd5eaf6c8efe07067b0e5a00235e8c71318642b2ed262920b27721

Understandably, this means that many projects with a checked-in wrapper aren't using this
feature.

[sha256]: https://docs.gradle.org/current/userguide/gradle_wrapper.html
[init]: https://docs.gradle.org/current/userguide/init_scripts.html

