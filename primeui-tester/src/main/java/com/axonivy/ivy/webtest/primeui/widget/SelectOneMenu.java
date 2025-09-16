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

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.match;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class SelectOneMenu {
  private final String oneMenuId;

  public SelectOneMenu(By locator) {
    oneMenuId = $(locator).shouldBe(visible).attr("id");
  }

  /**
   * Select menu entry by item value
   * @param value value
   * @return SelectOneMenu
   */
  public SelectOneMenu selectItemByValue(String value) {
    if (value == null) {
      throw new IllegalArgumentException("value must not be null!");
    }
    var label = $(By.id(oneMenuId)).find("select option[value='" + value + "']").getAttribute("textContent");
    return selectItemByLabel(label);
  }

  /**
   * Select menu entry by item label
   * @param label label
   * @return SelectOneMenu
   */
  public SelectOneMenu selectItemByLabel(String label) {
    if (label == null) {
      throw new IllegalArgumentException("Label must not be null!");
    }
    if (!label.equals(getSelectedItem())) {
      selectItem(label);
    }
    return this;
  }

  /**
   * Selected menu entry should match the given condition
   * @param condition condition
   * @return SelectOneMenu
   */
  public SelectOneMenu selectedItemShould(Condition condition) {
    selectLabel().should(condition);
    return this;
  }

  /**
   * Get selected item text
   * @return text
   */
  public String getSelectedItem() {
    if ("input".equals(selectLabel().getTagName())) {
      return selectLabel().shouldBe(visible).getValue();
    }
    return selectLabel().shouldBe(visible).getText();
  }

  private void selectItem(final String label) {
    $(By.id(oneMenuId)).find("span.ui-icon.ui-icon-triangle-1-s").shouldBe(visible).click();
    $(By.id(oneMenuId + "_panel")).should(match("menupanel should not animate",
            el -> !el.getAttribute("style").contains("opacity")));
    $(By.id(oneMenuId + "_items")).shouldBe(visible);
    $(By.id(oneMenuId + "_items")).findAll("li").first().hover();
    $(By.id(oneMenuId + "_items")).findAll("li").find(exactText(label)).shouldBe(visible, enabled).click();
    $(By.id(oneMenuId + "_items")).shouldNotBe(visible);
    if ("input".equals(selectLabel().getTagName())) {
      selectLabel().shouldBe(exactValue(label));
    } else {
      selectLabel().shouldBe(exactText(label));
    }
  }

  private SelenideElement selectLabel() {
    return $(By.id(oneMenuId)).find(".ui-selectonemenu-label");
  }
}
