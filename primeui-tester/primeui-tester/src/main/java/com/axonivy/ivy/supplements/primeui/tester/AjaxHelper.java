package com.axonivy.ivy.supplements.primeui.tester;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AjaxHelper
{
  private WebDriver driver;

  public AjaxHelper(WebDriver driver)
  {
    this.driver = driver;
  }

  public <T> T await(ExpectedCondition<T> condition)
  {
    return waitAtMost(10, TimeUnit.SECONDS, condition);
  }

  public <T> T waitAtMost(long waitingTime, TimeUnit timeUnit, ExpectedCondition<T> condition)
  {
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    try
    {
      return new WebDriverWait(driver, timeUnit.toSeconds(waitingTime)).until(condition);
    }
    catch (TimeoutException ex)
    {
      System.out.println("Timed out while " + condition + " in source code: " + driver.getPageSource());
      throw ex;
    }
    finally
    {
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
  }
}