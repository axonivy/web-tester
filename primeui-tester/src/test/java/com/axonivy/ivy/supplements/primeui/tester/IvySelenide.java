package com.axonivy.ivy.supplements.primeui.tester;

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
 * IvySelenide is an JUnit5 annotation which can be used for Selenide test setup.
 * 
 * <p>It can be configured over system properties:
 * <ul>
 *   <li><b>ivy.selenide.browser: </b>e.g chrome, firefox (default)</li>
 *   <li><b>ivy.selenide.headless: </b>false or true (default)</li>
 *   <li><b>ivy.selenide.reportfolder: </b>e.g target/senenide/reports/&lt;testclass&gt;/&lt;testmethod&gt; (default)</li>
 * </ul>
 * 
 * or annotation parameters:
 * <b>@IvySelenide(browser = "firefox", headless = true, reportFolder = "target/senenide/reports")</b>
 * </p>
 * 
 * @see WebDriverRunner#getWebDriver() use 'WebDriverRunner' to get the WebDriver
 * @see Selenide#open() use 'open' to browse a webpage
 * @see Selenide#$ use '$' to get an element by a select
 * @see Selenide#$$ use '$$' to get all elements found by a selector
 * @see SelenideElement#should(com.codeborne.selenide.Condition...) use '$.should()' to test element
 */
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Inherited
@ExtendWith(IvySelenideExtension.class)
public @interface IvySelenide
{
  String BROWSER_DEFAULT = "firefox";
  boolean HEADLESS_DEFAULT = true;
  String REPORT_FOLDER_DEFAULT = "target/selenide/reports";
  
  String browser() default BROWSER_DEFAULT;
  boolean headless() default HEADLESS_DEFAULT;
  String reportFolder() default REPORT_FOLDER_DEFAULT;
}