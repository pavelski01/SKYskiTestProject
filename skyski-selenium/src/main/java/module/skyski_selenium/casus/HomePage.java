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
		super.retryingFindClickElementByCss("form > a:nth-of-type(2)");
		super.titleAssertion("Projekt SKYski", "Load home page in polish");
		super.retryingFindClickElementByCss("form > a:last-of-type");
		super.titleAssertion("Проект SKYski", "Load home page in russian");
		super.retryingFindClickElementByCss("form > a:first-of-type");
		super.titleAssertion("SKYski Project", "Load home page in english");
	}
	
	@Test
    public void goToBibliographyPage_ValidUrl_Success() 
    {
		super.getWebDriver().get("http://localhost:8080/skyski");
		super.titleAssertion("SKYski Project", "Load home page");
		super.resetScreen();
		super.adjustScreen();
		super.retryingFindClickElementByCss("#bibliography");
		super.titleAssertion("SKYski Bibliography", "Load bibliography page");
		super.retryingFindClickElementByCss("form tfoot > tr > td > a");
		super.titleAssertion("SKYski Project", "Load home page");
		super.resetScreen();
		super.adjustScreen();
		/*super.findElementByCss("form > a:nth-of-type(2)").click();
		super.titleAssertion("Bibliografia SKYski", "Load home bibliography in polish");
		super.findElementByCss("form > a:last-of-type").click();
		super.titleAssertion("Библиография SKYski", "Load home bibliography in russian");
		super.findElementByCss("form > a:first-of-type").click();
		super.titleAssertion("SKYski Bibliography", "Load home bibliography in english");*/
    }
}