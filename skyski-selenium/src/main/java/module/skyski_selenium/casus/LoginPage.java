package module.skyski_selenium.casus;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import module.skyski_selenium.basic.BasicTestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginPage extends BasicTestCase
{
	@Test
    public void goToLoginPage_ValidUrl_Success() 
    {
		super.resetScreen();
		super.adjustScreen();
		super.retryingFindClickElementByCss("#logging");
		super.titleNotAssertion("SKYski Project", "Not load home page");
		super.titleAssertion("SKYski Logging", "Load sign in page");
    }
}