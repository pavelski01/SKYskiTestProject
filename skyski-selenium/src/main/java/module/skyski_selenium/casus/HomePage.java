package module.skyski_selenium.casus;

import module.skyski_selenium.basic.BasicTestCase;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomePage extends BasicTestCase
{
	@Test
    public void goToHomePage_ValidUrl_Success() 
	{
		super.getWebDriver().get("http://localhost:8080/skyski");
		super.titleAssertion("SKYski Project", "Load home page");
		super.resetScreen();
		super.adjustScreen();
	}
	
	@Test
    public void changeLanguageOnHomePage_PolishRussianEnglish_Success() 
	{
		super.getWebDriver().get("http://localhost:8080/skyski");
		super.titleAssertion("SKYski Project", "Load home page");
		super.resetScreen();
		super.adjustScreen();		
		super.findElementByCss("form > a:nth-of-type(2)").click();
		super.titleAssertion("Projekt SKYski", "Load home page in polish");
		super.findElementByCss("form > a:last-of-type").click();
		super.titleAssertion("Проект SKYski", "Load home page in russian");
		super.findElementByCss("form > a:first-of-type").click();
		super.titleAssertion("SKYski Project", "Load home page in english");
	}
}