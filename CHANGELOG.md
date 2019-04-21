# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.4.0]

_Released 2019-04-21_

### Added
- Publishing Gradle Module Metadata

### Changed
- Depend on a strict range for okhttp of >= 3.0.0 and <= 4.0.0. We're not using
  features introduced in a specific version, so other things on the buildscript
  classpath should be free to declare what they need. So long as it fits within
  our range, we're good to go.
- Built with Gradle 5.4

## [0.3.0]

_Released 2018-08-30_

### Added
- If running with Gradle > 4.8.1 the wrapper task will be configured only
  when called

### Changed
- All logging now starts with the [dist-sha] marker

## [0.2.2]

_Released 2018-08-27_

### Fixed
- Plugin will now no-op on Gradle < 4.5. This is the first version with the
  methods we need to set the distributionSha256Sum.

### Changed
- Built with Gradle 4.10

## [0.2.1]

_Released 2018-08-23_

### Fixed
- `DistributionShaInitPlugin` was incorrectly applying `DistributionShaPlugin`
  with the Kotlin DSL, causing the init script to not load when the plugin was
  applied.

### Changed
- Built with Gradle 4.10-rc-3

## [0.2.0]

_Released 2018-08-22_

### Added
- `DistributionShaInitPlugin` so the plugin can be applied in an init script,
  as opposed to having an init script that waits for the `rootProject` to load.

## [0.1.0]

_Released 2018-08-17_

### Added
- `DistributionShaPlugin`, which configures the `Wrapper` task to automatically
  download the `distributionSha256Sum` with the distribution if available.