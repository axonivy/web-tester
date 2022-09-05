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
package com.axonivy.ivy.webtest.primeui;

import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.primeui.widget.Accordion;
import com.axonivy.ivy.webtest.primeui.widget.Dialog;
import com.axonivy.ivy.webtest.primeui.widget.InputNumber;
import com.axonivy.ivy.webtest.primeui.widget.SelectBooleanCheckbox;
import com.axonivy.ivy.webtest.primeui.widget.SelectCheckboxMenu;
import com.axonivy.ivy.webtest.primeui.widget.SelectManyCheckbox;
import com.axonivy.ivy.webtest.primeui.widget.SelectOneMenu;
import com.axonivy.ivy.webtest.primeui.widget.SelectOneRadio;
import com.axonivy.ivy.webtest.primeui.widget.Table;

/**
 * An API using a {@link org.openqa.selenium.WebDriver WebDriver} to interact
 * with Primefaces {@link org.openqa.selenium.WebElement WebElements}.
 */
@SuppressWarnings("deprecation")
public class PrimeUi {

  /**
   * Get a SelectOneMenu widget to test against
   * @param locator e.g By.id("")
   * @return SelectOneMenu
   */
  public static SelectOneMenu selectOne(By locator) {
    return new SelectOneMenu(locator);
  }

  /**
   * Get a SelectCheckboxMenu widget to test against
   * @param locator e.g By.id("")
   * @return SelectCheckboxMenu
   */
  public static SelectCheckboxMenu selectCheckboxMenu(By locator) {
    return new SelectCheckboxMenu(locator);
  }

  /**
   * Get a SelectBooleanCheckbox widget to test against
   * @param checks e.g By.id("")
   * @return SelectBooleanCheckbox
   */
  public static SelectBooleanCheckbox selectBooleanCheckbox(By checks) {
    return new SelectBooleanCheckbox(checks);
  }

  /**
   * Get a SelectManyCheckbox widget to test against
   * @param manyCheckbox e.g By.id("")
   * @return SelectManyCheckbox
   */
  public static SelectManyCheckbox selectManyCheckbox(By manyCheckbox) {
    return new SelectManyCheckbox(manyCheckbox);
  }

  /**
   * Get a SelectOneRadio widget to test against
   * @param oneRadio e.g By.id("")
   * @return SelectOneRadio
   */
  public static SelectOneRadio selectOneRadio(By oneRadio) {
    return new SelectOneRadio(oneRadio);
  }

  /**
   * Get a Table widget to test against
   * @param dataTable e.g By.id("")
   * @return Table
   */
  public static Table table(By dataTable) {
    return new Table(dataTable);
  }

  /**
   * Get a Dialog widget to test against
   * @param dialog e.g By.id("")
   * @deprecated
   * @return Dialog
   */
  @Deprecated
  public static Dialog dialog(By dialog) {
    return new Dialog(dialog);
  }

  /**
   * Get a Accordion widget to test against
   * @param locator e.g By.id("")
   * @return Accordion
   */
  public static Accordion accordion(By locator) {
    return new Accordion(locator);
  }

  /**
   * Get a InputNumber widget to test against
   * @param input e.g By.id("")
   * @return InputNumber
   */
  public static InputNumber inputNumber(By input) {
    return new InputNumber(input);
  }
}
