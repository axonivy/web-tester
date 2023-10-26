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

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

public class Table {
  private final String tableId;

  public Table(By dataTable) {
    tableId = $(dataTable).shouldBe(visible).attr("id");
  }

  /**
   * Get row of the table by index
   * @param index index
   * @return SelenideElement
   */
  public SelenideElement row(int index) {
    return $(By.id(tableId + "_data")).find("tr", index);
  }

  /**
   * Get column of the table by index
   * @param index index
   * @return collection
   */
  public ElementsCollection column(int index) {
    return $(By.id(tableId + "_data")).findAll(By.xpath("tr/td[" + (index + 1) + "]"));
  }

  @Deprecated
  public void firstRowContains(String expectedText) {
    row(1).shouldBe(visible, text(expectedText));
  }

  /**
   * Check if the table overall contains the given text
   * @param checkText checkText
   * @return table
   */
  public Table contains(String checkText) {
    $(By.id(tableId)).shouldBe(visible, text(checkText));
    return this;
  }

  /**
   * Check if the table overall does not contain the given text
   * @param checkText text which should not be contained
   * @return table
   */
  public Table containsNot(String checkText) {
    $(By.id(tableId + "_data")).shouldNotHave(text(checkText));
    return this;
  }

  /**
   * @deprecated use {@link #valueAtShouldBe(int, int, WebElementCondition)} instead.
   * @param row row
   * @param column column
   * @param condition condition
   * @return table
   */
  @Deprecated
  public Table valueAtShoudBe(int row, int column, WebElementCondition condition) {
    return valueAtShouldBe(row, column, condition);
  }

  /**
   * Check if the entry at the given row and column matches the given condition
   * @param row row
   * @param column column
   * @param condition condition
   * @return table
   */
  public Table valueAtShouldBe(int row, int column, WebElementCondition condition) {
    row(row).find("td", column).shouldBe(condition);
    return this;
  }

  /**
   * Get the value of the entry at the given row and column
   * @param row row
   * @param column column
   * @return value
   */
  public String valueAt(int row, int column) {
    return row(row).find("td", column).shouldBe(visible).getText();
  }

  /**
   * Search in the globalFilter input label
   * @param search search text
   * @return table
   */
  public Table searchGlobal(String search) {
    fillInput(By.id(tableId + ":globalFilter"), search);
    return this;
  }

  /**
   * Search in a given column
   * @param column column
   * @param search search text
   * @return table
   */
  public Table searchColumn(int column, String search) {
    var columnId = $(By.id(tableId)).find(".ui-sortable-column", column).shouldBe(visible).attr("id");
    fillInput(By.id(columnId + ":filter"), search);
    return this;
  }

  private void fillInput(By id, String search) {
    $(id).shouldBe(visible).clear();
    $(id).shouldBe(visible).sendKeys(search);
  }
}