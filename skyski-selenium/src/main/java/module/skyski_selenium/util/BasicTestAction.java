package module.skyski_selenium.util;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;

public class BasicTestAction extends BasicTestConfig
{
	public BasicTestAction()
	{ 
		super();
		if (super.webDrivers[0] != null) 
			this.currentWebDriver = super.webDrivers[0];
	}
	
	public void toSystemOut(String _text)
	{ 
		if (super.config.isDebug()) 
			System.out.println("[DEBUG] " + _text); 
	}
	
	public void setupTimeout(int _seconds)
	{
		this.currentWebDriver.manage().timeouts().implicitlyWait(_seconds, TimeUnit.SECONDS);
    }

    public void resetTimeout()
    {
		this.currentWebDriver.manage().timeouts().implicitlyWait(
			super.config.getTimeout(), TimeUnit.SECONDS
		);
    }
    
    public WebDriver getWebDriver() { return this.currentWebDriver; }
    
    public void nextWebDriver()
    {
    	if (this.currentWebDriver != null)
	    	for (int i = 0; i < super.webDrivers.length; i++)
	    		if 
	    		(
    				super.webDrivers[i] == this.currentWebDriver 
    					&& i != super.webDrivers.length - 1
				)
	    		{
	    			this.currentWebDriver = super.webDrivers[i + 1];
	    			break;
	    		}
    				
    }
    
    private WebDriver currentWebDriver;
}
