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
package com.axonivy.ivy.webtest;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.axonivy.ivy.webtest.engine.WebAppFixture;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

class IvyWebTestExtension implements BeforeEachCallback, BeforeAllCallback, ParameterResolver {

  static final String BROWSER_DEFAULT = "firefox";
  static final boolean HEADLESS_DEFAULT = true;
  static final String REPORT_FOLDER_DEFAULT = "target/selenide/reports";

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    IvyTestRuntimeProps.loadToSystem();
    Configuration.browser = browser(context);
    Configuration.headless = headless(context);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Configuration.reportsFolder = reportFolder(context);
    Selenide.open();
  }

  private boolean headless(ExtensionContext context) {
    return context.getConfigurationParameter("ivy.selenide.headless")
        .map(Boolean::parseBoolean)
        .orElseGet(() -> findAnnotation(context).map(IvyWebTest::headless)
            .orElse(HEADLESS_DEFAULT));
  }

  private String browser(ExtensionContext context) {
    return context.getConfigurationParameter("ivy.selenide.browser")
        .orElseGet(() -> findAnnotation(context).map(IvyWebTest::browser)
            .orElse(BROWSER_DEFAULT));
  }

  private String reportFolder(ExtensionContext context) {
    String methodDir = context.getTestClass().map(c -> c.getName() + "/").orElse("") +
        context.getTestMethod().map(Method::getName).orElse("");
    String reportDir = context.getConfigurationParameter("ivy.selenide.reportfolder")
        .orElseGet(() -> findAnnotation(context).map(IvyWebTest::reportFolder)
            .orElse(REPORT_FOLDER_DEFAULT));
    return Path.of(reportDir, methodDir).toString();
  }

  private Optional<IvyWebTest> findAnnotation(ExtensionContext context) {
    return context.getTestClass().map(c -> c.getAnnotation(IvyWebTest.class));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return parameterContext.getParameter().getType().isAssignableFrom(WebAppFixture.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    return new WebAppFixture();
  }

}
