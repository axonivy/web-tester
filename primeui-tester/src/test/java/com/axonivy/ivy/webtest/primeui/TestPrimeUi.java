package com.axonivy.ivy.webtest.primeui;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.axonivy.ivy.webtest.primeui.ShowcaseUtil.Showcase;
import com.axonivy.ivy.webtest.primeui.widget.Accordion;
import com.axonivy.ivy.webtest.primeui.widget.Dialog;
import com.axonivy.ivy.webtest.primeui.widget.SelectBooleanCheckbox;
import com.axonivy.ivy.webtest.primeui.widget.SelectManyCheckbox;
import com.axonivy.ivy.webtest.primeui.widget.SelectOneMenu;
import com.axonivy.ivy.webtest.primeui.widget.SelectOneRadio;
import com.axonivy.ivy.webtest.primeui.widget.Table;
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
    SelectOneMenu selectOne = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Basic:");
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    selectOne.selectedItemShould(text(ps4));
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

  @Test
  void testSelectCheckBoxMenu_all()
  {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu("Basic:").selectAllItems();
    assertSelectMenu("Brasilia");
  }

  @Test
  void testSelectCheckBoxMenu_itemByValue()
  {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu("Basic:").selectItemByValue("Miami");
    assertSelectMenu("Miami");
  }

  @Test
  void testSelectCheckBoxMenu_itemsByValue()
  {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu("Multiple:").selectItemsByValue("Miami", "Brasilia");
    assertSelectMenu("Miami\nBrasilia");
  }

  private void assertSelectMenu(String selected)
  {
    $$(".ui-button").find(exactText("Submit")).shouldBe(visible).click();
    $(".ui-dialog-content").shouldHave(text(selected));
  }

  @Test
  void testSelectBooleanCheckBox()
  {
    SelectBooleanCheckbox booleanCheckbox = ShowcaseUtil.open(Showcase.CHECKBOX).checkbox("Basic");
    assertThat(booleanCheckbox.isChecked()).isEqualTo(false);
    booleanCheckbox.setChecked();
    assertThat(booleanCheckbox.isChecked()).isEqualTo(true);
    booleanCheckbox.removeChecked();
    assertThat(booleanCheckbox.isChecked()).isEqualTo(false);
  }
  
  @Test
  void testSelectManyCheckbox()
  {
    SelectManyCheckbox manyCheckbox = ShowcaseUtil.open(Showcase.MANYCHECKBOX).manyCheckbox();
    assertThat(manyCheckbox.getSelectedCheckboxes()).isEmpty();
    manyCheckbox.setCheckboxes(Arrays.asList("Xbox One", "Wii U"));
    assertThat(manyCheckbox.getSelectedCheckboxes()).contains("Xbox One", "Wii U");
    manyCheckbox.clear();
    assertThat(manyCheckbox.getSelectedCheckboxes()).isEmpty();
  }

  @Test
  void testSelectOneRadio() throws Exception
  {
    SelectOneRadio selectOneRadio = ShowcaseUtil.open(Showcase.ONERADIO).radio("Console:");
    String radioId = $$(".ui-selectoneradio").first().attr("id") + ":1";
    selectOneRadio.selectItemById(radioId);
    assertThat(selectOneRadio.getSelectedValue()).isEqualTo("PS4");
    selectOneRadio.selectItemByValue("Wii U");
    assertThat(selectOneRadio.getSelectedValue()).isEqualTo("Wii U");
  }

  @Test
  void testTableWithValue() throws Exception
  {
    Table table = ShowcaseUtil.open(Showcase.TABLE).table();
    int brandColumn = 2;

    String firstBrand = table.valueAt(0, brandColumn);
    String lastBrand = table.valueAt(8, brandColumn);

    int lastRow = 7;
    while (StringUtils.equals(firstBrand, lastBrand))
    {
      lastBrand = table.valueAt(lastRow, brandColumn);
      lastRow --;
    }
    table.searchGlobal(firstBrand);
    table.contains(firstBrand);
    table.containsNot(lastBrand);

    table.searchGlobal(lastBrand);
    table.firstRowContains(lastBrand);
    table.containsNot(firstBrand);
  }

  @Test
  void testDialog() throws Exception
  {
    Dialog dialog = ShowcaseUtil.open(Showcase.DIALOG).dialog();
    dialog.waitForVisibility(false);
    $$("button").find(text("Basic")).shouldBe(visible).click();
    dialog.waitForVisibility(true);
    dialog.close();
    dialog.waitHidden();
  }

  @Test
  void testAccordion()
  {
    Accordion accordion = ShowcaseUtil.open(Showcase.ACCORDION).accordion();
    accordion.toggleTab("Godfather Part II");
    validateTabOpen(accordion, "Godfather Part II", "Godfather Part I");
    accordion.openTab("Godfather Part II");
    validateTabOpen(accordion, "Godfather Part II", "Godfather Part I");
    accordion.openTab("Godfather Part I");
    validateTabOpen(accordion, "Godfather Part I", "Godfather Part II");
  }

  private void validateTabOpen(Accordion accordion, String openTab, String closedTab)
  {
    assertThat(accordion.isTabOpen(openTab)).isTrue();
    assertThat(accordion.isTabOpen(closedTab)).isFalse();
  }
  
}