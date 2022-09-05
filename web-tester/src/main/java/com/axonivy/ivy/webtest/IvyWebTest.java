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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

/**
 * IvyWebTest is an JUnit5 annotation which can be used for Selenide test setup.
 *
 * <br>
 * It can be configured over system properties:
 * <ul>
 * <li><b>ivy.selenide.browser: </b>e.g chrome, firefox (default)</li>
 * <li><b>ivy.selenide.headless: </b>false or true (default)</li>
 * <li><b>ivy.selenide.reportfolder: </b>e.g
 * target/selenide/reports/&lt;testclass&gt;/&lt;testmethod&gt; (default)</li>
 * </ul>
 *
 * or annotation parameters: <b>@IvyWebTest(browser = "firefox", headless =
 * true, reportFolder = "target/selenide/reports")</b>
 *
 * @see WebDriverRunner#getWebDriver() use 'WebDriverRunner' to get the
 *      WebDriver
 * @see Selenide#open() use 'open' to browse a webpage
 * @see Selenide#$ use '$' to get an element by a select
 * @see Selenide#$$ use '$$' to get all elements found by a selector
 * @see SelenideElement#should(com.codeborne.selenide.Condition...) use
 *      '$.should()' to test element
 */
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Inherited
@ExtendWith(IvyWebTestExtension.class)
public @interface IvyWebTest {
  String browser() default IvyWebTestExtension.BROWSER_DEFAULT;

  boolean headless() default IvyWebTestExtension.HEADLESS_DEFAULT;

  String reportFolder() default IvyWebTestExtension.REPORT_FOLDER_DEFAULT;
}
