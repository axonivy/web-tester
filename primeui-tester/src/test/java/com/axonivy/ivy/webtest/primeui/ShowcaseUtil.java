package com.axonivy.ivy.webtest.primeui;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

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
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class ShowcaseUtil
{
  private static String baseShowcaseUrl = "https://primefaces.org/showcase/ui/";

  public static Showcase open(Showcase showcase)
  {
    Selenide.open(baseShowcaseUrl + showcase.url);
    return showcase;
  }

  public static enum Showcase
  {
    CHECKBOX ("input/booleanCheckbox.xhtml", ".ui-selectbooleancheckbox"),
    ONEMENU ("input/oneMenu.xhtml", "div.ui-widget"),
    CHECKBOXMENU ("input/checkboxMenu.xhtml", "div.ui-widget"),
    MANYCHECKBOX ("input/manyCheckbox.xhtml", ".ui-selectmanycheckbox"),
    ONERADIO ("input/oneRadio.xhtml", ".ui-selectoneradio"),
    TABLE ("data/datatable/filter.xhtml", ".ui-datatable"),
    DIALOG ("overlay/dialog/basic.xhtml", ".ui-dialog"),
    ACCORDION ("panel/accordionPanel.xhtml", ".ui-accordion"), 
    INPUTNUMBER ("input/inputNumber.xhtml", ".ui-inputnumber");

    private String url;
    private String selector;

    Showcase(String url, String selector)
    {
      this.url = url;
      this.selector = selector;
    }

    public SelectBooleanCheckbox checkbox(String label)
    {
      return PrimeUi.selectBooleanCheckbox(elementId($$(selector).find(text(label))));
    }

    public SelectOneMenu oneMenu(String label)
    {
      return PrimeUi.selectOne(elementForLabel(label));
    }

    public SelectCheckboxMenu checkboxMenu(String label)
    {
      return PrimeUi.selectCheckboxMenu(elementForLabel(label));
    }

    public SelectManyCheckbox manyCheckbox()
    {
      return PrimeUi.selectManyCheckbox(firstElement());
    }

    public SelectOneRadio radio(String label)
    {
      return PrimeUi.selectOneRadio(elementForLabel(label));
    }
    
    public Table table()
    {
      return PrimeUi.table(firstElement());
    }
    
    public Dialog dialog()
    {
      return PrimeUi.dialog(firstElement());
    }
    
    public Accordion accordion()
    {
      return PrimeUi.accordion(firstElement());
    }
    
    public InputNumber inputNumber()
    {
      return PrimeUi.inputNumber(firstElement());
    }

    private By elementForLabel(String label)
    {
      return elementId($$("tr label").find(text(label)).parent().parent().find(selector));
    }
    
    private By firstElement()
    {
      return elementId($$(selector).first());
    }
    
    private By elementId(SelenideElement element)
    {
      return By.id(element.attr("id"));
    }
  }

}