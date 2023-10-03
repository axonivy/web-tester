/*
 * Copyright (C) 2021 Axon Ivy AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.axonivy.ivy.webtest.engine;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.codeborne.selenide.Selenide;

/**
 * <p>
 * Use the WebAppFixture to temporary change the active environment for your web tests.
 * <p>Changes are <b>not</b> rollbacked automatically to the previous value after the test.</p>
 * <p>To use the AppFixture annotate your test class with the {@link IvyWebTest} annotation.
 * Then add the AppFixture as parameter to any {@link Test}, {@link BeforeEach}, {@link AfterEach},
 * methods or the constructor of the test class.</p>
 * <p>Example:
 * <pre><code>
 * {@literal @IvyWebTest}
 * class Test {
 *   {@literal @Test}
 *   void test(WebAppFixture fixture) {
 *     open(EngineUrl.create().toUrl());
 *     $(By.id("sessionUserName")).shouldBe(Condition.exactText("Developer"));
 *     fixture.login("test", "test");
 *     $(By.id("sessionUserName")).shouldBe(Condition.exactText("test"));
 *   }
 * }
 * </code></pre>
 * @since 10.0.14
 */
public class WebAppFixture {

  /**
   * <p>Login to a user.
   * This user will be used as long as the browser context isn't closed or the {@link #logout()} method is called.
   * It uses the login form of the Dev-Workflow-UI.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   *   open(EngineUrl.create().toUrl());
   *   $(By.id("sessionUserName")).shouldBe(Condition.exactText("Developer"));
   *   fixture.login("test", "test");
   *   $(By.id("sessionUserName")).shouldBe(Condition.exactText("test"));
   * }
   * </code></pre>
   * @param username the name of the user
   * @param password the password of the user
   */
  public void login(String username, String password) {
    var currentUrl = Selenide.webdriver().driver().url();
    open(EngineUrl.create().app("").path("default-workflow/faces/login.xhtml").toUrl());
    $(By.id("loginForm:userName")).shouldBe(visible).sendKeys(username);
    $(By.id("loginForm:password")).shouldBe(visible).sendKeys(password);
    $(By.id("loginForm:login")).shouldBe(visible).click();
    $(By.id("loginForm:login")).shouldNotBe(visible);
    if (!"about:blank".equals(currentUrl)) {
      open(currentUrl);
    }
  }

  /**
   * <p>Logout the current user.
   * This method logout the current user by using the Dev-Workflow-UI.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   *   open(EngineUrl.create().toUrl());
   *   $(By.id("sessionUserName")).shouldBe(Condition.exactText("Developer"));
   *   fixture.logout();
   *   $(By.id("sessionUserName")).shouldBe(Condition.exactText("test"));
   * }
   * </code></pre>
   */
  public void logout() {
    var currentUrl = Selenide.webdriver().driver().url();
    open(EngineUrl.create().app("").path("default-workflow/faces/home.xhtml").toUrl());
    $(By.id("sessionUserName")).shouldBe(visible).click();
    $(By.id("sessionLogoutBtn")).shouldBe(visible).click();
    open(currentUrl);
  }

  /**
   * <p>Set a variable to a given value.
   * This variable will be set as long it is not been {@link #resetVar(String)}.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   *   fixture.var("myVar", "myNewValue");
   * }
   * </code></pre>
   */
  public void var(String name, String value) {
    try {
      var url = EngineUrl.create().rest("webtest/var").toUrl() + "?name=" + name + "&value=" + value;
      sendRequest(HttpRequest.newBuilder(new URI(url)).POST(BodyPublishers.noBody()));
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't set variable", ex);
    }
  }

  /**
   * <p>Reset a variable to the initial value.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   *   fixture.var("myVar", "myNewValue");
   *   //do some tests
   *   fixture.resetVar("myVar");
   * }
   * </code></pre>
   */
  public void resetVar(String name) {
    try {
      var url = EngineUrl.create().rest("webtest/var").toUrl() + "?name=" + name;
      sendRequest(HttpRequest.newBuilder(new URI(url)).DELETE());
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't remove variable", ex);
    }
  }

  /**
   * <p>Set an app configuration (like rest or web service,...) to a given value.
   * This configuration will be set as long it is not been {@link #resetConfig(String)}.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   *   fixture.config("RestClients.MyRestClient.Url", "${ivy.app.baseurl}/api/myCoolMockService");
   * }
   * </code></pre>
   */
  public void config(String name, String value) {
    try {
      var url = EngineUrl.create().rest("webtest/config").toUrl() + "?key=" + name + "&value=" + value;
      sendRequest(HttpRequest.newBuilder(new URI(url)).POST(BodyPublishers.noBody()));
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't set config", ex);
    }
  }

  /**
   * <p>Reset an app configuration (like rest or web service,...) to an initial value.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   *   fixture.config("RestClients.MyRestClient.Url", "${ivy.app.baseurl}/api/myCoolMockService");
   *   //do some tests
   *   fixture.resetConfig("RestClients.MyRestClient.Url");
   * }
   * </code></pre>
   */
  public void resetConfig(String name) {
    try {
      var url = EngineUrl.create().rest("webtest/config").toUrl() + "?key=" + name;
      sendRequest(HttpRequest.newBuilder(new URI(url)).DELETE());
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't remove config", ex);
    }
  }

  private void sendRequest(HttpRequest.Builder requestBuilder) throws Exception {
    var client = HttpClient.newHttpClient();
    var request = requestBuilder.header("X-Requested-By", "webtest").build();
    var response = client.send(request, BodyHandlers.ofString());
    if (response.statusCode() > 399) {
      throw new RuntimeException("Couldn't send web app fixture request (status code: " + response.statusCode() + "): " + response.body());
    }
  }
}
