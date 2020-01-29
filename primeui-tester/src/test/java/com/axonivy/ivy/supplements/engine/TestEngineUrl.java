package com.axonivy.ivy.supplements.engine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestEngineUrl
{

  @Test
  void testDesignerUrls()
  {
    String baseUrl = "http://localhost:8081/ivy/";
    assertThat(EngineUrl.rest()).isEqualTo(baseUrl + "api/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.process()).isEqualTo(baseUrl + "pro/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.staticView()).isEqualTo(baseUrl + "faces/view/" + EngineUrl.DESIGNER);
    assertThat(EngineUrl.isDesigner()).isEqualTo(true);
  }
  
  @Test
  void testEngineUrls()
  {
    String baseUrl = "http://www.axonivy.com:8080/ivy/";
    String app = "test";
    System.setProperty("test.engine.url", baseUrl);
    System.setProperty("test.engine.app", app);
    assertThat(EngineUrl.rest()).isEqualTo(baseUrl + "api/" + app);
    assertThat(EngineUrl.process()).isEqualTo(baseUrl + "pro/" + app);
    assertThat(EngineUrl.staticView()).isEqualTo(baseUrl + "faces/view/" + app);
    assertThat(EngineUrl.isDesigner()).isEqualTo(false);
  }
  
}
