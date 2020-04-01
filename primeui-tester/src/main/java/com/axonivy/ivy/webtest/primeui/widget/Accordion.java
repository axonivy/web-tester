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

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.axonivy.ivy.webtest.primeui.PrimeUi;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

public class Accordion
{
  private final String accordionId;

  public Accordion(final By accordionLocator)
  {
    accordionId = $(accordionLocator).shouldBe(visible).attr("id");
  }

  public void toggleTab(String tabName)
  {
    String previousState = accordionTab(tabName).getAttribute("aria-expanded");
    accordionTab(tabName).click();
    String tabContentId = StringUtils.removeEnd(accordionTab(tabName).getAttribute("id"), "_header");

    //Selenide doesn't support if a style is set or not. Primefaces 7 needs to wait for full visibility (no opacity).
    PrimeUi.ajax(WebDriverRunner.getWebDriver()).await(ExpectedConditions.numberOfElementsToBe(
            By.xpath("//div[@id='" + tabContentId + "' and not(contains(@style,'overflow'))]"), 1));
    $(By.id(tabContentId)).shouldBe(attribute("aria-hidden", previousState));
  }

  private SelenideElement accordionTab(String tabName)
  {
    return $(By.id(accordionId)).findAll(".ui-accordion-header").find(text(tabName)).shouldBe(visible, enabled);
  }

  public boolean isTabOpen(String tabName)
  {
    return accordionTab(tabName).getAttribute("aria-expanded").contains("true");
  }

  public void openTab(String tabName)
  {
    if (!isTabOpen(tabName))
    {
      toggleTab(tabName);
    }
  }
}