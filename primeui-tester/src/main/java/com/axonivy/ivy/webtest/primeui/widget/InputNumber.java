package com.axonivy.ivy.webtest.primeui.widget;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.codeborne.selenide.WebElementCondition;

public class InputNumber {

  private final String inputNumberId;

  public InputNumber(By inputNumber) {
    this.inputNumberId = $(inputNumber).shouldBe(visible).attr("id") + "_input";
  }

  /**
   * Set value of number input
   * @param value value to set
   * @return input number
   */
  public InputNumber setValue(String value) {
    clear();
    $(By.id(inputNumberId)).sendKeys(value);
    $(By.id(inputNumberId)).sendKeys(Keys.TAB);
    return this;
  }

  /**
   * Check if the number input match the given condition
   * @param condition condition which will be asserted
   * @return input number
   */
  public InputNumber should(WebElementCondition condition) {
    $(By.id(inputNumberId)).should(condition);
    return this;
  }

  /**
   * Clear the number input
   * @return input number
   */
  public InputNumber clear() {
    var oldValue = getValue();
    while (oldValue != null && !oldValue.isBlank()) {
      $(By.id(inputNumberId)).sendKeys(Keys.BACK_SPACE);
      var value = getValue();
      if (Objects.equals(oldValue, value)) {
        throw new RuntimeException("Couldn't clear number input field, maybe minValue is set");
      }
      oldValue = value;
    }
    return this;
  }

  private String getValue() {
    return $(By.id(inputNumberId)).shouldBe(visible).getValue();
  }
}
