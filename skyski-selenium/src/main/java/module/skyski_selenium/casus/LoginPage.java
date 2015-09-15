package module.skyski_selenium.casus;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import module.skyski_selenium.fixture.TestCaseFixture;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class LoginPage extends TestCaseFixture
{
	@Test
    public void loginPageShouldLoadAndUnload_ValidUrl_Success() 
    {		
		super.resetScreen();
		super.adjustScreen();
		super.retryingFindClickElementByCss("#logging");
		super.titleNotAssertion("SKYski Project", "Unload home page");
		super.titleAssertion("SKYski Logging", "Load sign in page");
		super.retryingFindClickElementByCss(".container > a:first-of-type");
		super.titleNotAssertion("SKYski Logging", "Unload sign in page");
		super.titleAssertion("SKYski Project", "Load home page");
    }
	
	@Test
    public void loginPageShouldChangeLanguage_PolishRussianEnglish_Success() 
    {		
		super.resetScreen();
		super.adjustScreen();
		super.retryingFindClickElementByCss("#logging");
		super.titleNotAssertion("SKYski Project", "Unload home page");
		super.titleAssertion("SKYski Logging", "Load sign in page");
		super.retryingFindClickElementByCss("form > a:nth-of-type(2)");
		super.titleNotAssertion("SKYski Logging", "Unload sign in page");
		super.titleAssertion("Logowanie SKYski", "Load sign in page in polish");
		super.retryingFindClickElementByCss("form > a:last-of-type");
		super.titleNotAssertion("Logowanie SKYski", "Unload sign in page in polish");
		super.titleAssertion("Лесозаготовки SKYski", "Load sign in page in russian");
		super.retryingFindClickElementByCss("form > a:first-of-type");
		super.titleNotAssertion("Лесозаготовки SKYski", "Unload sign in page in russian");
		super.titleAssertion("SKYski Logging", "Load sign in page in english");
    }
}