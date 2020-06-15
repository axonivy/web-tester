# Change Log (web-tester)

## [9.1.1-SNAPSHOT]

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
base URL of your running Axon.ivy Engine (**EngineUrl**).

### Added
- [XIVY-3568](https://jira.axonivy.com/jira/browse/XIVY-3568)
  Provide new IvyWebTest artifact.

---

#### Change Log notes

All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).
