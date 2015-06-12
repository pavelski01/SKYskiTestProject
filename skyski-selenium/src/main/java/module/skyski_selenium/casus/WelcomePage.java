package module.skyski_selenium.casus;

import module.skyski_selenium.basic.BasicTestCase;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WelcomePage extends BasicTestCase
{
	@Test
    public void goToWelcomPage_ValidUrl_Success()
	{
		super.getWebDriver().get("http://localhost:8080/skyski");
	}
}