# primeui-tester
primeui-tester is a API which helps you test your JSF-Page containing primefaces
widgets. The API provides you methods to interact with your widgets and check if
it's in the condition you expected it to be. 

# How to use in your project
The primeui-tester runs with [Selenide](https://selenide.org/),
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
    <groupId>com.axonivy.ivy.supplements</groupId>
    <artifactId>primeui-tester</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>tests</scope>
  </dependency>
</dependencies>
```

To test you're primefaces widgets add a new test class: e.g. [TestPrimeUi.java](https://github.com/ivy-supplements/primeui-tester/blob/master/primeui-tester/src/test/java/com/axonivy/ivy/supplements/primeui/tester/TestPrimeUi.java)

```java
@IvySelenide
public class TestPrimeUi
{

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

  ...
}
```

## License
The Apache License, Version 2.0
