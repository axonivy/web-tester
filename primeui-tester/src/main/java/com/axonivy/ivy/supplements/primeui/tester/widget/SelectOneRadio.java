package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class SelectOneRadio
{
  private String oneRadioId;

  public SelectOneRadio(By oneRadio)
  {
    oneRadioId = $(oneRadio).shouldBe(visible).attr("id");
  }

  public void selectItemById(final String id)
  {
    $(radioById(id)).click();
    $(radioById(id)).shouldHave(cssClass("ui-state-active"));
  }

  public void selectItemByValue(final String value)
  {
    $(radioByLabel(value)).click();
    $(radioByLabel(value)).shouldHave(cssClass("ui-state-active"));
  }
  
  public String getSelected()
  {
    return $(By.id(oneRadioId)).findAll(".ui-radiobutton-box").find(Condition.cssClass("ui-state-active"))
            .parent().find("div > input").getValue();
  }
  
  private SelenideElement radioById(String id)
  {
    return $(By.id(id)).parent().parent().find("div.ui-radiobutton-box").shouldBe(visible);
  }
  
  private SelenideElement radioByLabel(String label)
  {
    return $(By.id(oneRadioId)).findAll("label").find(exactText(label)).parent()
            .find("div.ui-radiobutton-box").shouldBe(visible);
  }

}