package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

public class SelectCheckboxMenu
{
  private final String checkBoxMenuId;

  public SelectCheckboxMenu(By locator)
  {
    checkBoxMenuId = $(locator).shouldBe(visible).attr("id");
  }

  public void selectAllItems()
  {
    openCheckboxPanel();
    $(By.id(checkBoxMenuId + "_panel")).find(".ui-widget-header .ui-chkbox-box").shouldBe(visible).click();
    $(By.id(checkBoxMenuId + "_panel")).find(".ui-widget-header .ui-chkbox-box").shouldHave(cssClass("ui-state-active"));
    closeCheckboxPanel();
  }

  public void selectItemByValue(String labelValue)
  {
    selectItemsByValue(labelValue);
  }

  public void selectItemsByValue(String... labelValues)
  {
    openCheckboxPanel();
    for (String label : labelValues)
    {
      selectItemInternal(label);
    }
    closeCheckboxPanel();
  }

  private void selectItemInternal(String labelValue)
  {
    $(By.id(checkBoxMenuId + "_panel")).findAll(".ui-selectcheckboxmenu-items li")
            .find(text(labelValue)).find(".ui-chkbox-box").shouldBe(visible).click();
    $(By.id(checkBoxMenuId + "_panel")).findAll(".ui-selectcheckboxmenu-items li")
            .find(text(labelValue)).find(".ui-chkbox-box").shouldHave(cssClass("ui-state-active"));
  }

  private void openCheckboxPanel()
  {
    $(By.id(checkBoxMenuId)).shouldBe(visible).find(".ui-icon-triangle-1-s").click();
  }

  private void closeCheckboxPanel()
  {
    String panelId = checkBoxMenuId + "_panel";
    $(By.id(panelId)).shouldBe(visible).find(".ui-selectcheckboxmenu-close").click();
    $(By.id(panelId)).shouldNotBe(visible);
  }
}
