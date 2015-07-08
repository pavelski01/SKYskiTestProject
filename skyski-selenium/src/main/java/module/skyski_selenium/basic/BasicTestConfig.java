package module.skyski_selenium.basic;

import module.skyski_selenium.config.ConfigurationSingleton;

public abstract class BasicTestConfig
{
	public BasicTestConfig()
	{ 
		this.config = 
			ConfigurationSingleton.getSingletonInstance();
	}
	
	protected ConfigurationSingleton config;
}