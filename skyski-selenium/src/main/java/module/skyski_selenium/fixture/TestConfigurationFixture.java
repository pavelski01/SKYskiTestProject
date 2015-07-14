package module.skyski_selenium.fixture;

import module.skyski_selenium.config.ConfigurationSingleton;

public abstract class TestConfigurationFixture
{
	public TestConfigurationFixture()
	{ 
		this.configuration = 
			ConfigurationSingleton.getSingletonInstance();
	}
	
	public ConfigurationSingleton getConfiguration() { return this.configuration; }
	
	private ConfigurationSingleton configuration;
}