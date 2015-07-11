package module.skyski_selenium.basic;

import module.skyski_selenium.config.ConfigurationSingleton;

public abstract class BasicTestConfig
{
	public BasicTestConfig()
	{ 
		this.configuration = 
			ConfigurationSingleton.getSingletonInstance();
	}
	
	public ConfigurationSingleton getConfiguration() { return this.configuration; }
	
	private ConfigurationSingleton configuration;}