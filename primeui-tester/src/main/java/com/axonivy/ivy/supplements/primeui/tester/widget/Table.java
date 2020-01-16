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
package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

public class Table
{
  private final String tableId;

  public Table(By dataTable)
  {
    tableId = $(dataTable).shouldBe(visible).attr("id");
  }

  public void firstRowContains(String expectedText)
  {
    $(By.id(tableId + "_data")).findAll("tr").first().shouldBe(visible, text(expectedText));
  }

  public void contains(String checkText)
  {
    $(By.id(tableId)).shouldBe(visible, text(checkText));
  }

  public void containsNot(String checkText)
  {
    $(By.id(tableId + "_data")).shouldNotHave(text(checkText));
  }

  public String valueAt(int row, int column) throws Exception
  {
    return $(By.id(tableId + "_data")).findAll("tr")
            .find(attribute("data-ri", String.valueOf(row))).find("td", column + 1).shouldBe(visible).getText();
  }
  
  public void searchGlobal(String search)
  {
    $(By.id(tableId + ":globalFilter")).shouldBe(visible).clear();
    $(By.id(tableId + ":globalFilter")).shouldBe(visible).sendKeys(search);
  }
}