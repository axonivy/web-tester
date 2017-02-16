package com.axonivy.ivy.supplements.primeui.tester;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    SelectOneMenu selectOne = prime.selectOne(By.id("j_idt88:console"));
    assertThat(selectOne.getSelectedItem()).isEqualTo("Select One");
    String ps4 = "PS4";
    selectOne.selectItemByLabel(ps4);
    assertThat(selectOne.getSelectedItem()).isEqualTo(ps4);
  }

  @Test
  public void selectCheckBoxMenu_all() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    SelectCheckboxMenu selectCheckBox = prime.selectCheckboxMenu(By.id("j_idt87:menu"));
    selectCheckBox.selectAllItems();
    By submitButton = By.id("j_idt87:j_idt91");
    driver.findElement(submitButton).click();
    outputContains("Brasilia");
  }

  @Test
  public void selectCheckBoxMenu_itemByValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/checkboxMenu.xhtml");

    SelectCheckboxMenu selectCheckBox = prime.selectCheckboxMenu(By.id("j_idt87:menu"));
    selectCheckBox.selectItemByValue("Miami");
    By submitButton = By.id("j_idt87:j_idt91");
    driver.findElement(submitButton).click();
    outputContains("Miami");
  }

  private void outputContains(String chosenValue)
  {
    prime.await(ExpectedConditions.textToBePresentInElementLocated(By.id("j_idt87:j_idt92_content"),
            chosenValue));
  }

  @Test
  public void testSelectBooleanCheckBox() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/booleanCheckbox.xhtml");

    SelectBooleanCheckbox selectBooleanCheckbox = prime.selectBooleanCheckbox(By.id("j_idt87:j_idt90_input"));
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(false);

    selectBooleanCheckbox.setChecked();
    assertThat(selectBooleanCheckbox.isChecked()).isEqualTo(true);
  }

  @Test
  public void testSelectOneRadio() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/input/oneRadio.xhtml");

    SelectOneRadio selectOneRadio = prime.selectOneRadio(By.id("j_idt88:console"));
    selectOneRadio.selectItemById("j_idt88:console:1");
    assertThat(selectOneRadio.getSelected()).isEqualTo("PS4");
    selectOneRadio.selectItemByValue("Wii U");
    assertThat(selectOneRadio.getSelected()).isEqualTo("Wii U");
  }

  @Test
  public void testTableWithValue() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/data/datatable/filter.xhtml");

    Table table = prime.table(By.id("j_idt89:j_idt90"));
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
    driver.findElement(By.id("j_idt89:j_idt90:globalFilter")).clear();
    driver.findElement(By.id("j_idt89:j_idt90:globalFilter")).sendKeys(firstBrand);
  }

  @Test
  public void testDialog() throws Exception
  {
    driver.get("http://primefaces.org/showcase/ui/overlay/dialog/basic.xhtml");

    driver.findElement(By.id("j_idt89")).click();
    Dialog dialog = prime.dialog(By.id("j_idt92"));
    dialog.waitForVisibility(true);

    driver.findElement(By.xpath("//*[@id='j_idt92']/div[1]/a")).click();
    dialog.waitToBeClosedOrError();
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