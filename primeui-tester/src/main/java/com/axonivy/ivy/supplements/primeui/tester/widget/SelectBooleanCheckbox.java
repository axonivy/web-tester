package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class SelectBooleanCheckbox
{
  private String booleanCheckboxId;

  public SelectBooleanCheckbox(By booleanCheckbox)
  {
    this.booleanCheckboxId = $(booleanCheckbox).shouldBe(visible).attr("id");
  }

  public void setChecked()
  {
    if (!isChecked())
    {
      checkbox().click();
      checkbox().shouldHave(cssClass("ui-state-active"));
    }
  }
  
  public void removeChecked()
  {
    if (isChecked())
    {
      checkbox().click();
      checkbox().shouldNotHave(cssClass("ui-state-active"));
    }
  }

  public boolean isChecked()
  {
    return checkbox().has(cssClass("ui-state-active"));
  }
  
  public boolean isDisabled()
  {
    return checkbox().has(cssClass("ui-state-disabled"));
  }

  private SelenideElement checkbox()
  {
    return $(By.id(booleanCheckboxId)).find(".ui-chkbox-box").shouldBe(visible);
  }
}