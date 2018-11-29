package com.axonivy.ivy.supplements.primeui.tester;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Accordion;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Dialog;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectBooleanCheckbox;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectCheckboxMenu;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectOneMenu;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.SelectOneRadio;
import com.axonivy.ivy.supplements.primeui.tester.PrimeUi.Table;

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

  @Test
  public void testSelectOneMenu() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/oneMenu.xhtml");

    SelectOneMenu selectOne = prime.selectOne(By.id("j_idt692:console"));
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

  @Test
  public void testSelectCheckBoxMenu_all() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    SelectCheckboxMenu selectCheckBox = prime.selectCheckboxMenu(By.id("j_idt691:menu"));
    selectCheckBox.selectAllItems();
    submitAndCheck("Brasilia");
  }

  @Test
  public void testSelectCheckBoxMenu_itemByValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    SelectCheckboxMenu selectCheckBox = prime.selectCheckboxMenu(By.id("j_idt691:menu"));
    selectCheckBox.selectItemByValue("Miami");
    submitAndCheck("Miami");
  }
  
  @Test
  public void testSelectCheckBoxMenu_itemsByValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    SelectCheckboxMenu selectCheckBox = prime.selectCheckboxMenu(By.id("j_idt691:menu"));
    selectCheckBox.selectItemsByValue("Miami", "Brasilia");
    submitAndCheck("Miami\n"
            + "Brasilia");
  }

  private void submitAndCheck(String selected)
  {
    By submitButton = By.id("j_idt691:j_idt699");
    driver.findElement(submitButton).click();
    outputContains(selected);
  }

  private void outputContains(String chosenValue)
  {
    By contentId = By.id("j_idt691:j_idt701_content");
    try
    {
      prime.awaitCondition(ExpectedConditions.textToBePresentInElementLocated(contentId,
              chosenValue));
    }
    catch (TimeoutException ex)
    {
      String text = driver.findElement(contentId).getText();
      throw new RuntimeException("Could not find text '"+chosenValue+"' in '"+text+"'", ex);
    }
  }

  @Test
  public void testSelectBooleanCheckBox() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/booleanCheckbox.xhtml");

    SelectBooleanCheckbox selectBooleanCheckbox = prime.selectBooleanCheckbox(By.id("j_idt691:j_idt694"));
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(false);

    selectBooleanCheckbox.setChecked();
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(true);
  }

  @Test
  public void testSelectOneRadio() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/oneRadio.xhtml");

    SelectOneRadio selectOneRadio = prime.selectOneRadio(By.id("j_idt692:console"));
    selectOneRadio.selectItemById("j_idt692:console:1");
    assertThat(selectOneRadio.getSelected()).isEqualTo("PS4");
    selectOneRadio.selectItemByValue("Wii U");
    assertThat(selectOneRadio.getSelected()).isEqualTo("Wii U");
  }

  @Test
  public void testTableWithValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/data/datatable/filter.xhtml");

    Table table = prime.table(By.id("j_idt693:j_idt694"));
    int brandColumn = 2;

    String firstBrand = table.valueAt(0, brandColumn);
    String lastBrand = table.valueAt(8, brandColumn);

    int lastRow = 7;
    if (StringUtils.equals(firstBrand, lastBrand))
    {
      for (; (!StringUtils.equals(firstBrand, lastBrand)); lastRow--)
      {
        lastBrand = table.valueAt(lastRow, brandColumn);
      }
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
    driver.findElement(By.id("j_idt693:j_idt694:globalFilter")).clear();
    driver.findElement(By.id("j_idt693:j_idt694:globalFilter")).sendKeys(firstBrand);
  }

  @Test
  public void testDialog() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/overlay/dialog/basic.xhtml");

    Dialog dialog = prime.dialog(By.id("j_idt696"));
    dialog.waitForVisibility(false);
    driver.findElement(By.id("j_idt693")).click();
    dialog.waitForVisibility(true);

    driver.findElement(By.xpath("//*[@id='j_idt696']/div[1]/a")).click();
    dialog.waitToBeClosedOrError();
  }

  @Test
  public void testAccordion()
  {
    driver.get("http://primefaces.org/showcase/ui/panel/accordionPanel.xhtml");

    Accordion accordion = prime.accordion(By.id("form:j_idt692"));
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

  @Before
  public void setUp()
  {
    driver = new HtmlUnitDriver(true);
    prime = new PrimeUi(driver);
  }

  @After
  public void quitDriver()
  {
    driver.quit();
  }
}