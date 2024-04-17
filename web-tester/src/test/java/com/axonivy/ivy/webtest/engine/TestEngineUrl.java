package com.axonivy.ivy.webtest.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.engine.EngineUrl.SERVLET;
import com.codeborne.selenide.Configuration;

class TestEngineUrl {
  private static String BASE_URL = "http://www.axonivy.com:8080/ivy/";
  private static String APP = "test";

  @BeforeEach
  void setup() {
    System.setProperty(EngineUrl.TEST_ENGINE_URL, BASE_URL);
    System.setProperty(EngineUrl.TEST_ENGINE_APP, APP);
  }

  @AfterEach
  void cleanup() {
    System.clearProperty(EngineUrl.TEST_ENGINE_URL);
    System.clearProperty(EngineUrl.TEST_ENGINE_APP);
  }

  @Test
  void designerUrls() {
    var remote = Configuration.remote;
    try {
      System.clearProperty(EngineUrl.TEST_ENGINE_URL);
      System.clearProperty(EngineUrl.TEST_ENGINE_APP);
      Configuration.remote = null;
      String baseUrl = "http://localhost:8081/";
      assertThat(EngineUrl.createRestUrl("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/api");
      assertThat(EngineUrl.createWebServiceUrl("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/ws");
      assertThat(EngineUrl.createProcessUrl("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/pro");
      assertThat(EngineUrl.createStaticViewUrl("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/faces/view");
      assertThat(EngineUrl.createCaseMapUrl("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/casemap");
      assertThat(EngineUrl.isDesigner()).isEqualTo(true);
    } finally {
      Configuration.remote = remote;
    }
  }

  @Test
  void engineUrls() {
    assertThat(EngineUrl.createRestUrl("")).isEqualTo(BASE_URL + APP + "/api");
    assertThat(EngineUrl.createWebServiceUrl("")).isEqualTo(BASE_URL + APP + "/ws");
    assertThat(EngineUrl.createProcessUrl("")).isEqualTo(BASE_URL + APP + "/pro");
    assertThat(EngineUrl.createStaticViewUrl("")).isEqualTo(BASE_URL + APP + "/faces/view");
    assertThat(EngineUrl.createCaseMapUrl("")).isEqualTo(BASE_URL + APP + "/casemap");
    assertThat(EngineUrl.isDesigner()).isEqualTo(false);
  }

  @Test
  void urlBuidler() {
    String baseUrl = "http://test/";
    String app = "bla";
    assertThat(EngineUrl.create().base(baseUrl).app(app).process("/test.ivp").toUrl())
            .isEqualTo(baseUrl + app + "/pro/test.ivp");
    assertThat(EngineUrl.create().base(baseUrl).app(app).process("test.ivp").toUrl())
            .isEqualTo(baseUrl + app + "/pro/test.ivp");
    assertThat(EngineUrl.create().base(baseUrl).app("").path("test").toUrl()).isEqualTo(baseUrl + "test");
    assertThat(EngineUrl.create().base(baseUrl).app(app).servlet(null).path("test").toUrl())
            .isEqualTo(baseUrl + app + "/test");
    assertThat(
            EngineUrl.create().base("http://base").app("app").servlet(SERVLET.PROCESS).path("path").toUrl())
                    .isEqualTo("http://base/app/pro/path");
    assertThat(EngineUrl.create().base("http://base/").app("/app/").servlet(SERVLET.PROCESS).path("/path/")
            .toUrl()).isEqualTo("http://base/app/pro/path/");
  }

  @Test
  void queryParam() {
    var url = EngineUrl.create().staticView("abc.xhtml").queryParam("userName", "crazy user").toUrl();
    assertThat(url).isEqualTo(BASE_URL + APP + "/faces/view/abc.xhtml?userName=crazy+user");
  }

  @Test
  void queryParamInPath() {
    assertThatThrownBy(() -> EngineUrl.create().path("bla?embedInFrame")).isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("Adding query parameters via the path method will not work");
    assertThat(EngineUrl.createProcessUrl("start.ivp?locale=en&format=DE")).isEqualTo("http://www.axonivy.com:8080/ivy/test/pro/start.ivp?locale=en&format=DE");
    assertThat(EngineUrl.createRestUrl("variable/myVar?value=new")).isEqualTo("http://www.axonivy.com:8080/ivy/test/api/variable/myVar?value=new");
  }
}
