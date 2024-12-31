package com.axonivy.demo.test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;
import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverConditions;

@IvyWebTest
class WebTestAppFixtureIT {

  @Test
  void login(WebAppFixture fixture) {
    open(EngineUrl.createProcessUrl("web-tester-fixture/18AF06DD4E1A49B8/start.ivp"));
    fixture.login("test", "test");
    Selenide.webdriver().shouldHave(WebDriverConditions.urlContaining("web-tester-fixture"));
    assertCurrentUser("test");

    fixture.logout();
    assertCurrentUser("Unknown User");

    fixture.login("test", "test");
    assertCurrentUser("test");
  }

  @Test
  void variable(WebAppFixture fixture) {
    fixture.var("myVar", "{hello}");
    open(EngineUrl.createProcessUrl("web-tester-fixture/18AF06DD4E1A49B8/start.ivp"));
    assertVariable("{hello}");

    fixture.resetVar("myVar");
    Selenide.refresh();
    assertVariable("init");
  }

  @Test
  void config(WebAppFixture fixture) {
    fixture.config("Variables.myVar", "${ivy.api.base}/hello");
    open(EngineUrl.createProcessUrl("web-tester-fixture/18AF06DD4E1A49B8/start.ivp"));
    assertVariable("${ivy.api.base}/hello");

    fixture.resetConfig("Variables.myVar");
    Selenide.refresh();
    assertVariable("init");
  }

  private void assertCurrentUser(String user) {
    open(EngineUrl.create().toUrl());
    $(By.id("sessionUserName")).shouldBe(text(user));
  }

  private void assertVariable(String variable) {
    $(By.id("form:variable")).shouldBe(exactText(variable));
  }

}
