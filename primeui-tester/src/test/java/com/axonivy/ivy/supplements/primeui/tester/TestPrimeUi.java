package com.axonivy.ivy.supplements.primeui.tester;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Accordion;
import com.axonivy.ivy.supplements.primeui.tester.widget.Dialog;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectBooleanCheckbox;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectManyCheckbox;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectOneMenu;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectOneRadio;
import com.axonivy.ivy.supplements.primeui.tester.widget.Table;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

/**
 * Class to test PrimeUi. Tests on the official Primefaces Showcase.
 * 
 * @author dhu
 * @since Apr 28, 2016
 */
public class TestPrimeUi
{
  private WebDriver driver;
  private PrimeUi prime;

  @BeforeEach
  public void setUp()
  {
    Configuration.browser = "firefox";
    //Configuration.headless = true;
    Configuration.reportsFolder = "target/senenide/reports";
    Selenide.open();
    driver = WebDriverRunner.getWebDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    prime = new PrimeUi(driver);
  }
  
  @Test
  public void testSelectOneMenu()
  {
    open("https://primefaces.org/showcase/ui/input/oneMenu.xhtml");
    SelectOneMenu selectOne = PrimeUi.selectOne(selectMenuForLabel("Basic:"));
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

  @Test
  public void testSelectCheckBoxMenu_all()
  {
    open("https://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    PrimeUi.selectCheckboxMenu(selectMenuForLabel("Basic:")).selectAllItems();
    assertSelectMenu("Brasilia");
  }

  @Test
  public void testSelectCheckBoxMenu_itemByValue()
  {
    open("https://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    PrimeUi.selectCheckboxMenu(selectMenuForLabel("Basic:")).selectItemByValue("Miami");
    assertSelectMenu("Miami");
  }

  @Test
  public void testSelectCheckBoxMenu_itemsByValue()
  {
    open("https://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    PrimeUi.selectCheckboxMenu(selectMenuForLabel("Multiple:")).selectItemsByValue("Miami", "Brasilia");
    assertSelectMenu("Miami\nBrasilia");
  }

  @Test
  public void testSelectBooleanCheckBox()
  {
    open("https://primefaces.org/showcase/ui/input/booleanCheckbox.xhtml");
    SelectBooleanCheckbox booleanCheckbox = PrimeUi.selectBooleanCheckbox(checkboxForLabel("Basic"));
    assertThat(booleanCheckbox.isChecked()).isEqualTo(false);
    booleanCheckbox.setChecked();
    assertThat(booleanCheckbox.isChecked()).isEqualTo(true);
    booleanCheckbox.removeChecked();
    assertThat(booleanCheckbox.isChecked()).isEqualTo(false);
  }
  
  @Test
  public void testSelectManyCheckbox()
  {
    open("https://www.primefaces.org/showcase/ui/input/manyCheckbox.xhtml");
    SelectManyCheckbox manyCheckbox = PrimeUi.selectManyCheckbox(firstManyCheckbox());
    assertThat(manyCheckbox.getSelectedCheckboxes()).isEmpty();
    manyCheckbox.setCheckboxes(Arrays.asList("Xbox One", "Wii U"));
    assertThat(manyCheckbox.getSelectedCheckboxes()).contains("Xbox One", "Wii U");
    manyCheckbox.clear();
    assertThat(manyCheckbox.getSelectedCheckboxes()).isEmpty();
  }

  @Test
  public void testSelectOneRadio() throws Exception
  {
    open("https://primefaces.org/showcase/ui/input/oneRadio.xhtml");
    SelectOneRadio selectOneRadio = PrimeUi.selectOneRadio(radioForLabel("Console:"));
    String radioId = $(radioForLabel("Console:")).attr("id") + ":1";
    selectOneRadio.selectItemById(radioId);
    assertThat(selectOneRadio.getSelected()).isEqualTo("PS4");
    selectOneRadio.selectItemByValue("Wii U");
    assertThat(selectOneRadio.getSelected()).isEqualTo("Wii U");
  }

  @Test
  public void testTableWithValue() throws Exception
  {
    open("https://primefaces.org/showcase/ui/data/datatable/filter.xhtml");

    Table table = PrimeUi.table(By.id(firstDataTableId()));
    int brandColumn = 2;

    String firstBrand = table.valueAt(0, brandColumn);
    String lastBrand = table.valueAt(8, brandColumn);

    int lastRow = 7;
    while (StringUtils.equals(firstBrand, lastBrand))
    {
      lastBrand = table.valueAt(lastRow, brandColumn);
      lastRow --;
    }
    searchTable(firstBrand);
    table.contains(firstBrand);
    table.containsNot(lastBrand);

    searchTable(lastBrand);
    table.firstRowContains(lastBrand);
    table.containsNot(firstBrand);
  }

  private void searchTable(String firstBrand)
  {
    driver.findElement(By.id(firstDataTableId() + ":globalFilter")).clear();
    driver.findElement(By.id(firstDataTableId() + ":globalFilter")).sendKeys(firstBrand);
  }

  @Test
  public void testDialog() throws Exception
  {
    open("https://primefaces.org/showcase/ui/overlay/dialog/basic.xhtml");
    Dialog dialog = PrimeUi.dialog(firstDialog());
    dialog.waitForVisibility(false);
    $$("button").find(text("Basic")).shouldBe(visible).click();
    dialog.waitForVisibility(true);
    dialog.close();
    dialog.waitHidden();
  }

  @Test
  public void testAccordion()
  {
    driver.get("http://primefaces.org/showcase/ui/panel/accordionPanel.xhtml");

    Accordion accordion = prime.accordion(By.xpath("//h3[text()='Basic']/following-sibling::div"));
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
  
  private By firstDialog()
  {
    return By.id($$(".ui-dialog").first().attr("id"));
  }
  
  private String firstDataTableId()
  {
    return $$(".ui-datatable").first().attr("id");
  }
  
  private By firstManyCheckbox()
  {
    return By.id($$(".ui-selectmanycheckbox").first().attr("id"));
  }
  
  private By radioForLabel(String label)
  {
    return By.id($$("label").find(text(label)).parent().parent().find(".ui-selectoneradio").attr("id"));
  }
  
  private By checkboxForLabel(String label)
  {
    return By.id($$(".ui-selectbooleancheckbox").find(text(label)).attr("id"));
  }

  private By selectMenuForLabel(String label)
  {
    return By.id($$("tr label").find(text(label)).parent().parent().find("div.ui-widget").attr("id"));
  }
  
  private void assertSelectMenu(String selected)
  {
    $$(".ui-button").find(exactText("Submit")).shouldBe(visible).click();
    $(".ui-dialog-content").shouldHave(text(selected));
  }
  
}