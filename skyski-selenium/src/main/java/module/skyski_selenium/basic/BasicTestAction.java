package module.skyski_selenium.basic;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public abstract class BasicTestAction extends BasicTestConfig
{
	public BasicTestAction()
	{ 
		super();
		if (super.webDrivers[0] != null) 
			this.currentWebDriver = super.webDrivers[0];
		this.currentStage = 
			super.config.getStage().contains("|") ? 
			super.config.getStage().split("|")[0] :
			super.config.getStage();
	}
	
	public void toSystemOut(String _text)
	{ 
		if (super.config.isDebug())
			System.out.println(
				"[DEBUG]" + ((_text.startsWith("[") ? "" : " ") + _text)
			);
	}
	
	public void setUpTimeout(int _seconds)
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
    
    public String getStage() { return this.currentStage; }
    
    public void nextStage()
    {
    	if (super.config.getStage().contains("|")) 
    	{
    		String[] stages = super.config.getStage().split("|");
    		for (int stageCounter = 0; stageCounter < stages.length; stageCounter++)
    		{
    			if 
    			(
					stages[stageCounter].equals(this.currentStage) &&
						stageCounter != stages.length - 1
				)
    			{
    				this.currentStage = stages[stageCounter + 1];
    				break;
    			}
    		}
    	}
    }
    
    public WebElement findElementBy(By _by)
    { return this.getWebDriver().findElement(_by); }
    
    public WebElement findElementByXpath(String _xpathSelector)
    { return this.findElementBy(By.xpath(_xpathSelector)); }
    
    public WebElement findElementByCss(String _cssSelector)
    { return this.findElementBy(By.cssSelector(_cssSelector)); }
    
    public boolean retryingFindClickElementBy(By _by)
    {
        boolean result = false;
        int attempt = 0;
        while (attempt < 5)
        {
            try 
            { 
            	this.getWebDriver().findElement(_by).click();
                result = true;
                break;
            } 
            catch (StaleElementReferenceException sere) { this.toSystemOut("[STALE] " + sere.getMessage()); }
            catch (WebDriverException wde) { this.toSystemOut("[WEBDRIVER] " + wde.getMessage()); }
            attempt++;
        }
        return result;
    }
    
    public String retryingFindTextElementBy(By _by)
    {
    	String result = null;
        int attempt = 0;
        while (attempt < 5)
        {
            try 
            { 
            	result = this.getWebDriver().findElement(_by).getText();
                break;
            } 
            catch (StaleElementReferenceException sere) { this.toSystemOut("[STALE] " + sere.getMessage()); }
            catch (WebDriverException wde) { this.toSystemOut("[WEBDRIVER] " + wde.getMessage()); }
            attempt++;
        }
        return result;
    }
    
    public String retryingFindTextElementByXpath(String _xpathSelector)
    { return this.retryingFindTextElementBy(By.xpath(_xpathSelector)); }
    
    public String retryingFindTextElementByCss(String _cssSelector)
    { return this.retryingFindTextElementBy(By.cssSelector(_cssSelector)); }
    
    public boolean retryingFindClickElementByXpath(String _xpathSelector)
    { return this.retryingFindClickElementBy(By.xpath(_xpathSelector)); }
    
    public boolean retryingFindClickElementByCss(String _cssSelector)
    { return this.retryingFindClickElementBy(By.cssSelector(_cssSelector)); }
    
    public void domClick(WebElement _element)
    {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor)this.getWebDriver();
        javascriptExecutor.executeScript("arguments[0].click();", _element);
    }

    public void hoverClick(WebElement _element)
    {
        Actions actions = new Actions(this.getWebDriver());
        actions.moveToElement(_element).click().build().perform();
    }
    
    public void htmlChordKeySequence(int _counter, CharSequence... _charSequences)
    {
    	WebElement html = null;
    	for (int i = 0; i < _counter; i++)
    	{
	        int attempt = 0;
	        while (attempt < 5)
	        {
	            try 
	            { 
	            	html = this.waitUntil(ExpectedConditions.presenceOfElementLocated(By.tagName("html")));
	            	html.sendKeys(Keys.chord(_charSequences));
	                break;
	            } 
	            catch (StaleElementReferenceException sere) { this.toSystemOut("[STALE] " + sere.getMessage()); }
	            catch (WebDriverException wde) { this.toSystemOut("[WEBDRIVER] " + wde.getMessage()); }
	            attempt++;
	        }
    	}
	}
    
    public void adjustScreen()
    { this.htmlChordKeySequence(4, Keys.CONTROL, Keys.SUBTRACT); }
    
    public void resetScreen()
    { this.htmlChordKeySequence(1, Keys.CONTROL, "0"); }
    
    public void titleAssertion(String _title, String _text)
    {
        try
        {
        	this.waitUntil(ExpectedConditions.titleIs(_title));
        }
        catch (WebDriverException wde)
        {
        	this.toSystemOut("FAILURE " + _text);
            fail("FAILURE " + _text);
        }
    }
    
    public void titleNotAssertion(String _title, String _text)
    {
    	try
        {
    		this.waitUntil(ExpectedConditions.titleIs(_title));
    		this.toSystemOut("FAILURE " + _text);
    		fail("FAILURE " + _text);
        }
    	catch (TimeoutException te) { this.toSystemOut("SUCCESS " + _text); }
    	catch (WebDriverException wde)
    	{
    		this.toSystemOut("FAILURE " + _text);
    		fail("FAILURE " + _text);
    	}
    }
    
    public <V> V waitUntil(Function<? super WebDriver, V> _isTrue)
    {
    	return new WebDriverWait(this.getWebDriver(), super.config.getTimeout()).until(_isTrue);
    }
    
	public void sortAssertion(
		String _sortButton, String _firstElement, String _secondElement, boolean _isPreSorted
	)
	{
		String firstTextBeforeSort;
		String lastTextBeforeSort;
		if (!_isPreSorted)
		{
			firstTextBeforeSort = this.retryingFindTextElementByCss(_firstElement);
			this.retryingFindClickElementByCss(_sortButton);
			this.waitUntil(ExpectedConditions.invisibilityOfElementWithText(
				By.cssSelector(_firstElement), firstTextBeforeSort)
			);
		}
		firstTextBeforeSort = this.retryingFindTextElementByCss(_firstElement);
		lastTextBeforeSort = this.retryingFindTextElementByCss(_secondElement);
		this.retryingFindClickElementByCss(_sortButton);
		this.waitUntil(ExpectedConditions.invisibilityOfElementWithText(
			By.cssSelector(_firstElement), firstTextBeforeSort
		));
		String firstTextAfterSort = this.retryingFindTextElementByCss(_firstElement);
		String lastTextAfterSort = this.retryingFindTextElementByCss(_secondElement);
		assertEquals(firstTextBeforeSort, lastTextAfterSort);
		assertEquals(lastTextBeforeSort, firstTextAfterSort);
	}
	
	public void elementPresentAssertionByCss(String _cssSelector, String _text)
	{
		this.elementPresentAssertion(By.cssSelector(_cssSelector), _text);
	}
	
	public void elementAbsentAssertionByCss(String _cssSelector, String _text)
	{
		this.elementPresentAssertion(By.cssSelector(_cssSelector), _text);
	}
	
	public void elementPresentAssertion(By _by, String _text)
	{
		try
		{
			this.waitUntil(ExpectedConditions.presenceOfElementLocated(_by));
			this.logAssertionSuccess(_text);
        }
		catch (TimeoutException te)
		{
            logAssertionFailure(_text);
            fail("FAILURE " + _text);
        }
	}
	
	public void elementAbsentAssertion(String _text, By _by)
	{
		try
		{
			this.waitUntil(ExpectedConditions.invisibilityOfElementLocated(_by));
			this.logAssertionSuccess(_text);
        }
		catch (TimeoutException te)
		{
            logAssertionFailure(_text);
            fail("FAILURE " + _text);
        }
	}
	
	public void logAssertion(String _text, boolean _isSuccess)
	{
		String status;
		if (_isSuccess) status = "success";
		else status = "failure";
		this.toSystemOut("assertion " + status + ": " + _text);
	}
	
	public void logAssertionFailure(String _text)
	{
		this.logAssertion(_text, false);
    }

    public void logAssertionSuccess(String _text)
    {
    	this.logAssertion(_text, true);
    }

    private WebDriver currentWebDriver;
    private String currentStage;
}
