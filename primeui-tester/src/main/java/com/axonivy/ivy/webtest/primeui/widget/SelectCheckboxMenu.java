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

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

public class SelectCheckboxMenu
{
  private final String checkBoxMenuId;

  public SelectCheckboxMenu(By locator)
  {
    checkBoxMenuId = $(locator).shouldBe(visible).attr("id");
  }

  public void selectAllItems()
  {
    openCheckboxPanel();
    $(By.id(checkBoxMenuId + "_panel")).find(".ui-widget-header .ui-chkbox-box").shouldBe(visible).click();
    $(By.id(checkBoxMenuId + "_panel")).find(".ui-widget-header .ui-chkbox-box").shouldHave(cssClass("ui-state-active"));
    closeCheckboxPanel();
  }

  public void selectItemByValue(String labelValue)
  {
    selectItemsByValue(labelValue);
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
    $(By.id(checkBoxMenuId + "_panel")).findAll(".ui-selectcheckboxmenu-items li")
            .find(text(labelValue)).find(".ui-chkbox-box").shouldBe(visible).click();
    $(By.id(checkBoxMenuId + "_panel")).findAll(".ui-selectcheckboxmenu-items li")
            .find(text(labelValue)).find(".ui-chkbox-box").shouldHave(cssClass("ui-state-active"));
  }

  private void openCheckboxPanel()
  {
    $(By.id(checkBoxMenuId)).shouldBe(visible).find(".ui-icon-triangle-1-s").click();
  }

  private void closeCheckboxPanel()
  {
    String panelId = checkBoxMenuId + "_panel";
    $(By.id(panelId)).shouldBe(visible).find(".ui-selectcheckboxmenu-close").click();
    $(By.id(panelId)).shouldNotBe(visible);
  }
}
