package com.axonivy.ivy.webtest.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

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
    String baseUrl = "http://localhost:8081/";
    assertThat(EngineUrl.createRest("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/api/");
    assertThat(EngineUrl.createSoap("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/ws/");
    assertThat(EngineUrl.createProcess("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/pro/");
    assertThat(EngineUrl.createStaticView("")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/faces/view/");
    assertThat(EngineUrl.createProcess("/test.ivp")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/pro/test.ivp");
    assertThat(EngineUrl.createProcess("test.ivp")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/pro/test.ivp");
    assertThat(EngineUrl.create().base("http://base").app("app").servlet("servlet").path("path").toUrl())
            .isEqualTo("http://base/app/servlet/path");
    assertThat(EngineUrl.create().base("http://base/").app("/app/").servlet("/servlet/").path("/path/").toUrl())
            .isEqualTo("http://base/app/servlet/path");
    assertThat(EngineUrl.createProcess("test.ivp")).isEqualTo(baseUrl + EngineUrl.DESIGNER + "/pro/test.ivp");
    assertThat(EngineUrl.isDesigner()).isEqualTo(true);
  }
  
  @Test
  void testEngineUrls()
  {
    String baseUrl = "http://www.axonivy.com:8080/ivy/";
    String app = "test";
    System.setProperty(EngineUrl.TEST_ENGINE_URL, baseUrl);
    System.setProperty(EngineUrl.TEST_ENGINE_APP, app);
    assertThat(EngineUrl.createRest("")).isEqualTo(baseUrl + app + "/api/");
    assertThat(EngineUrl.createSoap("")).isEqualTo(baseUrl + app + "/ws/");
    assertThat(EngineUrl.createProcess("")).isEqualTo(baseUrl + app + "/pro/");
    assertThat(EngineUrl.createStaticView("")).isEqualTo(baseUrl + app + "/faces/view/");
    assertThat(EngineUrl.isDesigner()).isEqualTo(false);
  }
  
}
