package com.axonivy.ivy.webtest.primeui;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.sizeLessThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.primeui.ShowcaseUtil.Showcase;
import com.codeborne.selenide.Configuration;

/**
 * Class to test PrimeUi. Tests on the official Primefaces Showcase.
 */
public class TestPrimeUi {

  @BeforeAll
  static void setup() {
    Configuration.browser = "firefox";
    Configuration.headless = true;
    Configuration.reportsFolder = "target/selenide/reports";
  }

  @Test
  void testSelectOneMenu() {
    String option = "Option1";
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu()
        .selectedItemShould(exactText("Select One"))
        .selectItemByLabel("Option2")
        .selectedItemShould(exactText("Option2"))
        .selectItemByValue(option)
        .selectedItemShould(exactText(option));
    assertThat(menu.getSelectedItem()).isEqualTo(option);
  }

  @Test
  void testSelectOneMenuEditable() {
    String option = "New York";
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenuEditable()
        .selectItemByLabel("Select One")
        .selectedItemShould(exactValue("Select One"))
        .selectItemByValue(option)
        .selectedItemShould(exactValue(option));
    assertThat(menu.getSelectedItem()).isEqualTo(option);
  }

  @Test
  void testSelectCheckBoxMenu_all() {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu()
        .selectAllItems()
        .itemsShouldBeSelected("Brasilia");
  }

  @Test
  void testSelectCheckBoxMenu_itemByValue() {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu()
        .selectItemsByValue("Miami")
        .itemsShouldBeSelected("Miami");
  }

  @Test
  void testSelectCheckBoxMenu_itemsByValue() {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu()
        .selectItemsByValue("Miami", "Brasilia")
        .itemsShouldBeSelected("Miami", "Brasilia");
  }

  @Test
  void testSelectBooleanCheckBox() {
    ShowcaseUtil.open(Showcase.CHECKBOX).checkbox("Basic")
        .shouldBeChecked(false)
        .setChecked()
        .shouldBeChecked(true)
        .removeChecked()
        .shouldBeChecked(false);
  }

  @Test
  void testSelectManyCheckbox() {
    var manyCheckbox = ShowcaseUtil.open(Showcase.MANYCHECKBOX).manyCheckbox();
    manyCheckbox.shouldBeDisabled(false);
    manyCheckbox.shouldBe(empty);
    manyCheckbox.setCheckboxes(Arrays.asList("Option 1", "Option 2"));
    manyCheckbox.shouldBe(exactTexts("Option 1", "Option 2"));
    manyCheckbox.clear();
    manyCheckbox.shouldBe(empty);
  }

  @Test
  void testSelectOneRadio() throws Exception {
    ShowcaseUtil.open(Showcase.ONERADIO).radio()
        .selectItemById($$(".ui-selectoneradio").filter(visible).first().attr("id") + ":0")
        .selectedValueShouldBe(exactValue("Option1"))
        .selectItemByValue("Option2")
        .selectedValueShouldBe(exactValue("Option2"))
        .selectItemByLabel("Option3")
        .selectedValueShouldBe(exactValue("Option3"));
  }

  @Test
  void testTableWithValue() throws Exception {
    var table = ShowcaseUtil.open(Showcase.TABLE).table();
    table.column(0).shouldBe(sizeGreaterThan(5));
    var firstCell = table.valueAt(0, 0);
    var secondCell = firstCell;
    for (var i = 1; firstCell.equals(secondCell); i++) {
      secondCell = table.valueAt(i, 0);
    }
    table.searchColumn(0, firstCell)
        .contains(firstCell)
        .containsNot(secondCell)
        .column(0).shouldBe(sizeLessThan(5));
    // searchGlobal seems not to work on new PrimeFaces Showcase yet.
  }

  @Test
  void testAccordion() {
    ShowcaseUtil.open(Showcase.ACCORDION).accordion()
        .toggleTab("Header I")
        .tabShouldBe("Header I", false)
        .openTab("Header I")
        .tabShouldBe("Header I", true)
        .openTab("Header II")
        .tabShouldBe("Header I", false)
        .tabShouldBe("Header II", true);
  }

  @Test
  void testInputNumber() {
    ShowcaseUtil.open(Showcase.INPUTNUMBER).inputNumber()
        .should(exactValue("0.00"))
        .setValue("5")
        .should(exactValue("5.00"))
        .setValue("3.14")
        .should(exactValue("3.14"));
  }

}
