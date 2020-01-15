package com.axonivy.ivy.supplements.primeui.tester;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

/**
 * IvySelenideExtension is an JUnit5 extension which can be used for Selenide test setup.
 * 
 * <p>It can be configured over the following system properties:
 * <ul>
 *   <li><b>ivy.selenide.browser: </b>e.g chrome, firefox (default)</li>
 *   <li><b>ivy.selenide.headless: </b>false or true (default)</li>
 *   <li><b>ivy.selenide.reportfolder: </b>e.g target/senenide/reports/&lt;testclass&gt;/&lt;testmethod&gt; (default)</li>
 * </ul>
 * </p>
 * 
 * @see WebDriverRunner#getWebDriver() use 'WebDriverRunner' to get the WebDriver
 * @see Selenide#open() use 'open' to browse a webpage
 * @see Selenide#$ use '$' to get an element by a select
 * @see Selenide#$$ use '$$' to get all elements found by a selector
 * @see SelenideElement#should(com.codeborne.selenide.Condition...) use '$.should()' to test element
 */
public class IvySelenideExtension implements BeforeEachCallback
{

  @Override
  public void beforeEach(ExtensionContext context) throws Exception
  {
    String reportDir = context.getTestClass().map(c -> c.getName() + "/").orElse("");
    reportDir += context.getTestMethod().map(m -> m.getName()).orElse("");
    Configuration.browser = context.getConfigurationParameter("ivy.selenide.browser").orElse("firefox");
    Configuration.headless = BooleanUtils.toBoolean(context.getConfigurationParameter("ivy.selenide.headless")
            .orElse("true"));
    Configuration.reportsFolder = context.getConfigurationParameter("ivy.selenide.reportfolder")
            .orElse("target/senenide/reports/" + reportDir);
    Selenide.open();
  }

}
