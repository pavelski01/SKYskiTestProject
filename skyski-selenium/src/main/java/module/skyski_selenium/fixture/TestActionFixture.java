package module.skyski_selenium.fixture;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import module.skyski_selenium.config.ConfigurationSingleton;

public abstract class TestActionFixture
{    
    public WebElement findElementBy(By _by)
    {
    	return ConfigurationSingleton.getSingletonInstance().getWebDriverDetails().
			getWebDriver().findElement(_by);
	}
    
    public WebElement findElementByXpath(String _xpathSelector)
    { return this.findElementBy(By.xpath(_xpathSelector)); }
    
    public WebElement findElementByCss(String _cssSelector)
    { return this.findElementBy(By.cssSelector(_cssSelector)); }
    
    public String findRegex(String _source, String _regex)
    {
    	Pattern aPattern = Pattern.compile(_regex);
    	Matcher aMatcher = aPattern.matcher(_source.replace("\n", " "));
    	if (aMatcher.find()) return aMatcher.group();
    	else return _source;
    }
    
    public boolean retryingFindClickElementBy(By _by)
    {
        boolean result = false;
        int attempt = 0;
        while (attempt < 5)
        {
            try 
            {
            	ConfigurationSingleton.getSingletonInstance().getWebDriverDetails().
            		getWebDriver().findElement(_by).click();
                result = true;
                ConfigurationSingleton.getSingletonInstance().toSystemOut(
            		"[TEST][SUCCESS] Click element identified by " + _by.toString()
        		);
                break;
            }
            catch (StaleElementReferenceException _sere) 
            {
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][STALE] " + this.findRegex(_sere.getMessage(), this.errorRegex));
            	}
            catch (WebDriverException _wde)
            { 
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][MALFUNCTION] " + this.findRegex(_wde.getMessage(), this.errorRegex)
    			);
        	}
            catch (Exception _e)
            {
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][FAULT] " + this.findRegex(_e.getMessage(), this.errorRegex)
    			);
            }
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
            	result = 
        			ConfigurationSingleton.getSingletonInstance().getWebDriverDetails().
        				getWebDriver().findElement(_by).getText();
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][SUCCESS] Find element identified by " + _by.toString()
    			);
                break;
            } 
            catch (StaleElementReferenceException _sere) 
            { 
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][STALE] " + this.findRegex(_sere.getMessage(), this.errorRegex)
    			);
        	}
            catch (WebDriverException _wde) 
            { 
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][MALFUNCTION] " + this.findRegex(_wde.getMessage(), this.errorRegex)
    			);
        	}
            catch (Exception _e)
            {
            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
        			"[TEST][FAULT] " + this.findRegex(_e.getMessage(), this.errorRegex)
    			);
            }
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
        JavascriptExecutor javascriptExecutor = 
    		(JavascriptExecutor)ConfigurationSingleton.getSingletonInstance().
    			getWebDriverDetails().getWebDriver();
        javascriptExecutor.executeScript("arguments[0].click();", _element);
    }
    
    public void htmlChordKeySequence(String _message, int _counter, CharSequence... _charSequences)
    {
    	WebElement firstDivision = null;
    	for (int i = 0; i < _counter; i++)
    	{
	        int attempt = 0;
	        while (attempt < 5)
	        {
	            try 
	            {
	            	firstDivision = 
            			this.waitUntil(
        					ExpectedConditions.presenceOfElementLocated(By.tagName("div"))
    					);
	            	TestActionFixture.actions.moveToElement(firstDivision);
	            	TestActionFixture.actions.click();
	            	TestActionFixture.actions.sendKeys(Keys.chord(_charSequences));
	            	TestActionFixture.executeActions();
	                break;
	            } 
	            catch (StaleElementReferenceException _sere) 
	            { 
	            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
            			"[TEST][STALE] " + this.findRegex(_sere.getMessage(), this.errorRegex)
        			);
            	}
	            catch (WebDriverException _wde) 
	            { 
	            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
            			"[TEST][MALFUNCTION] " + this.findRegex(_wde.getMessage(), this.errorRegex)
        			);
            	}
	            catch (Exception _e)
	            {
	            	ConfigurationSingleton.getSingletonInstance().toSystemOut(
	        			"[TEST][FAULT] " + this.findRegex(_e.getMessage(), this.errorRegex)
	    			);
	            }
	            attempt++;
	        }
    	}
    	ConfigurationSingleton.getSingletonInstance().toSystemOut(
			"[TEST][SUCCESS] " + _message
		);
	}
    
    public void adjustScreen()
    { this.htmlChordKeySequence("Adjust screen", 4, Keys.CONTROL, Keys.SUBTRACT); }
    
    public void resetScreen()
    { this.htmlChordKeySequence("Reset screen", 1, Keys.CONTROL, "0"); }
    
    public void titleAssertion(String _title, String _text)
    {
        try
        {
        	this.waitUntil(ExpectedConditions.titleIs(_title));
        	ConfigurationSingleton.getSingletonInstance().toSystemOut("[TEST][SUCCESS] " + _text);
        }
        catch (WebDriverException _wde)
        {
        	ConfigurationSingleton.getSingletonInstance().toSystemOut("[TEST][FAILURE] " + _text);
            fail("[TEST][FAILURE] " + _text);
        }
    }
    
    public void titleNotAssertion(String _title, String _text)
    {
    	try
        {
    		this.waitUntil(ExpectedConditions.not(ExpectedConditions.titleIs(_title)));
    		ConfigurationSingleton.getSingletonInstance().toSystemOut("[TEST][SUCCESS] " + _text);
        }
    	catch (WebDriverException _wde)
    	{
    		ConfigurationSingleton.getSingletonInstance().toSystemOut("[TEST][FAILURE] " + _text);
    		fail("[TEST][FAILURE] " + _text);
    	}
    }
    
    public <V> V waitUntil(Function<? super WebDriver, V> _isTrue)
    { 
    	return new WebDriverWait(
			ConfigurationSingleton.getSingletonInstance().getWebDriverDetails().getWebDriver(), 
			ConfigurationSingleton.getSingletonInstance().getTimeout()
		).until(_isTrue);
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
		ConfigurationSingleton.getSingletonInstance().toSystemOut(
			"[TEST][SUCCESS] Correct sorting - before: first->" + firstTextBeforeSort.substring(0, 10) +
				((firstTextBeforeSort.length() > 10) ? "..." : "") + 
					" last->" + lastTextBeforeSort.substring(0, 10) + 
						((lastTextBeforeSort.length() > 10) ? "..." : "") +
						" - after: first->" + firstTextAfterSort.substring(0, 10) +
					((firstTextAfterSort.length() > 10) ? "..." : "") +
				" last->" + lastTextAfterSort.substring(0, 10) +
			((lastTextAfterSort.length() > 10) ? "..." : "")
		);
	}
	
	public void elementPresentAssertionByCss(String _cssSelector, String _text)
	{ this.elementPresentAssertion(By.cssSelector(_cssSelector), _text); }
	
	public void elementAbsentAssertionByCss(String _cssSelector, String _text)
	{ this.elementPresentAssertion(By.cssSelector(_cssSelector), _text); }
	
	public void elementPresentAssertion(By _by, String _text)
	{
		try
		{
			this.waitUntil(ExpectedConditions.presenceOfElementLocated(_by));
			this.logAssertionSuccess(_text);
        }
		catch (TimeoutException _te)
		{
            logAssertionFailure(_text);
            fail("[TEST][FAILURE] " + _text);
        }
	}
	
	public void elementAbsentAssertion(String _text, By _by)
	{
		try
		{
			this.waitUntil(ExpectedConditions.invisibilityOfElementLocated(_by));
			this.logAssertionSuccess(_text);
        }
		catch (TimeoutException _te)
		{
            logAssertionFailure(_text);
            fail("[TEST][FAILURE] " + _text);
        }
	}
	
	public void logAssertion(String _text, boolean _isSuccess)
	{
		String status;
		if (_isSuccess) status = "success";
		else status = "failure";
		ConfigurationSingleton.getSingletonInstance().toSystemOut(
			"assertion " + status + ": " + _text
		);
	}
	
	public void logAssertionFailure(String _text)
	{ this.logAssertion(_text, false); }

    public void logAssertionSuccess(String _text)
    { this.logAssertion(_text, true); }
    
    private static void executeActions()
    {
    	TestActionFixture.actions.build().perform();
    	TestActionFixture.actions = new Actions(
			ConfigurationSingleton.getSingletonInstance().
				getWebDriverDetails().getWebDriver()
		);
    }
    
    private static Actions actions = new Actions(
		ConfigurationSingleton.getSingletonInstance().
			getWebDriverDetails().getWebDriver()
	);
    private final String errorRegex = "(.+?(?=Command duration or timeout))|(.+?(?=Build info))";
}