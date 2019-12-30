package com.axonivy.ivy.supplements.primeui.tester;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Accordion;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Dialog;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Table;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectBooleanCheckbox;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectOneMenu;
import com.axonivy.ivy.supplements.primeui.tester.widget.SelectOneRadio;
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
  public void testSelectOneMenu() throws Exception
  {
    open("http://primefaces.org/showcase/ui/input/oneMenu.xhtml");
    SelectOneMenu selectOne = PrimeUi.selectOne(selectMenuForLabel("Basic:"));
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

  @Test
  public void testSelectCheckBoxMenu_all() throws Exception
  {
    open("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    PrimeUi.selectCheckboxMenu(selectMenuForLabel("Basic:")).selectAllItems();
    assertSelectMenu("Brasilia");
  }

  @Test
  public void testSelectCheckBoxMenu_itemByValue() throws Exception
  {
    open("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    PrimeUi.selectCheckboxMenu(selectMenuForLabel("Basic:")).selectItemByValue("Miami");
    assertSelectMenu("Miami");
  }

  @Test
  public void testSelectCheckBoxMenu_itemsByValue() throws Exception
  {
    open("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    PrimeUi.selectCheckboxMenu(selectMenuForLabel("Multiple:")).selectItemsByValue("Miami", "Brasilia");
    assertSelectMenu("Miami\nBrasilia");
  }

  @Test
  public void testSelectBooleanCheckBox() throws Exception
  {
    open("http://primefaces.org/showcase/ui/input/booleanCheckbox.xhtml");
    SelectBooleanCheckbox selectBooleanCheckbox = PrimeUi.selectBooleanCheckbox(checkboxForLabel("Basic"));
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(false);
    selectBooleanCheckbox.setChecked();
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(true);
    selectBooleanCheckbox.removeChecked();
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(false);
  }

  @Test
  public void testSelectOneRadio() throws Exception
  {
    open("http://primefaces.org/showcase/ui/input/oneRadio.xhtml");
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
    driver.get("http://primefaces.org/showcase/ui/data/datatable/filter.xhtml");

    String tableId = getElementId("//form/div[1]");
    Table table = prime.table(By.id(tableId));
    int brandColumn = 2;

    String firstBrand = table.valueAt(0, brandColumn);
    String lastBrand = table.valueAt(8, brandColumn);

    int lastRow = 7;
    while (StringUtils.equals(firstBrand, lastBrand))
    {
      lastBrand = table.valueAt(lastRow, brandColumn);
      lastRow --;
    }
    searchTable(firstBrand, tableId);
    table.contains(firstBrand);
    table.containsNot(lastBrand);

    searchTable(lastBrand, tableId);
    table.firstRowContains(lastBrand);
    table.containsNot(firstBrand);
  }

  private void searchTable(String firstBrand, String tableId)
  {
    driver.findElement(By.id(tableId + ":globalFilter")).clear();
    driver.findElement(By.id(tableId + ":globalFilter")).sendKeys(firstBrand);
  }

  @Test
  public void testDialog() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/overlay/dialog/basic.xhtml");

    Dialog dialog = prime.dialog(By.xpath("//div[div/span/text()='Basic Dialog']"));
    dialog.waitForVisibility(false);
    driver.findElement(By.xpath("//button[span/text()='Basic']")).click();
    dialog.waitForVisibility(true);

    driver.findElement(By.xpath("//a[@aria-label='Close']")).click();
    dialog.waitToBeClosedOrError();
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

  private String getElementId(String xPath)
  {
    String elementId = driver
            .findElement(By.xpath(xPath))
            .getAttribute("id");
    return elementId;
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