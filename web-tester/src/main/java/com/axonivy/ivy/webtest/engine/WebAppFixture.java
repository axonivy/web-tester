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

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriBuilder;

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
 * &nbsp;&nbsp;{@literal @Test}
 * &nbsp;&nbsp;void test(WebAppFixture fixture) {
 * &nbsp;&nbsp;&nbsp;&nbsp;open(EngineUrl.create().toUrl());
 * &nbsp;&nbsp;&nbsp;&nbsp;$(By.id("sessionUserName")).shouldBe(Condition.exactText("Developer"));
 * &nbsp;&nbsp;&nbsp;&nbsp;fixture.login("test", "test");
 * &nbsp;&nbsp;&nbsp;&nbsp;$(By.id("sessionUserName")).shouldBe(Condition.exactText("test"));
 * &nbsp;&nbsp;}
 * }
 * </code></pre>
 * @since 10.0.14
 */
public class WebAppFixture {

  private static final String VAR_PATH = "variables";
  private static final String CONFIG_PATH = "configs";

  /**
   * <p>Login to a user.
   * This user will be used as long as the browser context isn't closed or the {@link #logout()} method is called.
   * It uses the login form of the Dev-Workflow-UI.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   * &nbsp;&nbsp;open(EngineUrl.create().toUrl());
   * &nbsp;&nbsp;$(By.id("sessionUserName")).shouldBe(Condition.exactText("Developer"));
   * &nbsp;&nbsp;fixture.login("test", "test");
   * &nbsp;&nbsp;$(By.id("sessionUserName")).shouldBe(Condition.exactText("test"));
   * }
   * </code></pre>
   * @param username the name of the user
   * @param password the password of the user
   */
  public void login(String username, String password) {
    var currentUrl = Selenide.webdriver().driver().url();
    open(EngineUrl.create().app("").path("dev-workflow-ui/faces/login.xhtml").toUrl());
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
   * &nbsp;&nbsp;open(EngineUrl.create().toUrl());
   * &nbsp;&nbsp;$(By.id("sessionUserName")).shouldBe(Condition.exactText("Developer"));
   * &nbsp;&nbsp;fixture.logout();
   * &nbsp;&nbsp;$(By.id("sessionUserName")).shouldBe(Condition.exactText("test"));
   * }
   * </code></pre>
   */
  public void logout() {
    var currentUrl = Selenide.webdriver().driver().url();
    open(EngineUrl.create().app("").path("dev-workflow-ui/faces/home.xhtml").toUrl());
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
   * &nbsp;&nbsp;fixture.var("myVar", "myNewValue");
   * }
   * </code></pre>
   */
  public void var(String name, String value) {
    try {
      var url = configRestUrl().path(VAR_PATH).path(name).build();
      sendRequest(HttpRequest.newBuilder(url).POST(BodyPublishers.ofString(value)));
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
   * &nbsp;&nbsp;fixture.var("myVar", "myNewValue");
   * &nbsp;&nbsp;//do some tests
   * &nbsp;&nbsp;fixture.resetVar("myVar");
   * }
   * </code></pre>
   */
  public void resetVar(String name) {
    try {
      var url = configRestUrl().path(VAR_PATH).path(name).build();
      sendRequest(HttpRequest.newBuilder(url).DELETE());
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
   * &nbsp;&nbsp;fixture.config("RestClients.MyRestClient.Url", "${ivy.app.baseurl}/api/myCoolMockService");
   * }
   * </code></pre>
   */
  public void config(String name, String value) {
    try {
      var url = configRestUrl().path(CONFIG_PATH).path(name).build();
      sendRequest(HttpRequest.newBuilder(url).POST(BodyPublishers.ofString(value)));
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't set config", ex);
    }
  }

  /**
   * <p>Set an app configuration (like rest or web service features,...) to a given value.
   * This configuration will be set as long it is not been {@link #resetConfig(String)}.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   * &nbsp;&nbsp;fixture.config("RestClients.MyRestClient.Features", List.of("myFirstFeature", "mySecondFeature");
   * }
   * </code></pre>
   */
  public void config(String name, List<String> value) {
    config(name, value.stream().collect(Collectors.joining("\n")));
  }

  /**
   * <p>Reset an app configuration (like rest or web service,...) to an initial value.</p>
   * <p>Example:
   * <pre><code>
   * {@literal @Test}
   * void test(WebAppFixture fixture) {
   * &nbsp;&nbsp;fixture.config("RestClients.MyRestClient.Url", "${ivy.app.baseurl}/api/myCoolMockService");
   * &nbsp;&nbsp;//do some tests
   * &nbsp;&nbsp;fixture.resetConfig("RestClients.MyRestClient.Url");
   * }
   * </code></pre>
   */
  public void resetConfig(String name) {
    try {
      var url = configRestUrl().path(CONFIG_PATH).path(name).build();
      sendRequest(HttpRequest.newBuilder(url).DELETE());
    } catch (Exception ex) {
      throw new RuntimeException("Couldn't remove config", ex);
    }
  }

  private static UriBuilder configRestUrl() {
    return EngineUrl.create().app("system").rest("apps").builder().path(EngineUrl.applicationName());
  }

  private void sendRequest(HttpRequest.Builder requestBuilder) throws Exception {
    var client = HttpClient.newBuilder().build();
    var request = requestBuilder
        .header("Authorization", basicAuth("admin", "admin"))
        .header("X-Requested-By", "webtest").build();
    var response = client.send(request, BodyHandlers.ofString());
    if (response.statusCode() > 399) {
      throw new RuntimeException("Couldn't send web app fixture request (status code: " + response.statusCode() + "): " + response.body());
    }
  }

  private static final String basicAuth(String username, String password) {
    var value = username + ":" + password;
    return "Basic " + Base64.getEncoder().encodeToString(value.getBytes());
  }
}
