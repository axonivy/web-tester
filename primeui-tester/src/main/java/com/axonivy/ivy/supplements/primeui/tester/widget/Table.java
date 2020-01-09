package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

public class Table
{
  private final String tableId;

  public Table(By dataTable)
  {
    tableId = $(dataTable).shouldBe(visible).attr("id");
  }

  public void firstRowContains(String expectedText)
  {
    $(By.id(tableId + "_data")).findAll("tr").first().shouldBe(visible, text(expectedText));
  }

  public void contains(String checkText)
  {
    $(By.id(tableId)).shouldBe(visible, text(checkText));
  }

  public void containsNot(String checkText)
  {
    $(By.id(tableId + "_data")).shouldNotHave(text(checkText));
  }

  public String valueAt(int row, int column) throws Exception
  {
    return $(By.id(tableId + "_data")).findAll("tr")
            .find(attribute("data-ri", String.valueOf(row))).find("td", column + 1).shouldBe(visible).getText();
  }
  
  public void searchGlobal(String search)
  {
    $(By.id(tableId + ":globalFilter")).shouldBe(visible).clear();
    $(By.id(tableId + ":globalFilter")).shouldBe(visible).sendKeys(search);
  }
}