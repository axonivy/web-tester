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

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.Arrays;

import org.openqa.selenium.By;

public class SelectCheckboxMenu {
  private final String checkBoxMenuId;

  public SelectCheckboxMenu(By locator) {
    checkBoxMenuId = $(locator).shouldBe(visible).attr("id");
  }

  /**
   * Select all items
   * @return SelectCheckboxMenu
   */
  public SelectCheckboxMenu selectAllItems() {
    openCheckboxPanel();
    $(By.id(checkBoxMenuId + "_panel")).find(".ui-widget-header .ui-chkbox-box").shouldBe(visible).click();
    $(By.id(checkBoxMenuId + "_panel")).find(".ui-widget-header .ui-chkbox-box")
        .shouldHave(cssClass("ui-state-active"));
    closeCheckboxPanel();
    return this;
  }

  /**
   * @deprecated use {@link #selectItemsByValue(String...)}
   * @param label label
   * @return SelectCheckboxMenu
   */
  @Deprecated
  public SelectCheckboxMenu selectItemByValue(String label) {
    return selectItemsByValue(label);
  }

  /**
   * Select items by given labels
   * @param labels labels to select
   * @return SelectCheckboxMenu
   */
  public SelectCheckboxMenu selectItemsByValue(String... labels) {
    openCheckboxPanel();
    Arrays.stream(labels).forEach(this::selectItemInternal);
    closeCheckboxPanel();
    return this;
  }

  /**
   * Check if items of given labels are selected
   * @param labels labels
   * @return SelectCheckboxMenu
   */
  public SelectCheckboxMenu itemsShouldBeSelected(String... labels) {
    openCheckboxPanel();
    Arrays.stream(labels).forEach(this::checkThatLabelIsSelected);
    closeCheckboxPanel();
    return this;
  }

  private void selectItemInternal(String label) {
    $(By.id(checkBoxMenuId + "_panel")).findAll(".ui-selectcheckboxmenu-items li")
        .find(text(label)).find(".ui-chkbox-box").shouldBe(visible).click();
    checkThatLabelIsSelected(label);
  }

  private void checkThatLabelIsSelected(String label) {
    $(By.id(checkBoxMenuId + "_panel")).findAll(".ui-selectcheckboxmenu-items li")
        .find(text(label)).find(".ui-chkbox-box").shouldHave(cssClass("ui-state-active"));
  }

  private void openCheckboxPanel() {
    $(By.id(checkBoxMenuId)).shouldBe(visible).find(".ui-icon-triangle-1-s").click();
  }

  private void closeCheckboxPanel() {
    String panelId = checkBoxMenuId + "_panel";
    $(By.id(panelId)).shouldBe(visible).find(".ui-selectcheckboxmenu-close").click();
    $(By.id(panelId)).shouldNotBe(visible);
  }
}
