# web-tester

[![web-tester version][0]][1] [![primeui-tester version][2]][3] [![unit-tester version][4]][5]

The `web-tester` artifact provides you a API which helps you test your JSF-Page.
With this API it is easy to setup your test environment and send requests
against your [Axon Ivy Engine](https://developer.axonivy.com/download).

Checkout our official
[documentation](https://developer.axonivy.com/doc/9.2/concepts/testing/web-testing.html).
for more information.

## primeui-tester

If your JSF-Page contains [PrimeFaces](https://www.primefaces.org/showcase/)
widgets, the `primeui-tester` gives you the possibility to interact with those
widgets and check if it's in the condition you expected it to be.

## unit-tester

The `unit-tester` artifact provides you the following APIs for unit testing:

* [JUnit 5](https://junit.org/junit5/) 
* [AssertJ](https://assertj.github.io/doc/)

# How to use in your project

The web-tester runs with [Selenide](https://selenide.org/),
[Selenium](https://selenium.dev/projects/) and [JUnit
5](https://junit.org/junit5/). Simply add this library to your dependencies in
the pom.xml:

```xml
<dependencies>
...
  <dependency>
    <groupId>com.axonivy.ivy.webtest</groupId>
    <artifactId>web-tester</artifactId>
    <version>10.0.15</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

Add a new test class to test your process (e.g.
[WebTestRegistrationForm.java](https://github.com/axonivy/project-build-examples/blob/master/compile-test/crmIntegrationTests/src_test/ch/ivyteam/integrationtest/WebTestRegistrationFormIT.java))
or a PrimeFaces widget (e.g.
[TestPrimeUi.java](primeui-tester/src/test/java/com/axonivy/ivy/webtest/primeui/TestPrimeUi.java)):

```java
@IvyWebTest
class WebTest {

  @Test
  void registerNewCustomer() {
    open(EngineUrl.createProcessUrl("myWorkflow/154616078A1D629D/start.ivp"));
    $(By.id("form:firstname")).sendKeys("Unit");
    $(By.id("form:lastname")).sendKeys("Test");
    $(By.id("form:submit")).shouldBe(enabled).click();
    $(By.id("form:newCustomer")).shouldBe(visible, text("Unit Test"));
  }

  @Test
  void selectOneMenu() {
    open("https://primefaces.org/showcase/ui/input/oneMenu.xhtml");
    var selectOne = PrimeUi.selectOne(selectMenuForLabel("Basic:"));
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    var ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }
}
```

## Changelog

* [web-tester](web-tester/CHANGELOG.md)
* [primeui-tester](primeui-tester/CHANGELOG.md)
* [unit-tester](unit-tester/CHANGELOG.md)

## Release new version

### Preparation

* Check/Update the changelog files above

### Release

Since 9.4: Releasing is only possible on a release branch.

* Create a release branch if it does not exist yet (e.g. release/10.0)
* Run the [release build](build/release/Jenkinsfile) on the release branch
* Merge the Pull Request for next development iteration
* If you have created a new release branch, then manually raise the version on the master branch to the next major or minor version by executing the following command in the root of this project:

```bash
mvn versions:set -DnewVersion=10.0.0-SNAPSHOT -DprocessAllModules -DgenerateBackupPoms=false
```

### Post-Release

Wait until the maven central release is available: this may take several hours until it's fully distributed.

* Raise web-tester in other repos by triggering this [build](https://jenkins.ivyteam.io/view/jobs/job/github-repo-manager_raise-web-tester-version/job/master/)


## Authors

[ivyTeam](https://developer.axonivy.com/)

[![Axon Ivy](https://www.axonivy.com/hubfs/brand/axonivy-logo-black.svg)](http://www.axonivy.com)

## License

The Apache License, Version 2.0

[0]: https://img.shields.io/maven-metadata/v.svg?versionPrefix=10&label=web-tester&logo=apachemaven&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Faxonivy%2Fivy%2Fwebtest%2Fweb-tester%2Fmaven-metadata.xml
[1]: https://repo1.maven.org/maven2/com/axonivy/ivy/webtest/web-tester/
[2]: https://img.shields.io/maven-metadata/v.svg?versionPrefix=10&label=primeui-tester&logo=apachemaven&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Faxonivy%2Fivy%2Fwebtest%2Fprimeui-tester%2Fmaven-metadata.xml
[3]: https://repo1.maven.org/maven2/com/axonivy/ivy/webtest/primeui-tester/
[4]: https://img.shields.io/maven-metadata/v.svg?versionPrefix=10&label=unit-tester&logo=apachemaven&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Faxonivy%2Fivy%2Ftest%2Funit-tester%2Fmaven-metadata.xml
[5]: https://repo1.maven.org/maven2/com/axonivy/ivy/test/unit-tester/
