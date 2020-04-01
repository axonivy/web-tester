# Change Log
All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## web-tester

### [8.0.0] - 2020-04-01

Add a new `web-tester` artifact. This provides an JUnit5 annotation
(**IvyWebTest**) to simply start a selenide test. It also has a API to get the
base URL of your running Axon.ivy Engine (**EngineUrl**).

#### Added
- [XIVY-3568](https://jira.axonivy.com/jira/browse/XIVY-3568)
  Provide new IvyWebTest artifact.


## primeui-tester

### [7.0.0] - 2020-04-01

The JUnit5 annotation **IvySelenide** has been renamed (to **IvyWebTest**) and moved to the
`web-tester` artifact with the **EngineUrl**. If you use it you need to change the dependency in your
pom file.
In addition the package name of the `primeui-tester` has been changed: 
`com.axonivy.ivy.supplements.primeui.tester -> com.axonivy.ivy.webtest.primeui`

#### Changed
- [XIVY-3568](https://jira.axonivy.com/jira/browse/XIVY-3568)
  Provide new IvyWebTest artifact.
