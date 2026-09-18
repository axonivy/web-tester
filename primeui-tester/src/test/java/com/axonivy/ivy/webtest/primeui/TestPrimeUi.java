package com.axonivy.ivy.webtest.primeui;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.sizeLessThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.webdriver;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;

import com.axonivy.ivy.webtest.primeui.ShowcaseUtil.Showcase;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

/**
 * Class to test PrimeUi. Tests on the official Primefaces Showcase.
 */
class TestPrimeUi {

  @BeforeAll
  static void beforeAll() {
    Configuration.browser = System.getProperty("selenide.browser", "firefox");
    Configuration.headless = true;
    Configuration.reportsFolder = "target/selenide/reports";
  }

  @Test
  void selectOneMenu() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Basic")
        .selectedItemShould(exactText("Select One"))
        .selectItemByLabel("Option1")
        .selectedItemShould(exactText("Option1"))
        .selectItemByValue("Option2")
        .selectedItemShould(exactText("Option2"));
    assertThat(menu.getSelectedItem()).isEqualTo("Option2");
  }

  @Test
  void selectOneMenu_duringOpeningTransition() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Basic");
    // A slow, translucent theme must work without relying on hover latency or a final opacity of 1.
    executeJavaScript("""
        const style = document.createElement('style');
        style.textContent = `
            .ui-selectonemenu-panel { opacity: .8; }
            .ui-connected-overlay-enter-active { transition-duration: 1s !important; }
            `;
        document.head.appendChild(style);
        """);
    menu.selectItemByLabel("Option2")
        .selectedItemShould(exactText("Option2"))
        .selectItemByValue("Option1")
        .selectedItemShould(exactText("Option1"));
  }

  @Test
  void selectOneMenu_withoutAnimations() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Basic");
    executeJavaScript("PrimeFaces.animationEnabled = false;");
    menu.selectItemByLabel("Option2")
        .selectedItemShould(exactText("Option2"))
        .selectItemByValue("Option1")
        .selectedItemShould(exactText("Option1"));
  }

  @Test
  void selectOneMenu_lazy() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Lazy")
        .selectedItemShould(exactText("Select One"))
        .selectItemByLabel("Option 1")
        .selectedItemShould(exactText("Option 1"))
        .selectItemByValue("Option 19")
        .selectedItemShould(exactText("Option 19"));
    assertThat(menu.getSelectedItem()).isEqualTo("Option 19");
  }

  @Test
  void selectOneMenu_reselectInScrollablePanel() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Lazy");
    var window = webdriver().driver().getWebDriver().manage().window();
    var originalSize = window.getSize();
    try {
      window.setSize(new Dimension(800, 600));
      executeJavaScript("document.querySelector('[id$=\":lazy\"]').scrollIntoView({block: 'center', behavior: 'instant'});");
      menu.selectItemByLabel("Option 19")
          .selectedItemShould(exactText("Option 19"))
          .selectItemByValue("Option 10")
          .selectedItemShould(exactText("Option 10"))
          .selectItemByValue("Option 1")
          .selectedItemShould(exactText("Option 1"));
    } finally {
      window.setSize(originalSize);
    }
  }

  @Test
  void selectOneMenu_grouping() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Grouping")
        .selectedItemShould(exactText("Select One"))
        .selectItemByLabel("Mexico")
        .selectedItemShould(exactText("Mexico"))
        .selectItemByValue("Germany")
        .selectedItemShould(exactText("Germany"));
    assertThat(menu.getSelectedItem()).isEqualTo("Germany");
  }

  @Test
  void selectOneMenu_editable() {
    var menu = ShowcaseUtil.open(Showcase.ONEMENU).oneMenu("Editable")
        .selectedItemShould(Condition.empty)
        .selectItemByLabel("Barcelona")
        .selectedItemShould(exactValue("Barcelona"))
        .selectItemByValue("New York")
        .selectedItemShould(exactValue("New York"));
    assertThat(menu.getSelectedItem()).isEqualTo("New York");
  }

  @Test
  void selectCheckBoxMenu_all() {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu()
        .selectAllItems()
        .itemsShouldBeSelected("Brasilia");
  }

  @Test
  void selectCheckBoxMenu_itemByValue() {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu()
        .selectItemsByValue("Miami")
        .itemsShouldBeSelected("Miami");
  }

  @Test
  void selectCheckBoxMenu_itemsByValue() {
    ShowcaseUtil.open(Showcase.CHECKBOXMENU).checkboxMenu()
        .selectItemsByValue("Miami", "Brasilia")
        .itemsShouldBeSelected("Miami", "Brasilia");
  }

  @Test
  void selectBooleanCheckBox() {
    ShowcaseUtil.open(Showcase.CHECKBOX).checkbox("Basic")
        .shouldBeChecked(false)
        .setChecked()
        .shouldBeChecked(true)
        .removeChecked()
        .shouldBeChecked(false);
  }

  @Test
  void selectManyCheckbox() {
    var manyCheckbox = ShowcaseUtil.open(Showcase.MANYCHECKBOX).manyCheckbox();
    manyCheckbox.shouldBeDisabled(false);
    manyCheckbox.shouldBe(empty);
    manyCheckbox.setCheckboxes(Arrays.asList("Option 1", "Option 2"));
    manyCheckbox.shouldBe(exactTexts("Option 1", "Option 2"));
    manyCheckbox.clear();
    manyCheckbox.shouldBe(empty);
  }

  @Test
  void selectOneRadio() throws Exception {
    ShowcaseUtil.open(Showcase.ONERADIO).radio()
        .selectItemById($$(".ui-selectoneradio").filter(visible).first().attr("id") + ":0")
        .selectedValueShouldBe(exactValue("Option1"))
        .selectItemByValue("Option2")
        .selectedValueShouldBe(exactValue("Option2"))
        .selectItemByLabel("Option3")
        .selectedValueShouldBe(exactValue("Option3"));
  }

  @Test
  void tableWithValue() throws Exception {
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
  void accordion() {
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
  void inputNumber() {
    ShowcaseUtil.open(Showcase.INPUTNUMBER).inputNumber()
        .should(exactValue("0.00"))
        .setValue("5")
        .should(exactValue("5.00"))
        .setValue("3.14")
        .should(exactValue("3.14"));
  }

}
