package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.axonivy.ivy.supplements.primeui.tester.AjaxHelper;
import com.codeborne.selenide.WebDriverRunner;

public class SelectOneMenu
{
  private final String oneMenuId;
  private final By locatorLabel;

  public SelectOneMenu(By locator)
  {
    oneMenuId = $(locator).attr("id");
    locatorLabel = By.id(oneMenuId + "_label");
  }

  public void selectItemByLabel(String label)
  {
    if (label == null)
    {
      throw new IllegalArgumentException("Label must not be null!");
    }
    if (label.equals(getSelectedItem()))
    {
      return;
    }
    
    selectItem(label);
  }
  
  public String getSelectedItem()
  {
    return $(locatorLabel).shouldBe(visible).getText();
  }

  private void selectItem(final String label)
  {
    $(By.id(oneMenuId)).find("span.ui-icon.ui-icon-triangle-1-s").shouldBe(visible).click();
    //Selenide doesn't support if a style is set or not. Primefaces 7 needs to wait for full visibility (no opacity).
    AjaxHelper ajax = new AjaxHelper(WebDriverRunner.getWebDriver());
    ajax.await(ExpectedConditions.numberOfElementsToBe(
            By.xpath("//div[@id='" + oneMenuId + "_panel' and not(contains(@style,'opacity'))]"), 1));
    $(By.id(oneMenuId + "_items")).shouldBe(visible);
    $(By.id(oneMenuId + "_items")).findAll("li").find(exactText(label)).shouldBe(visible, enabled).click();
    $(By.id(oneMenuId + "_items")).shouldNotBe(visible);
    $(locatorLabel).shouldBe(exactText(label));
  }

}
