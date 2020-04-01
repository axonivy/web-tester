/*
 * Copyright (C) 2020 AXON IVY AG
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
package com.axonivy.ivy.webtest.primeui.widget;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.WebDriverRunner;

public class SelectOneMenu
{
  private final String oneMenuId;
  private final By locatorLabel;

  public SelectOneMenu(By locator)
  {
    oneMenuId = $(locator).shouldBe(visible).attr("id");
    locatorLabel = By.id(oneMenuId + "_label");
  }

  public void selectItemByLabel(String label)
  {
    if (label == null)
    {
      throw new IllegalArgumentException("Label must not be null!");
    }
    if (label.equals(getSelectedItem()))
    {
      return;
    }
    
    selectItem(label);
  }
  
  public String getSelectedItem()
  {
    return $(locatorLabel).shouldBe(visible).getText();
  }

  private void selectItem(final String label)
  {
    $(By.id(oneMenuId)).find("span.ui-icon.ui-icon-triangle-1-s").shouldBe(visible).click();
    //Selenide doesn't support if a style is set or not. Primefaces 7 needs to wait for full visibility (no opacity).
    PrimeUi.ajax(WebDriverRunner.getWebDriver()).await(ExpectedConditions.numberOfElementsToBe(
            By.xpath("//div[@id='" + oneMenuId + "_panel' and not(contains(@style,'opacity'))]"), 1));
    $(By.id(oneMenuId + "_items")).shouldBe(visible);
    $(By.id(oneMenuId + "_items")).findAll("li").find(exactText(label)).shouldBe(visible, enabled).click();
    $(By.id(oneMenuId + "_items")).shouldNotBe(visible);
    $(locatorLabel).shouldBe(exactText(label));
  }

}
