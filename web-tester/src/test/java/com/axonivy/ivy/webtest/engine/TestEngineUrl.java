package com.axonivy.ivy.webtest.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.engine.EngineUrl.SERVLET;

public class TestEngineUrl
{
  
  @AfterEach
  void cleanup()
  {
    System.clearProperty(EngineUrl.TEST_ENGINE_URL);
    System.clearProperty(EngineUrl.TEST_ENGINE_APP);
  }

  @Test
  void testDesignerUrls()
  {
    String baseUrl = "http://localhost:8081/ivy/";
    assertThat(EngineUrl.createRestUrl("")).isEqualTo(baseUrl + "api/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.createWebServiceUrl("")).isEqualTo(baseUrl + "ws/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.createProcessUrl("")).isEqualTo(baseUrl + "pro/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.createStaticViewUrl("")).isEqualTo(baseUrl + "faces/view/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.isDesigner()).isEqualTo(true);
  }
  
  @Test
  void testEngineUrls()
  {
    String baseUrl = "http://www.axonivy.com:8080/ivy/";
    String app = "test";
    System.setProperty(EngineUrl.TEST_ENGINE_URL, baseUrl);
    System.setProperty(EngineUrl.TEST_ENGINE_APP, app);
    assertThat(EngineUrl.createRestUrl("")).isEqualTo(baseUrl + "api/" + app);
    assertThat(EngineUrl.createWebServiceUrl("")).isEqualTo(baseUrl + "ws/" + app);
    assertThat(EngineUrl.createProcessUrl("")).isEqualTo(baseUrl + "pro/" + app);
    assertThat(EngineUrl.createStaticViewUrl("")).isEqualTo(baseUrl + "faces/view/" + app);
    assertThat(EngineUrl.isDesigner()).isEqualTo(false);
  }
  
  @Test
  void testUrlBuidler()
  {
    String baseUrl = "http://test/";
    String app = "bla";
    assertThat(EngineUrl.create().base(baseUrl).app(app).process("/test.ivp").toUrl()).isEqualTo(baseUrl + "pro/" + app + "/test.ivp");
    assertThat(EngineUrl.create().base(baseUrl).app(app).process("test.ivp").toUrl()).isEqualTo(baseUrl + "pro/" + app + "/test.ivp");
    assertThat(EngineUrl.create().base(baseUrl).app("").path("test").toUrl()).isEqualTo(baseUrl + "test");
    assertThat(EngineUrl.create().base(baseUrl).app(app).servlet(null).path("test").toUrl()).isEqualTo(baseUrl + app + "/test");
    assertThat(EngineUrl.create().base("http://base").app("app").servlet(SERVLET.PROCESS).path("path").toUrl())
            .isEqualTo("http://base/pro/app/path");
    assertThat(EngineUrl.create().base("http://base/").app("/app/").servlet(SERVLET.PROCESS).path("/path/").toUrl())
            .isEqualTo("http://base/pro/app/path");
  }
  
}
