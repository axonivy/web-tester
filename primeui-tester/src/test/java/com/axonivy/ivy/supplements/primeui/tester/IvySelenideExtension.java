package com.axonivy.ivy.supplements.primeui.tester;

import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

class IvySelenideExtension implements BeforeEachCallback, BeforeAllCallback
{
  
  @Override
  public void beforeAll(ExtensionContext context) throws Exception
  {
    Configuration.browser = browser(context);
    Configuration.headless = headless(context);
    Selenide.open();
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception
  {
    Configuration.reportsFolder = reportFolder(context);
  }
  
  private boolean headless(ExtensionContext context)
  {
    return context.getConfigurationParameter("ivy.selenide.headless")
            .map(param -> BooleanUtils.toBoolean(param))
            .orElseGet(() -> findAnnotation(context).map(a -> a.headless())
                    .orElse(IvySelenide.HEADLESS_DEFAULT));
  }
  
  private String browser(ExtensionContext context)
  {
    return context.getConfigurationParameter("ivy.selenide.browser")
            .orElseGet(() -> findAnnotation(context).map(a -> a.browser())
                    .orElse(IvySelenide.BROWSER_DEFAULT));
  }
  
  private String reportFolder(ExtensionContext context)
  {
    String methodDir = context.getTestClass().map(c -> c.getName() + "/").orElse("") + 
            context.getTestMethod().map(m -> m.getName()).orElse("");
    String reportDir = context.getConfigurationParameter("ivy.selenide.reportfolder")
            .orElseGet(() -> findAnnotation(context).map(a -> a.reportFolder())
                    .orElse(IvySelenide.REPORT_FOLDER_DEFAULT));
    return StringUtils.appendIfMissing(reportDir, "/") + methodDir;
  }
  
  private Optional<IvySelenide> findAnnotation(ExtensionContext context)
  {
    return context.getTestClass().map(c -> c.getAnnotation(IvySelenide.class));
  }

}
