package module.skyski_selenium.casus;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import module.skyski_selenium.fixture.TestCaseFixture;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomePage extends TestCaseFixture
{
	@Test
    public void homePageShouldLoad_ValidUrl_Success() 
	{
		super.resetScreen();
		super.adjustScreen();		
	}
	
	@Test
    public void HomePageShouldChangeLanguage_PolishRussianEnglish_Success() 
	{
		super.resetScreen();
		super.adjustScreen();
		super.retryingFindClickElementByCss("form > a:nth-of-type(2)");
		super.titleNotAssertion("SKYski Project", "Unload home page");
		super.titleAssertion("Projekt SKYski", "Load home page in polish");
		super.retryingFindClickElementByCss("form > a:last-of-type");
		super.titleNotAssertion("Projekt SKYski", "Unload home page in polish");
		super.titleAssertion("Проект SKYski", "Load home page in russian");
		super.retryingFindClickElementByCss("form > a:first-of-type");
		super.titleNotAssertion("Проект SKYski", "Unload home page in russian");
		super.titleAssertion("SKYski Project", "Load home page in english");
	}
}