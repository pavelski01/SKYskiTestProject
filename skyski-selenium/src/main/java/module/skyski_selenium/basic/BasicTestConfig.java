package module.skyski_selenium.basic;

import org.openqa.selenium.WebDriver;
import module.skyski_selenium.config.Configuration;

public class BasicTestConfig
{
	public BasicTestConfig()
	{
		this.config = Configuration.getSingletonInstance();
		this.webDrivers = Configuration.getDrivers();
	}
	
	protected Configuration config;
	protected WebDriver[] webDrivers;
}