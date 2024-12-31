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

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.match;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class Accordion {

  private final String accordionId;

  public Accordion(final By accordionLocator) {
    accordionId = $(accordionLocator).shouldBe(visible).attr("id");
  }

  /**
   * Toggle Accordion to given tabName
   * @param tabName name of the tab
   * @return accordion
   */
  public Accordion toggleTab(String tabName) {
    String previousState = accordionTab(tabName).getAttribute("aria-expanded");
    accordionTab(tabName).click();
    String tabContentId = StringUtils.removeEnd(accordionTab(tabName).getAttribute("id"), "_header");
    $(By.id(tabContentId)).should(match("accordion should not animate",
        el -> !el.getAttribute("style").contains("overflow")));
    $(By.id(tabContentId)).shouldBe(attribute("aria-hidden", previousState));
    return this;
  }

  /**
   * Open tab of Accordion by given tabName
   * @param tabName name of the tab
   * @return accordion
   */
  public Accordion openTab(String tabName) {
    if (!isTabOpen(tabName)) {
      toggleTab(tabName);
    }
    return this;
  }

  /**
   * Check if tab of Accordion is open according to given parameter
   * @param tabName name of the tab
   * @param open if it should be open or not
   * @return accordion
   */
  public Accordion tabShouldBe(String tabName, boolean open) {
    accordionTab(tabName).shouldHave(attribute("aria-expanded", String.valueOf(open)));
    return this;
  }

  /**
   * Check if tab of Accordion is open
   * @param tabName name of the tab
   * @deprecated use {@link #tabShouldBe(String, boolean)} instead
   * @return true if its open otherwise false
   */
  @Deprecated
  public boolean isTabOpen(String tabName) {
    return accordionTab(tabName).getAttribute("aria-expanded").contains("true");
  }

  private SelenideElement accordionTab(String tabName) {
    return $(By.id(accordionId)).findAll(".ui-accordion-header").find(text(tabName)).shouldBe(visible,
        enabled);
  }
}
