package com.axonivy.ivy.webtest.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.engine.EngineUrl.SERVLET;
import com.codeborne.selenide.Configuration;

class TestEngineUrl {
  private static String BASE_URL = "http://www.axonivy.com:8080";
  private static String APP = "test";
  private static String CONTEXT = "ivy";

  @BeforeEach
  void setup() {
    System.setProperty(EngineUrl.TEST_ENGINE_URL, BASE_URL);
    System.setProperty(EngineUrl.TEST_ENGINE_APP, APP);
    System.setProperty(EngineUrl.TEST_ENGINE_CONTEXT, CONTEXT);
  }

  @AfterEach
  void cleanup() {
    System.clearProperty(EngineUrl.TEST_ENGINE_URL);
    System.clearProperty(EngineUrl.TEST_ENGINE_APP);
    System.clearProperty(EngineUrl.TEST_ENGINE_CONTEXT);
  }

  @Test
  void designerUrls() {
    var remote = Configuration.remote;
    try {
      var appName = "Developer-testee";
      var engineUrl = "http://localhost:8080";
      var context = "~Developer-testee";
      System.setProperty(EngineUrl.TEST_ENGINE_URL, engineUrl);
      System.setProperty(EngineUrl.TEST_ENGINE_APP, appName);
      System.setProperty(EngineUrl.TEST_ENGINE_CONTEXT, context);
      Configuration.remote = null;
      var baseUrl = engineUrl + "/" + context + "/" + appName;
      assertThat(EngineUrl.createRestUrl("")).isEqualTo(baseUrl + "/api");
      assertThat(EngineUrl.createWebServiceUrl("")).isEqualTo(baseUrl + "/ws");
      assertThat(EngineUrl.createProcessUrl("")).isEqualTo(baseUrl + "/pro");
      assertThat(EngineUrl.createStaticViewUrl("")).isEqualTo(baseUrl + "/faces/view");
      assertThat(EngineUrl.createCaseMapUrl("")).isEqualTo(baseUrl + "/casemap");
      assertThat(EngineUrl.isDesigner())
        .as("detects Designer mode")
        .isTrue();
    } finally {
      System.clearProperty(EngineUrl.TEST_ENGINE_URL);
      System.clearProperty(EngineUrl.TEST_ENGINE_APP);
      System.clearProperty(EngineUrl.TEST_ENGINE_CONTEXT);
      Configuration.remote = remote;
    }
  }

  @Test
  void engineUrlNotSet() {
    var remote = Configuration.remote;
    try {
      System.clearProperty(EngineUrl.TEST_ENGINE_URL);
      System.clearProperty(EngineUrl.TEST_ENGINE_APP);
      Configuration.remote = null;
      assertThatThrownBy(EngineUrl::base)
          .isInstanceOf(RuntimeException.class)
          .hasMessageStartingWith("No valid engine url provided.");
    } finally {
      Configuration.remote = remote;
    }
  }

  @Test
  void engineUrls() {
    var engineUrl = BASE_URL + "/" + CONTEXT + "/"+ APP;
    assertThat(EngineUrl.createRestUrl("")).isEqualTo(engineUrl + "/api");
    assertThat(EngineUrl.createWebServiceUrl("")).isEqualTo(engineUrl + "/ws");
    assertThat(EngineUrl.createProcessUrl("")).isEqualTo(engineUrl + "/pro");
    assertThat(EngineUrl.createStaticViewUrl("")).isEqualTo(engineUrl + "/faces/view");
    assertThat(EngineUrl.createCaseMapUrl("")).isEqualTo(engineUrl + "/casemap");
    assertThat(EngineUrl.isDesigner()).isFalse();
  }

  @Test
  void urlBuilder() {
    String baseUrl = "http://test";
    String app = "bla";
    String context = "blub";
    assertThat(EngineUrl.create().base(baseUrl).context(context).app(app).process("/test.ivp").toUrl())
        .isEqualTo(baseUrl + "/" + context + "/" + app + "/pro/test.ivp");
    assertThat(EngineUrl.create().base(baseUrl).context(context).app(app).process("test.ivp").toUrl())
        .isEqualTo(baseUrl + "/" + context + "/" + app + "/pro/test.ivp");
    assertThat(EngineUrl.create().base(baseUrl).context("").app("").path("test").toUrl())
        .isEqualTo(baseUrl + "/test");
    assertThat(EngineUrl.create().base(baseUrl).context(context).app(app).servlet(null).path("test").toUrl())
        .isEqualTo(baseUrl + "/" + context + "/" + app + "/test");
    assertThat(
        EngineUrl.create().base("http://base").context("context").app("app").servlet(SERVLET.PROCESS).path("path").toUrl())
            .isEqualTo("http://base/context/app/pro/path");
    assertThat(EngineUrl.create().base("http://base/").context("/context/").app("/app/").servlet(SERVLET.PROCESS).path("/path/")
        .toUrl()).isEqualTo("http://base/context/app/pro/path/");
  }

  @Test
  void queryParam() {
    var url = EngineUrl.create().staticView("abc.xhtml").queryParam("userName", "crazy user").toUrl();
    assertThat(url).isEqualTo(BASE_URL + "/" + CONTEXT + "/" + APP + "/faces/view/abc.xhtml?userName=crazy+user");
  }

  @Test
  void queryParamInPath() {
    var url = EngineUrl.create();
    assertThatThrownBy(() -> url.path("bla?embedInFrame")).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Adding query parameters via the path method will not work");
    assertThat(EngineUrl.createProcessUrl("start.ivp?locale=en&format=DE")).isEqualTo("http://www.axonivy.com:8080/ivy/test/pro/start.ivp?locale=en&format=DE");
    assertThat(EngineUrl.createRestUrl("variable/myVar?value=new")).isEqualTo("http://www.axonivy.com:8080/ivy/test/api/variable/myVar?value=new");
  }

  @Test
  void configUri() {
    assertThat(WebAppFixture.configRestUrl().build().toASCIIString())
        .as("Configs endpoints for WebAppFixture live on system app.")
        .isEqualTo(BASE_URL + "/system/api/apps/"+ CONTEXT+ "/"+APP);
  }
}
