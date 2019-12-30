package com.axonivy.ivy.supplements.primeui.tester.widget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SelectCheckboxMenu
{
  private final String checkBoxMenuId;

  public SelectCheckboxMenu(By locator)
  {
    checkBoxMenuId = webDriver.findElement(locator).getAttribute("id");
  }

  public void selectAllItems()
  {
    openCheckboxPanel();
    webDriver.findElement(By.xpath("//*[@id='" + checkBoxMenuId + "_panel']/div[1]/div/div[2]")).click();
    waitForChosenInternal("");
    closeCheckboxPanel();
  }

  public void selectItemByValue(String labelValue)
  {
    openCheckboxPanel();
    selectItemInternal(labelValue);
    closeCheckboxPanel();
  }

  public void selectItemsByValue(String... labelValues)
  {
    openCheckboxPanel();
    for (String label : labelValues)
    {
      selectItemInternal(label);
    }
    closeCheckboxPanel();
  }

  private void selectItemInternal(String labelValue)
  {
    webDriver.findElement(By.xpath(
            "//*[@id='" + checkBoxMenuId + "_panel']/div[2]/ul/li/label[text()='" + labelValue + "']/../div/div[2]")
            ).click();
    waitForChosenInternal("label[.='" + labelValue + "']/../");
  }

  private void waitForChosenInternal(final String value)
  {
    await(driver -> driver
            .findElement(By.xpath("//*[@id='" + checkBoxMenuId + "_panel']/div[2]/ul/li/" +
                    value + "div/div[2]"))
            .getAttribute("class").contains("state-active"));
  }

  protected void openCheckboxPanel()
  {
    webDriver.findElement(By.id(checkBoxMenuId)).findElement(By.className("ui-icon-triangle-1-s")).click();
  }

  protected void closeCheckboxPanel()
  {
    String panelId = checkBoxMenuId + "_panel";
    WebElement panel = ajax.await(ExpectedConditions.visibilityOfElementLocated(By.id(panelId)));
    panel.findElement(By.className("ui-selectcheckboxmenu-close")).click();
    ajax.await(ExpectedConditions.invisibilityOfElementLocated(By.id(panelId)));
  }
}
