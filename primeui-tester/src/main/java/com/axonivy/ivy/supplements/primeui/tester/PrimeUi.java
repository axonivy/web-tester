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

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
  
  public SelectManyCheckbox selectManyCheckbox(By manyCheckbox)
  {
    return new SelectManyCheckbox(manyCheckbox);
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

      // With PrimeFaces 7 it seems that a list gets blended in and the a click on an element of the list
      // only has an effect if opacity=1, meaning there is no opacity value in the element.
      awaitCondition(ExpectedConditions.numberOfElementsToBe(
              By.xpath("//div[@id='" + oneMenuId + "_panel' and not(contains(@style,'opacity'))]"), 1));
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
    
    public void removeChecked()
    {
      if (!isChecked())
      {
        return;
      }
      webDriver.findElement(By.xpath("//*[@id='" + booleanCheckboxId + "']/div[2]/span")).click();
      waitIsUnChecked();
    }

    public void waitIsChecked()
    {
      await(driver -> isCheckedInternal(driver));
    }
    
    public void waitIsUnChecked()
    {
      await(driver -> !isCheckedInternal(driver));
    }

    public boolean isChecked()
    {
      return isCheckedInternal(webDriver);
    }
    
    public boolean isDisabled()
    {
      return isDisabledInternal(webDriver);
    }

    private boolean isCheckedInternal(WebDriver driver)
    {
      return driver
              .findElement(By.xpath("//*[@id='" + booleanCheckboxId + "']/div[2]"))
              .getAttribute("class")
              .contains("ui-state-active");
    }
    
    private boolean isDisabledInternal(WebDriver driver)
    {
      return driver.findElement(By.xpath("//*[@id='" + booleanCheckboxId + "']/div[2]"))
              .getAttribute("class")
              .contains("ui-state-disabled");
    }
  }
  
  public class SelectManyCheckbox
  {
    private String manyCheckboxId;
    
    public SelectManyCheckbox(By manyCheckbox)
    {
      manyCheckboxId = webDriver.findElement(manyCheckbox).getAttribute("id");
    }
    
    public List<String> getSelectedCheckboxes()
    {
      return webDriver.findElements(By.xpath("//*[@id='" + manyCheckboxId + "']/div/div/div/div[2]")).stream()
              .filter(e -> e.getAttribute("class").contains("ui-state-active"))
              .map(e -> e.findElement(By.xpath("../../label")).getText())
              .collect(Collectors.toList());
    }
    
    public boolean isManyCheckboxDisabled()
    {
      return webDriver.findElements(By.xpath("//*[@id='" + manyCheckboxId + "']/div/div/div/div[2]")).stream()
              .anyMatch(e -> e.getAttribute("class").contains("ui-state-disabled"));
    }
    
    public void setCheckboxes(List<String> values)
    {
      values.stream().forEach(v -> webDriver.findElement(By.xpath("//*[@id='" + manyCheckboxId + "']/div/div/label[text()='" + v + "']/../div/div[2]")).click());
    }
    
    public void clear()
    {
      if (isManyCheckboxDisabled())
      {
        return;
      }
      setCheckboxes(getSelectedCheckboxes());
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
    
    public void selectItemByCss(final String selector)
    {
      WebElement radio = webDriver.findElement(By.id(oneRadioId))
              .findElement(By.cssSelector(selector));
      new WebDriverWait(webDriver, 10).until(ExpectedConditions.elementToBeClickable(radio));
      radio.click();
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
            if (hasErrors(driver) || isClosed(driver))
            {
              return true;
            }
            return null;
          }

        });
    }

    private boolean isClosed(WebDriver driver)
    {
      try
      {
        return driver.findElement(By.id(dialogId)).getAttribute("aria-hidden")
                .equals(Boolean.TRUE.toString());
      }
      catch (Exception ex)
      {
        return false;
      }
    }

    private boolean hasErrors(WebDriver driver)
    {
      try
      {
        return driver.findElement(By.id(dialogId)).findElement(By.className("ui-state-error")) != null;
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
      String xPath = "//*[@id='" + accordionId + "']/*[contains(., '" + tabName + "')][@role='tab']";
      By tabLocator = By.xpath(xPath);
      String expansionAttribute = "aria-expanded";
      WebElement item = awaitCondition(ExpectedConditions.elementToBeClickable(webDriver.findElement(
              tabLocator)));
      String previousState = item.getAttribute(expansionAttribute);
      item.click();

      By nextSibling = By.xpath(xPath + "/following-sibling::*[contains(@role,'tabpanel')]");
      await(driver ->
              !driver.findElements(nextSibling).get(0).getAttribute("style").matches(".*overflow:.?hidden;.*height:.*"));
      await(driver -> 
              !driver.findElement(tabLocator).getAttribute(expansionAttribute).contains(previousState));
    }

    public boolean isTabOpen(String tabName)
    {
      return webDriver
              .findElement(By.xpath("//*[@id='" + accordionId + "']/*[contains(., '" + tabName + "')]"))
              .getAttribute("aria-expanded").contains("true");
    }

    public void openTab(String tabName)
    {
      if (isTabOpen(tabName))
      {
        return;
      }
      else
      {
        toggleTab(tabName);
      }
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