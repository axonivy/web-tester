/*
 * Copyright (C) 2021 Axon Ivy AG
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

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class Table
{
  private final String tableId;

  public Table(By dataTable)
  {
    tableId = $(dataTable).shouldBe(visible).attr("id");
  }
  
  public SelenideElement row(int index)
  {
    return $(By.id(tableId + "_data")).find("tr", index);
  }
  
  public ElementsCollection column(int index)
  {
    return $(By.id(tableId + "_data")).findAll(By.xpath("tr/td[" + (index + 1) + "]"));
  }

  @Deprecated
  public void firstRowContains(String expectedText)
  {
    row(1).shouldBe(visible, text(expectedText));
  }

  public Table contains(String checkText)
  {
    $(By.id(tableId)).shouldBe(visible, text(checkText));
    return this;
  }

  public Table containsNot(String checkText)
  {
    $(By.id(tableId + "_data")).shouldNotHave(text(checkText));
    return this;
  }
  
  /**
   * @deprecated use {@link #valueAtShouldBe(int, int, Condition)} instead.
   */
  @Deprecated
  public Table valueAtShoudBe(int row, int column, Condition condition)
  {
    row(row).find("td", column).shouldBe(condition);
    return this;
  }
  
  public Table valueAtShouldBe(int row, int column, Condition condition)
  {
    row(row).find("td", column).shouldBe(condition);
    return this;
  }
  
  public String valueAt(int row, int column)
  {
    return row(row).find("td", column).shouldBe(visible).getText();
  }
  
  public Table searchGlobal(String search)
  {
    fillInput(By.id(tableId + ":globalFilter"), search);
    return this;
  }

  public Table searchColumn(int row, String search)
  {
    var columnId = $(By.id(tableId)).find(".ui-sortable-column", row).shouldBe(visible).attr("id");
    fillInput(By.id(columnId + ":filter"), search);
    return this;
  }
  
  private void fillInput(By id, String search)
  {
    $(id).shouldBe(visible).clear();
    $(id).shouldBe(visible).sendKeys(search);
  }
}