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
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.match;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

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
    var options = $(By.id(oneMenuId)).findAll("select option");
    for (int index = 0; index < options.size(); index++) {
      var option = options.get(index);
      if (value.equals(option.getAttribute("value"))) {
        var label = option.getAttribute("textContent");
        if (isBlankLabel(label)) {
          // PrimeFaces renders blank labels as NBSP; newer versions add a separate aria-label.
          // Select by the stable option/item order instead of round-tripping the rendered text.
          selectItemByIndex(index, value);
          return this;
        }
        return selectItemByLabel(label);
      }
    }
    throw new IllegalArgumentException("No item with value '" + value + "' found in SelectOneMenu '" + oneMenuId + "'");
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
    if (isBlankLabel(label)) {
      var options = $(By.id(oneMenuId)).findAll("select option");
      for (int index = 0; index < options.size(); index++) {
        var option = options.get(index);
        if (isBlankLabel(option.getAttribute("textContent"))) {
          selectItemByIndex(index, option.getAttribute("value"));
          return this;
        }
      }
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
  public SelectOneMenu selectedItemShould(WebElementCondition condition) {
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
    openMenu();
    $(By.id(oneMenuId + "_items")).findAll("li").find(exactText(label)).shouldBe(visible, enabled).click();
    $(By.id(oneMenuId + "_items")).shouldNotBe(visible);
    if ("input".equals(selectLabel().getTagName())) {
      selectLabel().shouldBe(exactValue(label));
    } else {
      selectLabel().shouldBe(exactText(label));
    }
  }

  private void selectItemByIndex(int index, String value) {
    openMenu();
    $(By.id(oneMenuId + "_panel")).findAll("li.ui-selectonemenu-item").get(index)
        .shouldBe(visible, enabled).click();
    $(By.id(oneMenuId + "_items")).shouldNotBe(visible);
    $(By.id(oneMenuId + "_input")).shouldBe(exactValue(value));
  }

  private void openMenu() {
    $(By.id(oneMenuId)).find("span.ui-icon.ui-icon-triangle-1-s").shouldBe(visible).click();
    // Wait for the opening transition instead of hovering the first item:
    // hovering can scroll the page and cause PrimeFaces to close the panel.
    $(By.id(oneMenuId + "_panel")).shouldBe(visible)
        .shouldNotHave(cssClass("ui-connected-overlay-enter"), cssClass("ui-connected-overlay-enter-active"))
        .should(match("menupanel should not animate", el -> !el.getAttribute("style").contains("opacity")));
    $(By.id(oneMenuId + "_items")).shouldBe(visible);
  }

  private boolean isBlankLabel(String label) {
    return label == null || label.replace("&nbsp;", "").replace("&#160;", "").replace('\u00a0', ' ').isBlank();
  }

  private SelenideElement selectLabel() {
    return $(By.id(oneMenuId)).find(".ui-selectonemenu-label");
  }
}
