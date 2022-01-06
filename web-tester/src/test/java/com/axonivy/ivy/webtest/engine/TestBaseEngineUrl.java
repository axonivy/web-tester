package com.axonivy.ivy.webtest.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;

/**
 * Test {@link BaseEngineUrl}
 * @author rwei
 * @since Jan 5, 2022
 */
class TestBaseEngineUrl {

  @AfterEach
  void clearConfigs() {
    Configuration.remote = null;
    System.clearProperty(BaseEngineUrl.TEST_ENGINE_URL);
  }

  @Test
  void defaultValue() {
    assertThat(BaseEngineUrl.url())
        .as("Engine url is not configured using default value")
        .isEqualTo("http://localhost:8081/");
  }

  @Test
  void systemProperty() {
    System.setProperty(BaseEngineUrl.TEST_ENGINE_URL, "http://localhost:9090/ivy");
    assertThat(BaseEngineUrl.url())
        .as("Engine url is configured using a system property")
        .isEqualTo("http://localhost:9090/ivy");
  }

  @Test
  void systemProperty_noValidUri() {
    System.setProperty(BaseEngineUrl.TEST_ENGINE_URL, "gugus sugus");
    assertThat(BaseEngineUrl.url())
        .as("Engine url is configured using a system property")
        .isEqualTo("gugus sugus");
  }


  @Test
  void driverRemote() throws UnknownHostException {
    Configuration.remote = "http://selenium:5678/wd/hub";
    assertThat(BaseEngineUrl.url())
        .as("Selenium is running remote therefore replace localhost in engine url with explicit host name")
        .isEqualTo("http://"+hostName()+":8081/");
  }

  @Test
  void driverRemote_invalidUri() {
    Configuration.remote = "hello world";
    assertThat(BaseEngineUrl.url())
        .as("Selenium remote config is invalid do not manipulate engine url")
        .isEqualTo("http://localhost:8081/");
  }

  @Test
  void driverRemote_ivyLocalIp4() throws UnknownHostException {
    Configuration.remote = "http://selenium:5678/wd/hub";
    System.setProperty(BaseEngineUrl.TEST_ENGINE_URL, "http://127.0.0.1:8081/");
    assertThat(BaseEngineUrl.url())
        .as("Selenium is running remote therefore replace localhost in engine url with explicit host name")
        .isEqualTo("http://"+hostName()+":8081/");
  }

  @Test
  void driverRemote_ivyLocalIp6() throws UnknownHostException {
    Configuration.remote = "http://selenium:5678/wd/hub";
    System.setProperty(BaseEngineUrl.TEST_ENGINE_URL, "http://[::1]:8080/ivy");
    assertThat(BaseEngineUrl.url())
        .as("Selenium is running remote therefore replace localhost in engine url with explicit host name")
        .isEqualTo("http://"+hostName()+":8080/ivy");
  }

  @Test
  void driverLocal()  {
    Configuration.remote = "http://localhost:5678/wd/hub";
    assertThat(BaseEngineUrl.url())
        .as("Selenium is running local no need to replace localhost")
        .isEqualTo("http://localhost:8081/");
  }

  @Test
  void driverRemote_ivyRemote()  {
    Configuration.remote = "http://localhost:5678/wd/hub";
    System.setProperty(BaseEngineUrl.TEST_ENGINE_URL, "http://dev.axonivy.com:9090/ivy");
    assertThat(BaseEngineUrl.url())
        .as("Engine url is not localhost no need to change it even if driver runs remote")
        .isEqualTo("http://dev.axonivy.com:9090/ivy");
  }

  private String hostName() throws UnknownHostException {
    return InetAddress.getLocalHost().getHostName();
  }
}
