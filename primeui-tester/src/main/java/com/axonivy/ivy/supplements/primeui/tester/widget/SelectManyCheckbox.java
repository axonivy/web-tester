package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;

public class SelectManyCheckbox
{
  private String manyCheckboxId;
  
  public SelectManyCheckbox(By manyCheckbox)
  {
    manyCheckboxId = $(manyCheckbox).shouldBe(visible).attr("id");
  }
  
  public List<String> getSelectedCheckboxes()
  {
    return $(By.id(manyCheckboxId)).shouldBe(visible).findAll(".ui-chkbox-box").filter(cssClass("ui-state-active"))
            .stream().map(e -> e.parent().parent().find("label").getText())
            .collect(Collectors.toList());
  }
  
  public boolean isManyCheckboxDisabled()
  {
    return $(By.id(manyCheckboxId)).shouldBe(visible).findAll(".ui-chkbox-box").stream()
            .anyMatch(e -> e.has(cssClass("ui-state-disabled")));
  }
  
  public void setCheckboxes(List<String> values)
  {
    for (String value : values)
    {
      $(By.id(manyCheckboxId)).findAll("label").find(Condition.exactText(value)).parent().find(".ui-chkbox-box")
              .shouldBe(visible).click();
      $(By.id(manyCheckboxId)).findAll("label").find(Condition.exactText(value)).parent().find(".ui-chkbox-box")
              .shouldHave(cssClass("ui-state-active"));
    }
  }
  
  public void clear()
  {
    if (isManyCheckboxDisabled())
    {
      return;
    }
    setCheckboxes(getSelectedCheckboxes());
  }
  
}