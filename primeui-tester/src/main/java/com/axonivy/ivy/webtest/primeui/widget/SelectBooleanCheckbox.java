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
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class SelectBooleanCheckbox
{
  private final String booleanCheckboxId;

  public SelectBooleanCheckbox(By booleanCheckbox)
  {
    this.booleanCheckboxId = $(booleanCheckbox).shouldBe(visible).attr("id");
  }

  public SelectBooleanCheckbox setChecked()
  {
    if (!isChecked())
    {
      checkbox().click();
      shouldBeChecked(true);
    }
    return this;
  }
  
  public SelectBooleanCheckbox removeChecked()
  {
    if (isChecked())
    {
      checkbox().click();
      shouldBeChecked(false);
    }
    return this;
  }
  
  public SelectBooleanCheckbox shouldBeChecked(boolean checked)
  {
    return checkboxShouldHaveCssClass(checked, "ui-state-active");
  }
  
  public SelectBooleanCheckbox shouldBeDisabled(boolean disabled)
  {
    return checkboxShouldHaveCssClass(disabled, "ui-state-disabled");
  }

  private SelectBooleanCheckbox checkboxShouldHaveCssClass(boolean state, String cssClass)
  {
    if (state)
    {
      checkbox().shouldHave(cssClass(cssClass));
    }
    else
    {
      checkbox().shouldNotHave(cssClass(cssClass));
    }
    return this;
  }

  /**
   * @deprecated use {@link #shouldBeChecked(boolean)}
   */
  @Deprecated
  public boolean isChecked()
  {
    return checkbox().has(cssClass("ui-state-active"));
  }
  
  /**
   * @deprecated use {@link #shouldBeDisabled(boolean)}
   */
  @Deprecated
  public boolean isDisabled()
  {
    return checkbox().has(cssClass("ui-state-disabled"));
  }

  private SelenideElement checkbox()
  {
    return $(By.id(booleanCheckboxId)).find(".ui-chkbox-box").shouldBe(visible);
  }
}