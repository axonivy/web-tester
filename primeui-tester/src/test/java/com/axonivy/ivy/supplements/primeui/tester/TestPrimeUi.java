package com.axonivy.ivy.supplements.primeui.tester;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Accordion;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Dialog;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectBooleanCheckbox;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectCheckboxMenu;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectOneMenu;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectOneRadio;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Table;
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
    Configuration.headless = true;
    Configuration.reportsFolder = "target/senenide/reports";
    Selenide.open();
    driver = WebDriverRunner.getWebDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    prime = new PrimeUi(driver);
  }
  
  @Test
  public void testSelectOneMenu() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/oneMenu.xhtml");

    SelectOneMenu selectOne = getSelectOneMenu();
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

  private SelectOneMenu getSelectOneMenu()
  {
    WebElement selectOne = getElementWithBasicLabel();
    return prime.selectOne(By.id(selectOne.getAttribute("id")));
  }

  @Test
  public void testSelectCheckBoxMenu_all() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");
    
    getCheckboxMenu().selectAllItems();
    submitAndCheck("Brasilia");
  }

  @Test
  public void testSelectCheckBoxMenu_itemByValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    getCheckboxMenu().selectItemByValue("Miami");
    submitAndCheck("Miami");
  }

  private SelectCheckboxMenu getCheckboxMenu()
  {
    WebElement checkbox = getElementWithBasicLabel();
    return prime.selectCheckboxMenu(By.id(checkbox.getAttribute("id")));
  }

  @Test
  public void testSelectCheckBoxMenu_itemsByValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    getMultipleCheckboxMenu().selectItemsByValue("Miami", "Brasilia");
    submitAndCheck("Miami\n"
            + "Brasilia");
  }

  private SelectCheckboxMenu getMultipleCheckboxMenu()
  {
    WebElement checkbox = getElementWithLabel("Multiple:");
    return prime.selectCheckboxMenu(By.id(checkbox.getAttribute("id")));
  }

  private void submitAndCheck(String selected)
  {
    driver.findElement(By.xpath("//span[text()='Submit']")).click();
    outputContains(selected);
  }

  private void outputContains(String chosenValue)
  {
    By contentLocator = By.xpath("//div[@aria-modal='true' and @role='dialog']/div[2]");
    try
    {
      prime.awaitCondition(ExpectedConditions.textToBePresentInElementLocated(contentLocator,
              chosenValue));
    }
    catch (TimeoutException ex)
    {
      String text = driver.findElement(contentLocator).getText();
      throw new RuntimeException("Could not find text '" + chosenValue + "' in '" + text + "'", ex);
    }
  }

  @Test
  public void testSelectBooleanCheckBox() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/booleanCheckbox.xhtml");

    SelectBooleanCheckbox selectBooleanCheckbox = prime
            .selectBooleanCheckbox(By.xpath("//div[span/text()='Basic']"));
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(false);

    selectBooleanCheckbox.setChecked();
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(true);
  }

  @Test
  public void testSelectOneRadio() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/oneRadio.xhtml");

    String elementId = getElementId("//table[//label/text()='Console:']//table");
    SelectOneRadio selectOneRadio = prime.selectOneRadio(By.id(elementId));
    selectOneRadio.selectItemById(elementId + ":1");
    assertThat(selectOneRadio.getSelected()).isEqualTo("PS4");
    selectOneRadio.selectItemByValue("Wii U");
    assertThat(selectOneRadio.getSelected()).isEqualTo("Wii U");
    selectOneRadio.selectItemByCss("label[for=" + elementId.replace(":", "\\:") + "\\:0]");
    assertThat(selectOneRadio.getSelected()).isEqualTo("Xbox One");
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

  private WebElement getElementWithBasicLabel()
  {
    return getElementWithLabel("Basic:");
  }
  private WebElement getElementWithLabel(String labelText)
  {
    return driver.findElement(By.xpath("//tr[td[1]/label/text()='" + labelText + "']/td[2]/div"));
  }
}