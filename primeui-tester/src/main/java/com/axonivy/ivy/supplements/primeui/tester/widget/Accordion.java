package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.axonivy.ivy.supplements.primeui.tester.AjaxHelper;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

public class Accordion
{
  private String accordionId;

  public Accordion(final By accordionLocator)
  {
    accordionId = $(accordionLocator).shouldBe(visible).attr("id");
  }

  public void toggleTab(String tabName)
  {
    String previousState = accorionTab(tabName).getAttribute("aria-expanded");
    accorionTab(tabName).click();
    String tabContentId = StringUtils.removeEnd(accorionTab(tabName).getAttribute("id"), "_header");

    //Selenide doesn't support if a style is set or not. Primefaces 7 needs to wait for full visibility (no opacity).
    AjaxHelper ajax = new AjaxHelper(WebDriverRunner.getWebDriver());
    ajax.await(ExpectedConditions.numberOfElementsToBe(
            By.xpath("//div[@id='" + tabContentId + "' and not(contains(@style,'overflow'))]"), 1));
    $(By.id(tabContentId)).shouldBe(attribute("aria-hidden", previousState));
  }

  private SelenideElement accorionTab(String tabName)
  {
    return $(By.id(accordionId)).findAll(".ui-accordion-header").find(text(tabName)).shouldBe(visible, enabled);
  }

  public boolean isTabOpen(String tabName)
  {
    return accorionTab(tabName).getAttribute("aria-expanded").contains("true");
  }

  public void openTab(String tabName)
  {
    if (!isTabOpen(tabName))
    {
      toggleTab(tabName);
    }
  }
}