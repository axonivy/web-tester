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
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class SelectManyCheckbox
{
  private final String manyCheckboxId;
  
  public SelectManyCheckbox(By manyCheckbox)
  {
    manyCheckboxId = $(manyCheckbox).shouldBe(visible).attr("id");
  }
  
  public List<String> getSelectedCheckboxes()
  {
    return $(By.id(manyCheckboxId)).shouldBe(visible).findAll(".ui-chkbox-box").filter(cssClass("ui-state-active"))
            .stream().map(e -> e.parent().parent().find("label").getText())
            .collect(Collectors.toList());
  }
  
  public boolean isManyCheckboxDisabled()
  {
    return $(By.id(manyCheckboxId)).shouldBe(visible).findAll(".ui-chkbox-box").stream()
            .anyMatch(e -> e.has(cssClass("ui-state-disabled")));
  }
  
  public SelectManyCheckbox setCheckboxes(List<String> values)
  {
    for (String value : values)
    {
      boolean active = getCheckboxForLabel(value).has(cssClass("ui-state-active"));
      getCheckboxForLabel(value).click();
      if (active)
      {
        getCheckboxForLabel(value).shouldNotHave(cssClass("ui-state-active"));
      }
      else
      {
        getCheckboxForLabel(value).shouldHave(cssClass("ui-state-active"));
      }
    }
    return this;
  }

  public SelectManyCheckbox clear()
  {
    if (!isManyCheckboxDisabled())
    {
      setCheckboxes(getSelectedCheckboxes());
    }
    return this;
  }
  
  private SelenideElement getCheckboxForLabel(String value)
  {
    return $(By.id(manyCheckboxId)).findAll("label").find(exactText(value)).parent().find(".ui-chkbox-box")
            .shouldBe(visible);
  }

}