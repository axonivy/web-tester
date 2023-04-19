# Change Log (web-tester)

## [11.1.0] - 2023-04-19

### Changed

- Upgrade selenide to 6.13.0

## [10.0.0] - 2022-10-06

### Changed

- Upgrade selenide to 6.8.1

## [9.4.1] - 2022-09-05

## [9.4.0] - 2022-09-05

### Changed

- Upgrade selenide to 6.7.1
- Upgrade selenium-java to 4.3.0
- Upgrade commons lang3 to 3.12.0
- Upgrade junit to 5.8.1
- Upgrade assertj-core to 3.23.1

## [9.3.0] - 2021-11-16

## [9.2.2] - 2021-04-29

### Added

- Add casemap URL to EngineUrl Util

## [9.2.1] - 2020-10-15

## [9.1.0] - 2020-06-24

## [8.0.1] - 2020-04-06

The API of the **EngineUrl** has been changed. There are new methods (e.g
`createProcessUrl(String path)`), which uses the new URL builder to create the
engine URL. The old methods are still available but they are deprecated.

### Changed

- [XIVY-3568](https://jira.axonivy.com/jira/browse/XIVY-3568)
  Provide new IvyWebTest artifact.

## [8.0.0] - 2020-04-01

Add a new `web-tester` artifact. This provides an JUnit5 annotation
(**IvyWebTest**) to simply start a selenide test. It also has a API to get the
base URL of your running Axon Ivy Engine (**EngineUrl**).

### Added

- [XIVY-3568](https://jira.axonivy.com/jira/browse/XIVY-3568)
  Provide new IvyWebTest artifact.

---

#### Change Log notes

All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).
