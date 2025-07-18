package com.axonivy.ivy.webtest.primeui;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.primeui.widget.Accordion;
import com.axonivy.ivy.webtest.primeui.widget.InputNumber;
import com.axonivy.ivy.webtest.primeui.widget.SelectBooleanCheckbox;
import com.axonivy.ivy.webtest.primeui.widget.SelectCheckboxMenu;
import com.axonivy.ivy.webtest.primeui.widget.SelectManyCheckbox;
import com.axonivy.ivy.webtest.primeui.widget.SelectOneMenu;
import com.axonivy.ivy.webtest.primeui.widget.SelectOneRadio;
import com.axonivy.ivy.webtest.primeui.widget.Table;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class ShowcaseUtil {
  private static String baseShowcaseUrl = "https://primefaces.org/showcase/ui/";

  public static Showcase open(Showcase showcase) {
    Selenide.open(baseShowcaseUrl + showcase.url);
    return showcase;
  }

  public enum Showcase {
    CHECKBOX("input/booleanCheckbox.xhtml", ".ui-selectbooleancheckbox"),
    ONEMENU("input/oneMenu.xhtml", ".ui-selectonemenu"),
    CHECKBOXMENU("input/checkboxMenu.xhtml", ".ui-selectcheckboxmenu.ui-selectcheckboxmenu-multiple"),
    MANYCHECKBOX("input/manyCheckbox.xhtml", ".ui-selectmanycheckbox"),
    ONERADIO("input/oneRadio.xhtml", ".ui-selectoneradio"),
    TABLE("data/datatable/filter.xhtml", ".ui-datatable"),
    ACCORDION("panel/accordionPanel.xhtml", ".ui-accordion"),
    INPUTNUMBER("input/inputNumber.xhtml", ".ui-inputnumber");

    private final String url;
    private final String selector;

    Showcase(String url, String selector) {
      this.url = url;
      this.selector = selector;
    }

    public SelectBooleanCheckbox checkbox(String label) {
      return PrimeUi.selectBooleanCheckbox(elementId($$(selector).find(text(label))));
    }

    public SelectOneMenu oneMenu() {
      return PrimeUi.selectOne(firstElement());
    }

    public SelectOneMenu oneMenuEditable() {
      var oneMenuEditable = $$("label").find(exactText("Editable")).parent().find(".ui-selectonemenu");
      return PrimeUi.selectOne(elementId(oneMenuEditable));
    }

    public SelectCheckboxMenu checkboxMenu() {
      return PrimeUi.selectCheckboxMenu(firstElement());
    }

    public SelectManyCheckbox manyCheckbox() {
      return PrimeUi.selectManyCheckbox(firstElement());
    }

    public SelectOneRadio radio() {
      return PrimeUi.selectOneRadio(firstElement());
    }

    public Table table() {
      return PrimeUi.table(firstElement());
    }

    public Accordion accordion() {
      return PrimeUi.accordion(firstElement());
    }

    public InputNumber inputNumber() {
      return PrimeUi.inputNumber(firstElement());
    }

    private By firstElement() {
      return elementId($$(selector).filter(visible).first());
    }

    private By elementId(SelenideElement element) {
      return By.id(element.attr("id"));
    }
  }

}
