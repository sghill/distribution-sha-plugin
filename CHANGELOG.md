# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

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