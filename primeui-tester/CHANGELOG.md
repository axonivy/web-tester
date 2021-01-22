# Change Log (primeui-tester)

## [8.0.11] - 2021-01-22

### Added

- SelectCheckboxMenu#itemsShouldBeSelected(String...)

## [8.0.10] - 2020-10-15

The versions of the **web-tester** and **primeui-tester** has been streamlined.

### Added

- New method for SelectOneMenu to check value with selenide conditions.
- New InputNumber Util for easy PrimeFaces 7.0.17 usage

## [7.0.0] - 2020-04-01

The JUnit5 annotation **IvySelenide** has been renamed (to **IvyWebTest**) and moved to the
`web-tester` artifact with the **EngineUrl**. If you use it you need to change the dependency in your
pom file.
In addition the package name of the `primeui-tester` has been changed: 
`com.axonivy.ivy.supplements.primeui.tester -> com.axonivy.ivy.webtest.primeui`

### Changed

- [XIVY-3568](https://jira.axonivy.com/jira/browse/XIVY-3568)
  Provide new IvyWebTest artifact.

---

#### Change Log notes

All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).