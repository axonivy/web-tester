# web-tester

[![web-tester version][web-lts] ![web-le][web-le]][web-releases] <br/>
[![primeui-tester version][prime-lts] ![prime-le][prime-le]][prime-releases] <br/>
[![test-bom version][bom-lts] ![bom-le][bom-le]][bom-releases] <br/>

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

## test-bom

The `test-bom` artifact defines third-party dependencies that are used for testing.

- [JUnit 5](https://junit.org/junit5/)
- [AssertJ](https://assertj.github.io/doc/)
- [Selenide](https://selenide.org/documentation.html)

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
    <version>12.0.1</version>
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

## Running tests

Start the PrimeFaces Showcase used by the widget tests:

```sh
docker run --rm -p 18085:8080 ghcr.io/primefaces/primefaces-showcase:15.X-latest
```

Run the widget tests from the repository root:

```sh
mvnd -pl primeui-tester -am test \
  -Dshowcase.url=http://localhost:18085/
```

Firefox is the default browser. Add `-Dselenide.browser=chrome` to use Chrome.

## Release new version

### Release

- Verify:
  - The consuming repos use the latest SNAPSHOT. To be sure, run the [raise version job][raise-version-job] providing the current SNAPSHOT.
  - Renovate dependencies are accurate; especially jersey-client must be in sync with the current core!
- Create a release branch if it does not exist yet (e.g. `release/13.2`).
- Run the [release build][release-build] on the respective release branch.
- Merge the pull request for the next development iteration.
- If you have created a new release branch, manually raise the version on the `master` branch to the next major or minor version:
  - Execute the following command in the root of this project (adjust version number accordingly):

    ```bash
    .ivy/manual-raise-version.sh 13.2.0-SNAPSHOT
    ```

  - If this change reflects a major bump, update the `versionPrefix` query parameter in the badge URLs below.
  - Add the appropriate label (`major` or `minor`) to the pull request so that the release drafter creates a separate, versioned release draft for the `master` branch and tracks changes appropriately.

### Post Release

- Verify that the release is available on [Axon Ivy Nexus][axon-ivy-nexus].
- Raise `web-tester` in consuming repos by triggering the [raise version job][raise-version-job].

## Authors

[ivyTeam](https://developer.axonivy.com/)

[![Axon Ivy](https://www.axonivy.com/hubfs/brand/axonivy-logo-black.svg)](http://www.axonivy.com)

## License

The Apache License, Version 2.0

[web-lts]: https://img.shields.io/maven-metadata/v.svg?versionPrefix=14&label=web-tester&logo=apachemaven&metadataUrl=https%3A%2F%2Fnexus.axonivy.com%2Frepository%2Fmaven-releases%2Fcom%2Faxonivy%2Fivy%2Fwebtest%2Fweb-tester%2Fmaven-metadata.xml
[web-le]: https://img.shields.io/maven-metadata/v.svg?label=LE&color=yellow&logo=apachemaven&metadataUrl=https%3A%2F%2Fnexus.axonivy.com%2Frepository%2Fmaven-releases%2Fcom%2Faxonivy%2Fivy%2Fwebtest%2Fweb-tester%2Fmaven-metadata.xml
[web-releases]: https://nexus.axonivy.com/#browse/browse:maven-releases
[prime-lts]: https://img.shields.io/maven-metadata/v.svg?versionPrefix=14&label=primeui-tester&logo=apachemaven&metadataUrl=https%3A%2F%2Fnexus.axonivy.com%2Frepository%2Fmaven-releases%2Fcom%2Faxonivy%2Fivy%2Fwebtest%2Fprimeui-tester%2Fmaven-metadata.xml
[prime-le]: https://img.shields.io/maven-metadata/v.svg?label=LE&color=yellow&logo=apachemaven&metadataUrl=https%3A%2F%2Fnexus.axonivy.com%2Frepository%2Fmaven-releases%2Fcom%2Faxonivy%2Fivy%2Fwebtest%2Fprimeui-tester%2Fmaven-metadata.xml
[prime-releases]: https://nexus.axonivy.com/#browse/browse:maven-releases
[bom-lts]: https://img.shields.io/maven-metadata/v.svg?versionPrefix=14&label=test-bom&logo=apachemaven&metadataUrl=https%3A%2F%2Fnexus.axonivy.com%2Frepository%2Fmaven-releases%2Fcom%2Faxonivy%2Fivy%2Ftest%2Ftest-bom%2Fmaven-metadata.xml
[bom-le]: https://img.shields.io/maven-metadata/v.svg?label=LE&color=yellow&logo=apachemaven&metadataUrl=https%3A%2F%2Fnexus.axonivy.com%2Frepository%2Fmaven-releases%2Fcom%2Faxonivy%2Fivy%2Ftest%2Ftest-bom%2Fmaven-metadata.xml
[bom-releases]: https://nexus.axonivy.com/#browse/browse:maven-releases
[raise-version-job]: https://jenkins.ivyteam.io/view/jobs/job/github-repo-manager_raise-web-tester-version/job/master/
[release-build]: https://jenkins.ivyteam.io/job/web-tester_release/
[axon-ivy-nexus]: https://nexus.axonivy.com/
