package com.axonivy.ivy.supplements.primeui.tester.widget;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

public class Dialog
{
  private String dialogId;

  public Dialog(By dialogLocator)
  {
    dialogId = $(dialogLocator).shouldBe(exist).attr("id");
  }

  public void waitForVisibility(Boolean should)
  {
    if (should)
    {
      waitVisible();
    }
    else 
    {
      waitHidden();
    }
  }

  public void waitVisible()
  {
    $(By.id(dialogId)).shouldBe(visible);
  }

  public void waitHidden()
  {
    $(By.id(dialogId)).shouldNotBe(visible);
  }
  
  public void close()
  {
    $(By.id(dialogId)).find(".ui-dialog-titlebar-close").shouldBe(visible).click();
    waitHidden();
  }

  @Deprecated
  public void waitToBeClosedOrError()
  {
    waitHidden();
  }
}
