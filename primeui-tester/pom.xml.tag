<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.axonivy.ivy.webtest</groupId>
    <artifactId>web-config</artifactId>
    <version>9.4.2</version>
    <relativePath>../maven-config</relativePath>
  </parent>

  <artifactId>primeui-tester</artifactId>
  <version>9.4.2</version>
  <packaging>jar</packaging>
  
  <name>primeui-tester</name>
  <description>API that simplifies testing of JSF pages that contain Primefaces Widgets.</description>
  <url>https://github.com/ivy-supplements/web-tester/primeui-tester</url>

  <dependencies>
    <dependency>
      <groupId>com.codeborne</groupId>
      <artifactId>selenide</artifactId>
      <version>6.7.4</version>

      <!-- to prevent error logs when executing IvyTest and IvyProcessTest and having primeui-tester dependency in same test project -->
      <exclusions>
    		<exclusion>
    			<groupId>io.opentelemetry</groupId>
    			<artifactId>*</artifactId>
    		</exclusion>
    	</exclusions>
    </dependency>
    <dependency>
      <groupId>com.axonivy.ivy.test</groupId>
      <artifactId>unit-tester</artifactId>
      <version>${project.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.12.0</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-deploy-plugin</artifactId>
        <version>3.0.0</version>
        <configuration>
          <skip>false</skip>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
