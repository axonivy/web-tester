<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.axonivy.ivy.webtest</groupId>
    <artifactId>web-config</artifactId>
    <version>9.4.2</version>
    <relativePath>maven-config</relativePath>
  </parent>

  <artifactId>web-module</artifactId>
  <version>9.4.2</version>
  <packaging>pom</packaging>

  <modules>
    <module>maven-config</module>
    <module>primeui-tester</module>
    <module>web-tester</module>
    <module>web-tester-product</module>
    <module>unit-tester</module>
  </modules>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-deploy-plugin</artifactId>
        <version>3.0.0</version>
        <configuration>
          <skip>true</skip>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
