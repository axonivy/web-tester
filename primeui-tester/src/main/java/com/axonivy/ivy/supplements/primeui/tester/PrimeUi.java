/*
 * Copyright (C) 2016 AXON IVY AG
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
package com.axonivy.ivy.supplements.primeui.tester;

import java.util.function.Function;

import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * An API using a {@link org.openqa.selenium.WebDriver WebDriver} to interact
 * with Primefaces {@link org.openqa.selenium.WebElement WebElements}.
 * 
 * @author Denis Huelin
 * @since 6.0.0
 */
public class PrimeUi
{
  private AjaxHelper ajax;
  private WebDriver webDriver;

  public PrimeUi(WebDriver driver)
  {
    this.webDriver = driver;
    this.ajax = new AjaxHelper(driver);
  }

  public SelectOneMenu selectOne(By locator)
  {
    return new SelectOneMenu(locator);
  }

  public SelectCheckboxMenu selectCheckboxMenu(By locator)
  {
    return new SelectCheckboxMenu(locator);
  }

  public SelectBooleanCheckbox selectBooleanCheckbox(By checks)
  {
    return new SelectBooleanCheckbox(checks);
  }

  public SelectOneRadio selectOneRadio(By oneRadio)
  {
    return new SelectOneRadio(oneRadio);
  }

  public Table table(By dataTable)
  {
    return new Table(dataTable);
  }

  public Dialog dialog(By dialog)
  {
    return new Dialog(dialog);
  }

  public Accordion accordion(By locator)
  {
    return new Accordion(locator);
  }

  public class SelectOneMenu
  {
    private final String oneMenuId;
    private final By locatorLabel;

    public SelectOneMenu(By locator)
    {
      oneMenuId = webDriver.findElement(locator).getAttribute("id");
      locatorLabel = By.id(oneMenuId + "_label");
    }

    public void selectItemByLabel(String label)
    {
      if (label == null)
      {
        throw new IllegalArgumentException("Label must not be null!");
      }
      if (label.equals(getFocusSelection().getAttribute("aria-activedescendant")))
      {
        return;
      }
      expandSelectableItems();
      selectInternal(label);
      awaitItemsCollapsed(true);
      waitForLabel(label);
    }

    private void selectInternal(final String label)
    {
      final String startValue = getFocusSelection().getAttribute("aria-activedescendant").toString();

      WebElement item = awaitCondition(ExpectedConditions.elementToBeClickable(webDriver.findElement(
              By.xpath("//div[@id='" + oneMenuId + "_panel']/div/ul/li[@data-label='" + label + "'][text()='"
                      + label + "']"))));
      item.click();

      awaitCondition(driver -> {
        try
        {
          if (ObjectUtils.notEqual(getFocusSelection(), startValue))
          {
            return true;
          }
          return null;
        }
        catch (StaleElementReferenceException ex)
        {
          return null;
        }
      });
    }

    private WebElement getFocusSelection()
    {
      return webDriver.findElement(By.id(oneMenuId + "_focus"));
    }

    private void awaitItemsCollapsed(boolean collapsed)
    {
      awaitCondition(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='" + oneMenuId
              + "_focus'][@aria-expanded= '" + !collapsed + "']")));
    }

    private void expandSelectableItems()
    {
      awaitCondition(driver -> {
        try
        {
          return webDriver.findElement(By.id(oneMenuId))
                  .findElement(By.cssSelector("span.ui-icon.ui-icon-triangle-1-s"));
        }
        catch (StaleElementReferenceException ex)
        {
          return null;
        }
      }).click();
      awaitItemsCollapsed(false);
    }

    public void waitForLabel(String selectLabel)
    {
      awaitCondition(ExpectedConditions.textToBePresentInElementLocated(locatorLabel, selectLabel));
    }

    public String getSelectedItem()
    {
      return webDriver.findElement(locatorLabel).getText();
    }
  }

  public class SelectCheckboxMenu
  {
    private String checkBoxMenuId;
    private final String checkAll = "U4VLuW0S0ncz2fbqQ7Kz";

    public SelectCheckboxMenu(By locator)
    {
      checkBoxMenuId = webDriver.findElement(locator).getAttribute("id");
    }

    public void selectAllItems()
    {
      openCheckboxPanel();
      webDriver.findElement(By.xpath("//*[@id='" + checkBoxMenuId + "_panel']/div[1]/div/div[2]")).click();

      waitForChosen(checkAll);

      closeCheckboxPanel();
    }

    public void selectItemByValue(String labelValue)
    {
      openCheckboxPanel();
      webDriver.findElement(By.xpath(
              "//*[@id='" + checkBoxMenuId + "_panel']/div[2]/ul/li/label[text()='" + labelValue + "']"))
              .click();

      waitForChosen(labelValue);
      closeCheckboxPanel();
    }

    private void waitForChosen(String value)
    {
      if (value.equalsIgnoreCase(checkAll))
      {
        waitForChosenInternal("");
      }
      else
      {
        waitForChosenInternal("label[.='" + value + "']/../");
      }
    }

    private void waitForChosenInternal(final String value)
    {
      await(driver -> driver
              .findElement(By.xpath("//*[@id='" + checkBoxMenuId + "_panel']/div[2]/ul/li/" +
                      value + "div/div[2]"))
              .getAttribute("class").contains("state-active"));
    }

    private void openCheckboxPanel()
    {
      webDriver.findElement(By.id(checkBoxMenuId)).findElement(By.className("ui-icon-triangle-1-s")).click();
    }

    private void closeCheckboxPanel()
    {
      String panelId = checkBoxMenuId + "_panel";
      WebElement panel = ajax.await(ExpectedConditions.visibilityOfElementLocated(By.id(panelId)));
      panel.findElement(By.className("ui-selectcheckboxmenu-close")).click();
    }
  }

  public class SelectBooleanCheckbox
  {
    private String booleanCheckboxId;

    public SelectBooleanCheckbox(By booleanCheckbox)
    {
      this.booleanCheckboxId = webDriver.findElement(booleanCheckbox).getAttribute("id");
    }

    public void setChecked()
    {
      if (isChecked())
      {
        return;
      }
      else
      {
        webDriver.findElement(By.xpath("//*[@id='" + booleanCheckboxId + "']/div[2]/span")).click();
        waitIsChecked();
      }
    }

    public void waitIsChecked()
    {
      await(driver -> isCheckedInternal(driver));
    }

    public boolean isChecked()
    {
      return isCheckedInternal(webDriver);
    }

    private boolean isCheckedInternal(WebDriver driver)
    {
      return driver
              .findElement(By.xpath("//*[@id='" + booleanCheckboxId + "']/div[2]"))
              .getAttribute("class")
              .contains("ui-state-active");
    }
  }

  public class SelectOneRadio
  {
    private String oneRadioId;

    public SelectOneRadio(By oneRadio)
    {
      oneRadioId = webDriver.findElement(oneRadio).getAttribute("id");
    }

    public void selectItemById(final String id)
    {
      webDriver.findElement(By.id(oneRadioId))
              .findElement(getRadioLocator("id", id))
              .click();

      await(driver -> driver.findElement(By.id(oneRadioId)).findElement(getRadioLocator("id", id))
              .getAttribute("class").contains("state-active"));
    }

    public void selectItemByValue(final String value)
    {
      WebElement item = webDriver.findElement(By.id(oneRadioId))
              .findElement(getRadioLocator("value", value));
      item.click();

      await(driver -> driver.findElement(By.id(oneRadioId))
              .findElement(getRadioLocator("value", value))
              .getAttribute("class").contains("ui-state-active"));
    }

    private By getRadioLocator(String attribute, String value)
    {
      return By.xpath("//input[@" + attribute + "='" + value + "']/../../div[2]");
    }

    public String getSelected()
    {
      return webDriver.findElement(By.id(oneRadioId))
              .findElement(By.xpath("//div[contains(@class, 'ui-state-active')]/../div[1]/input"))
              .getAttribute("value");
    }
  }

  public class Table
  {
    private String tableId;

    public Table(By dataTable)
    {
      tableId = webDriver.findElement(dataTable).getAttribute("id");
    }

    public void firstRowContains(String expectedText)
    {
      awaitCondition(ExpectedConditions.textToBePresentInElementLocated(
              By.xpath("//*[@id='" + tableId + "_data']/tr[1]"), expectedText));
    }

    public void contains(String checkText)
    {
      awaitCondition(ExpectedConditions.textToBePresentInElementLocated(
              By.id(tableId), checkText));
    }

    public void containsNot(String checkText)
    {
      awaitCondition(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(
              By.id(tableId + "_data"), checkText)));
    }

    public void select(String cellValue)
    {
      webDriver.findElement(By.xpath("//*[@id='" + tableId + "_data']/tr/td[text()='" + cellValue + "']"))
              .click();

      await(driver -> driver
              .findElement(By.xpath("//*[@id='" + tableId + "_data']/tr/td[text()='" + cellValue + "']/.."))
              .getAttribute("aria-selected").contains("true"));
    }

    public String valueAt(int row, int column) throws Exception
    {
      column += 1;
      String cellValue = webDriver.findElement(
              By.xpath("//*[@id='" + tableId + "_data']/tr[@data-ri='" + row + "']/td[" + column + "]"))
              .getText();
      return cellValue;
    }
  }

  public class Dialog
  {
    private String dialogId;

    public Dialog(final By dialogLocator)
    {
      dialogId = await(driver -> driver.findElement(dialogLocator).getAttribute("id"));
    }

    public void waitForVisibility(Boolean visible)
    {
      await(driver -> {
    	  WebElement dialog = driver.findElement(By.id(dialogId));
    	  String hidden = dialog.getAttribute("aria-hidden");
		return !visible.toString().equals(hidden);
      });
    }
    
    public void waitVisible()
    {
    	waitForVisibility(true);
    }
    
    public void waitHidden()
    {
    	waitForVisibility(false);
    }

    public void waitToBeClosedOrError()
    {
      awaitCondition(new ExpectedCondition<Boolean>()
        {
          @Override
          public Boolean apply(WebDriver driver)
          {
            WebElement dialog = driver.findElement(By.id(dialogId));
            if (hasErrors(dialog) || isClosed(dialog))
            {
              return true;
            }
            return null;
          }

        });
    }
    
    private boolean isClosed(WebElement dialog)
    {
      try
      {
        return dialog.getAttribute("aria-hidden").equals(Boolean.TRUE.toString());
      }
      catch (Exception ex)
      {
        return false;
      }
    }
    
    private boolean hasErrors(WebElement dialog)
    {
      try
      {
        return dialog.findElement(By.className("ui-state-error")) != null;
      }
      catch (Exception ex)
      {
        return false;
      }
    }
  }

  public class Accordion
  {
    private String accordionId;

    public Accordion(final By accordionLocator)
    {
      accordionId = await(driver -> driver.findElement(accordionLocator).getAttribute("id"));
    }

    public void toggleTab(String tabName)
    {
      By tabLocator = By.xpath("//*[@id='" + accordionId + "']/*[contains(., '" + tabName + "')]");
      String expansionAttribute = "aria-expanded";
      String previousState = webDriver.findElement(tabLocator).getAttribute(expansionAttribute);
      webDriver.findElement(tabLocator).click();

      await(driver -> !driver.findElement(tabLocator).getAttribute(expansionAttribute)
              .contains(previousState));
    }

    public boolean isTabOpen(String tabName)
    {
      return webDriver
              .findElement(By.xpath("//*[@id='" + accordionId + "']/*[contains(., '" + tabName + "')]"))
              .getAttribute("aria-expanded").contains("true");
    }
  }

  protected <T> T await(Function<WebDriver, T> condition)
  {
    return awaitCondition(new ExpectedCondition<T>()
      {
        @Override
        public T apply(WebDriver driver)
        {
          try
          {
            return condition.apply(driver);
          }
          catch (StaleElementReferenceException ex)
          {
            return null;
          }
        }
      });
  }

  protected <T> T awaitCondition(ExpectedCondition<T> condition)
  {
    return ajax.await(condition);
  }
}