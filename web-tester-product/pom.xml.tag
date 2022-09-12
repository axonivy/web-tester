<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.axonivy.ivy.webtest</groupId>
    <artifactId>web-config</artifactId>
    <version>9.4.2</version>
    <relativePath>../maven-config</relativePath>
  </parent>

  <groupId>com.axonivy.ivy.webtest</groupId>
  <artifactId>web-tester-product</artifactId>
  <version>9.4.2</version>
  <packaging>pom</packaging>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>3.4.2</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>single</goal>
            </goals>
            <configuration>              
              <appendAssemblyId>false</appendAssemblyId>
              <descriptors>
                <descriptor>zip.xml</descriptor>
              </descriptors>
            </configuration>
          </execution>
        </executions>
      </plugin>
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
