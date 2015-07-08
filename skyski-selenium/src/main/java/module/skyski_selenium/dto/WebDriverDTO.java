package module.skyski_selenium.dto;

import org.openqa.selenium.WebDriver;

public class WebDriverDTO
{	
	public String getBrowser() { return this.browser; }
	public WebDriver getWebDriver() { return this.webDriver; }
	public void setBrowser(String _browser) { this.browser = _browser; }
	public void setWebDriver(WebDriver _webDriver) { this.webDriver = _webDriver; }
	
	private String browser;
	private WebDriver webDriver;
}