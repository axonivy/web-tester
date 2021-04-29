[![web-tester version][0]][1] [![primeui-tester version][2]][3] [![unit-tester version][4]][5]

# web-tester
The `web-tester` artifact provides you a API which helps you test your JSF-Page.
With this API it is easy to setup your test environment and send requests
against your [Axon Ivy Engine](https://developer.axonivy.com/download). 

## primeui-tester
If your JSF-Page contains [PrimeFaces ](https://www.primefaces.org/showcase/)
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
    <version>9.2.1</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

Add a new test class to test your process (e.g.
[WebTestRegistrationForm.java](https://github.com/axonivy/project-build-examples/blob/master/compile-test/crmIntegrationTests/src_test/ch/ivyteam/integrationtest/WebTestRegistrationForm.java))
or a PrimeFaces widget (e.g.
[TestPrimeUi.java](primeui-tester/src/test/java/com/axonivy/ivy/webtest/primeui/TestPrimeUi.java)):

```java
@IvyWebTest
public class WebTest
{

  @Test
  public void registerNewCustomer()
  {
    open(EngineUrl.base());
    $(By.linkText("customer/register.ivp")).shouldBe(visible).click();
    $(By.id("form:firstname")).sendKeys("Unit");
    $(By.id("form:lastname")).sendKeys("Test");
    $(By.id("form:submit")).shouldBe(enabled).click();
    $(By.id("form:newCustomer")).shouldBe(visible, text("Unit Test"));
  }

  @Test
  public void testSelectOneMenu()
  {
    open("https://primefaces.org/showcase/ui/input/oneMenu.xhtml");
    SelectOneMenu selectOne = PrimeUi.selectOne(selectMenuForLabel("Basic:"));
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

}
```

## Changelog
- [web-tester](web-tester/CHANGELOG.md)
- [primeui-tester](primeui-tester/CHANGELOG.md)

## Authors

[ivyTeam](https://developer.axonivy.com/)

[![Axon Ivy](https://www.axonivy.com/hubfs/brand/axonivy-logo-black.svg)](http://www.axonivy.com)


## License
The Apache License, Version 2.0

[0]: https://img.shields.io/badge/web--tester-9.2.2-green
[1]: https://repo1.maven.org/maven2/com/axonivy/ivy/webtest/web-tester/
[2]: https://img.shields.io/badge/primeui--tester-9.2.2-green
[3]: https://repo1.maven.org/maven2/com/axonivy/ivy/webtest/primeui-tester/
[4]: https://img.shields.io/badge/unit--tester-9.2.2-green
[5]: https://repo1.maven.org/maven2/com/axonivy/ivy/test/unit-tester/
