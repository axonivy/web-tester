# web-tester
The `web-tester` artifact provides you a API which helps you test your JSF-Page.
With this API it is easy to setup your test environment and send requests
against your [Axon.ivy Engine](https://developer.axonivy.com/download). 

## primeui-tester
If your JSF-Page contains [PrimeFaces ](https://www.primefaces.org/showcase/)
widgets, the `primeui-tester` gives you the possibility to interact with those
widgets and check if it's in the condition you expected it to be. 

# How to use in your project
The web-tester runs with [Selenide](https://selenide.org/),
[Selenium](https://selenium.dev/projects/) and [JUnit
5](https://junit.org/junit5/). Simply add this library to your dependencies in
the pom.xml:

```xml
<repositories>
  <repository>
    <id>central.snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>

<dependencies>
...
  <dependency>
    <groupId>com.axonivy.ivy.webtest</groupId>
    <artifactId>web-tester</artifactId>
    <version>8.0.0-SNAPSHOT</version>
    <scope>tests</scope>
  </dependency>
</dependencies>
```

Add a new test class to test your process (e.g.
[WebTestRegistrationForm.java](https://github.com/axonivy/project-build-examples/blob/master/compile-test/crmIntegrationTests/src_test/ch/ivyteam/integrationtest/WebTestRegistrationForm.java))
or a PrimeFaces widget (e.g.
[TestPrimeUi.java](https://github.com/ivy-supplements/web-tester/blob/master/primeui-tester/src/test/java/com/axonivy/ivy/webtest/primeui/TestPrimeUi.java)):

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

## Authors

[ivyTeam](https://developer.axonivy.com/)

[![Axon.ivy](https://www.axonivy.com/hubfs/brand/axonivy-logo-black.svg)](http://www.axonivy.com)


## License
The Apache License, Version 2.0
