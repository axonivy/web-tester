# Change Log (primeui-tester)

## [10.0.14] - 

## [10.0.0] - 2022-10-06

## [9.4.1] - 2022-09-05

### Bugfix

- Exclude all opentelemetry dependencies in dependency hierarchy to prevent error logs in IvyTest and IvyProcessTest if this web-tester or primeui-tester is added as dependency.

## [9.4.0] - 2022-09-05

### Changed

- Support Selenium 4
- Update Selenide to 6.7.3
- Update commons-lang3 to 3.12.0

### Bugfix

- Fix possible endless loop in InputNumber#clear

## [9.3.0] - 2021-11-16

### Changed

- Update Selenide to 5.25.0
- SelectOneRadio: Change selectItemByValue so it selects by the item value and add selectItemBy Label as replacement for the old behaviour

### Added

- Add more JavaDoc
- SelectOneMenu: Add selectByValue method (by the item value)
- SelectOneMenu: Add support for SelectOneMenu with editable mode activated 

## [9.2.2] - 2021-04-29

As PrimeFaces changed their showcase, the tests needed to be adjusted.
Meanwhile there was some improvement in the API:

### Changed

- Update Selenide from 5.15.0 to 5.20.1
- The API returns now for the most parts the object itself. This allows you to use it as fluent API.

### Added

- Table#row(int)
- Table#column(int)
- Table#valueAtShoudBe(int, int, Condition)
- Table#searchColumn(int, String)
- Accordion#tabShouldBe(String, boolean)
- SelectCheckboxMenu#itemsShouldBeSelected(String...)
- SelectBooleanCheckbox#shouldBeChecked(boolean)
- SelectBooleanCheckbox#shouldBeDisabled(boolean)

### Deprecated

- Dialog
- Accordion#isTabOpen(String)
- SelectCheckboxMenu#selectItemByValue(String label)
- SelectBooleanCheckbox#isChecked()
- SelectBooleanCheckbox#isDisabled()

## [9.2.1] - 2020-10-15

### Added

- New InputNumber Util for easy PrimeFaces 7.0.17 usage

### Changed

- Update Selenide from 5.12.2 to 5.15.0

### Removed

- Deprecated AjaxHelper

## [9.1.0] - 2020-06-24

The versions of the **web-tester** and **primeui-tester** has been streamlined.

### Added

- New method for SelectOneMenu to check value with selenide conditions.

### Changed

- Update Selenide from 5.10.0 to 5.12.2

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
