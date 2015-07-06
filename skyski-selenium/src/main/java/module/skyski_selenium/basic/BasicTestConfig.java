package module.skyski_selenium.basic;

import org.openqa.selenium.WebDriver;

import module.skyski_selenium.config.core.ConfigurationSingleton;

public abstract class BasicTestConfig
{
	public BasicTestConfig()
	{
		this.config = ConfigurationSingleton.getSingletonInstance();
		this.webDrivers = ConfigurationSingleton.getDrivers();
	}
	
	protected ConfigurationSingleton config;
	protected WebDriver[] webDrivers;
}