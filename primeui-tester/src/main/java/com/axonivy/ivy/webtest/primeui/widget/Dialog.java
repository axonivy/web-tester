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

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

@Deprecated
public class Dialog {
  private final String dialogId;

  public Dialog(By dialogLocator) {
    dialogId = $(dialogLocator).shouldBe(exist).attr("id");
  }

  public void waitForVisibility(Boolean should) {
    if (should) {
      waitVisible();
    } else {
      waitHidden();
    }
  }

  public void waitVisible() {
    $(By.id(dialogId)).shouldBe(visible);
  }

  public void waitHidden() {
    $(By.id(dialogId)).shouldNotBe(visible);
  }

  public void close() {
    $(By.id(dialogId)).find(".ui-dialog-titlebar-close").shouldBe(visible).click();
    waitHidden();
  }

  @Deprecated
  public void waitToBeClosedOrError() {
    waitHidden();
  }
}
