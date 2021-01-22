package com.axonivy.ivy.webtest.primeui;

import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.primeui.ShowcaseUtil.Showcase;
import com.axonivy.ivy.webtest.primeui.widget.Accordion;
import com.codeborne.selenide.Configuration;

/**
 * Class to test PrimeUi. Tests on the official Primefaces Showcase.
 */
public class TestPrimeUi
{
  
  @BeforeAll
  static void setup()
  {
    Configuration.browser = "firefox";
    Configuration.headless = true;
    Configuration.reportsFolder = "target/selenide/reports";
  }
  
  @Test
  void testSelectOneMenu()
  {
    var selectOne = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu();
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    var option = "Option1";
    selectOne.selectItemByLabel(option);
    selectOne.selectedItemShould(text(option));
    assertThat(selectOne.getSelectedItem()).isEqualTo(option);
  }

  @Test
  void testSelectCheckBoxMenu_all()
  {
    var menu = ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu();
    menu.selectAllItems();
    menu.itemsShouldBeSelected("Brasilia");
  }

  @Test
  void testSelectCheckBoxMenu_itemByValue()
  {
    var menu = ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu();
    menu.selectItemByValue("Miami");
    menu.itemsShouldBeSelected("Miami");
  }

  @Test
  void testSelectCheckBoxMenu_itemsByValue()
  {
    var menu = ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu();
    menu.selectItemsByValue("Miami", "Brasilia");
    menu.itemsShouldBeSelected("Miami", "Brasilia");
  }

  @Test
  void testSelectBooleanCheckBox()
  {
    var booleanCheckbox = ShowcaseUtil.open(Showcase.CHECKBOX).checkbox("Basic");
    assertThat(booleanCheckbox.isChecked()).isEqualTo(false);
    booleanCheckbox.setChecked();
    assertThat(booleanCheckbox.isChecked()).isEqualTo(true);
    booleanCheckbox.removeChecked();
    assertThat(booleanCheckbox.isChecked()).isEqualTo(false);
  }
  
  @Test
  void testSelectManyCheckbox()
  {
    var manyCheckbox = ShowcaseUtil.open(Showcase.MANYCHECKBOX).manyCheckbox();
    assertThat(manyCheckbox.getSelectedCheckboxes()).isEmpty();
    manyCheckbox.setCheckboxes(Arrays.asList("Option 1", "Option 2"));
    assertThat(manyCheckbox.getSelectedCheckboxes()).contains("Option 1", "Option 2");
    manyCheckbox.clear();
    assertThat(manyCheckbox.getSelectedCheckboxes()).isEmpty();
  }

  @Test
  void testSelectOneRadio() throws Exception
  {
    var selectOneRadio = ShowcaseUtil.open(Showcase.ONERADIO).radio();
    var radioId = $$(".ui-selectoneradio").filter(visible).first().attr("id") + ":0";
    selectOneRadio.selectItemById(radioId);
    assertThat(selectOneRadio.getSelectedValue()).isEqualTo("Option1");
    selectOneRadio.selectItemByValue("Option2");
    assertThat(selectOneRadio.getSelectedValue()).isEqualTo("Option2");
  }

  @Test
  void testTableWithValue() throws Exception
  {
    var table = ShowcaseUtil.open(Showcase.TABLE).table();
    var firstBrand = table.valueAt(0, 0);
    var lastBrand = table.valueAt(8, 0);

    table.contains(firstBrand);
    table.contains(lastBrand);
    table.firstRowContains(firstBrand);
    table.containsNot("Hello World");
    //global search seems to be broken on new PrimeFaces Showcase yet 
  }

  @Test
  void testDialog() throws Exception
  {
    var dialog = ShowcaseUtil.open(Showcase.DIALOG).dialog();
    dialog.waitForVisibility(false);
    $$(".card .ui-button").first().shouldBe(visible).click();
    dialog.waitForVisibility(true);
    dialog.close();
    dialog.waitHidden();
  }

  @Test
  void testAccordion()
  {
    var accordion = ShowcaseUtil.open(Showcase.ACCORDION).accordion();
    accordion.toggleTab("Header I");
    accordion.isTabOpen("Header I");
    accordion.openTab("Header II");
    validateTabOpen(accordion, "Header II", "Header I");
    accordion.openTab("Header I");
    validateTabOpen(accordion, "Header I", "Header II");
  }

  @Test
  void testInputNumber()
  {
    var inputNumber = ShowcaseUtil.open(Showcase.INPUTNUMBER).inputNumber();
    inputNumber.should(exactValue("0.00"));
    inputNumber.setValue("5");
    inputNumber.should(exactValue("5.00"));
    inputNumber.setValue("3.14");
    inputNumber.should(exactValue("3.14"));
  }

  private void validateTabOpen(Accordion accordion, String openTab, String closedTab)
  {
    assertThat(accordion.isTabOpen(openTab)).isTrue();
    assertThat(accordion.isTabOpen(closedTab)).isFalse();
  }
  
}