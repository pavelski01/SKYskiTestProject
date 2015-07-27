package module.skyski_selenium.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import module.skyski_selenium.dto.StageDataDTO;
import module.skyski_selenium.dto.WebDriverDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public final class ConfigurationSingleton
{
    /* CONSTRUCTOR */
    public ConfigurationSingleton()
    {
        final String
            BROWSER_KEY = "browser", DEBUG_KEY = "debug",
            STAGE_KEY = "stage", TIMEOUT_KEY = "timeout";
        Properties properties = this.getProperties();
        String browser = properties.getProperty(BROWSER_KEY);
        if (browser == null) browser = ConfigurationSingleton.EMPTY;
        else this.analyzeBrowser(browser);
        this.debug = Boolean.parseBoolean(properties.getProperty(DEBUG_KEY));
        this.timeout = Integer.parseInt(properties.getProperty(TIMEOUT_KEY));
        String stage = properties.getProperty(STAGE_KEY);
        if (stage == null) stage = ConfigurationSingleton.EMPTY;
        else this.analyzeStage(stage, properties);
		if (this.getStagesData().get(0).getStage() != null)
			this.stageDetails = this.getStagesData().get(0);
    }

    public static ConfigurationSingleton getSingletonInstance()
    {
        if (ConfigurationSingleton.singletonInstance == null) 
        	ConfigurationSingleton.singletonInstance = new ConfigurationSingleton();
        return ConfigurationSingleton.singletonInstance;
    }
    
    public static String getStatusToString(boolean _status)
    {
        if (_status) return ConfigurationSingleton.SUCCESS;
        else return ConfigurationSingleton.FAILURE;
    }
    
    public void toSystemOut(String _text)
	{
		if (this.isDebug())
			System.out.println(
				"[DEBUG]" + ((_text.startsWith("[") ? "" : " ") + _text)
			);
	}
    
    public void automaticBasicAccessAuthentication()
    {
    	new Thread(new LoginWindow()).start();
		ConfigurationSingleton.getSingletonInstance().getWebDriverDetails().getWebDriver().get(
			ConfigurationSingleton.getSingletonInstance().getStageDetails().getAppUrl()
		);
    }
    
    public void webDriverCreate(String _webDriverString)
    {
    	String currentWebDriver = null;
    	if (_webDriverString.equals("chrome"))
    	{
    		this.webDriverDetails = new WebDriverDTO();
    		this.webDriverDetails.setBrowser(currentWebDriver = "chrome");
    		this.webDriverDetails.setWebDriver(this.getChromeWebDriverInstance());
    	}
    	else if (_webDriverString.equals("firefox"))
    	{
    		this.webDriverDetails = new WebDriverDTO();
    		this.webDriverDetails.setBrowser(currentWebDriver = "firefox");
    		this.webDriverDetails.setWebDriver(this.getFirefoxWebDriverInstance());
    	}
    	this.toSystemOut(
			"[WEBDRIVER][CREATE] " +
				currentWebDriver.substring(0, 1).toUpperCase() +
					currentWebDriver.substring(
						1, currentWebDriver.length()
					) + " create"
		);
    }
    
    public void webDriverQuit()
    {
    	this.getWebDriverDetails().getWebDriver().quit();
    	this.toSystemOut(
			"[WEBDRIVER][QUIT] " +
				this.getWebDriverDetails().getBrowser().substring(0, 1).toUpperCase() +
					this.getWebDriverDetails().getBrowser().substring(
						1, this.getWebDriverDetails().getBrowser().length()
					) + " quit"
		);
	}

    private void analyzeStage(String _stage, Properties _properties)
    {
        if (_stage.equals(ConfigurationSingleton.EMPTY)) return;
        final String DELIMITER = "|";
        final char D_CASE = 'd', P_CASE = 'p', T_CASE = 't';
        final String DEV_STAGE = "dev", PROD_STAGE = "prod", TEST_STAGE = "test";
        StringTokenizer stageStringTokenizer = new StringTokenizer(_stage, DELIMITER);
        String token;
        this.stagesData = new ArrayList<StageDataDTO>();
        while (stageStringTokenizer.hasMoreTokens())
        {
            token = stageStringTokenizer.nextToken().toLowerCase();
            switch (token.charAt(0))
            {
                case D_CASE:
                    this.initializeStage(DEV_STAGE, _properties);
                    break;
                case P_CASE:
                    this.initializeStage(PROD_STAGE, _properties);
                    break;
                case T_CASE:
                    this.initializeStage(TEST_STAGE, _properties);
                    break;
            }
        }
    }

    private void analyzeBrowser(String _browser)
    {
        if (_browser.equals(ConfigurationSingleton.EMPTY)) return;
        final String DELIMITER = "|";
        final char C_CASE = 'c', F_CASE = 'f';
        StringTokenizer browserStringTokenizer = new StringTokenizer(_browser, DELIMITER);
        String token;        
        ArrayList<String> webDrivers = new ArrayList<String>();
        while (browserStringTokenizer.hasMoreTokens())
        {
            token = browserStringTokenizer.nextToken().toLowerCase();
            switch (token.charAt(0))
            {
                case C_CASE:
                    webDrivers.add(new String("chrome"));                
                    break;
                case F_CASE:
                    webDrivers.add(new String("firefox"));                
                    break;
            }
        }
        this.webDrivers = webDrivers;
    }

    private WebDriver getChromeWebDriverInstance()
    {
    	final String
	    	FILE_SEPARATOR = System.getProperty("file.separator"),
	        DRIVER_NAME = "chromedriver",
	        LOG_NAME = "chromedriver.log",
			DRIVER_PATH = 
	        	ConfigurationSingleton.class.getProtectionDomain().
	    			getCodeSource().getLocation().getPath() + FILE_SEPARATOR + 
	    				"module" + FILE_SEPARATOR + "skyski_selenium" + FILE_SEPARATOR + 
	    					"config";
    	final String 
    		pathToChromeBin = DRIVER_PATH + FILE_SEPARATOR + DRIVER_NAME,
			pathToChromeLog = DRIVER_PATH + FILE_SEPARATOR + LOG_NAME;
    	//if (pathToChrome != null && !pathToChrome.equals(""))
    	//{
	    	System.setProperty("webdriver.chrome.driver", pathToChromeBin);
	    	System.setProperty("webdriver.chrome.logfile", pathToChromeLog);
    	//}
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
    		new String[] 
			{
				"disable-extensions",
				"ignore-certificate-errors",
				"no-sandbox",
				"safebrowsing-disable-extension-blacklist",
				"start-maximized"
			}
		);
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        ChromeDriver chromeDriver = new ChromeDriver(desiredCapabilities);
        chromeDriver.manage().timeouts().implicitlyWait(this.getTimeout(), TimeUnit.SECONDS);
        chromeDriver.manage().window().maximize();
        return chromeDriver;
    }

    private WebDriver getFirefoxWebDriverInstance()
    {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.private.browsing.autostart", true);
        firefoxProfile.setPreference("network.http.phishy-userpass-length", 255);
        firefoxProfile.setPreference("network.automatic-ntlm-auth.trusted-uris", "localhost");
        firefoxProfile.setPreference("network.negotiate-auth.trusteduris", "localhost");
        firefoxProfile.setPreference("signon.autologin.proxy", true);
        firefoxProfile.setPreference("webdriver.load.strategy", "unstable");        
        FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxProfile);
        firefoxDriver.manage().timeouts().implicitlyWait(this.getTimeout(), TimeUnit.SECONDS);
        firefoxDriver.manage().window().maximize();
        return firefoxDriver;
    }

    private void initializeStage(String _stagePrefix, Properties _properties)
    {
    	StageDataDTO stageDataTransport = new StageDataDTO();
    	stageDataTransport.setStage(_stagePrefix);
        String coreKey1 = _stagePrefix + "AppURL";
        String coreKey2 = _stagePrefix + "BasicCredentialUser";
        String coreKey3 = _stagePrefix + "BasicCredentialPassword";
        String coreValue1 = _properties.getProperty(coreKey1);
        String coreValue2 = _properties.getProperty(coreKey2);
        String coreValue3 = _properties.getProperty(coreKey3);
        if
        (
            coreValue1 != null && !coreValue1.equals(ConfigurationSingleton.EMPTY) &&
                coreValue2 != null && !coreValue2.equals(ConfigurationSingleton.EMPTY) &&
                	coreValue3 != null && !coreValue3.equals(ConfigurationSingleton.EMPTY)
        )
        {
        	stageDataTransport.setAppUrl(coreValue1);
        	stageDataTransport.setBasicCredentialUser(coreValue2);
        	stageDataTransport.setBasicCredentialPassword(coreValue3);
            final String[] realFake = { "Real", "Fake" };
            final String[] data =
            {
            	"Login", "Password", "Email", "Forename", "Surname",
                "StreetAddress", "PostalCity", "PostalCode", "Phone"
            };
            String temporaryKey, temporaryValue;
            for (String state : realFake)
                for (String record : data)
                {
                    temporaryKey = _stagePrefix + state + record;
                    temporaryValue = _properties.getProperty(temporaryKey);
                    temporaryKey = state.toLowerCase() + record;
                    if (temporaryValue == null) temporaryValue = ConfigurationSingleton.EMPTY;
                    this.setUpReflectionData(temporaryKey, temporaryValue, stageDataTransport);
                }
        }
    	this.stagesData.add(stageDataTransport);
    }

    private void setUpReflectionData(String _temporaryKey, String _temporaryValue, Object _ob)
    {
    	Class<? extends Object> cl = _ob.getClass();
    	Field field = null;
		try 
		{
			field = cl.getDeclaredField(_temporaryKey);
			field.setAccessible(true);
			field.set(_ob, _temporaryValue);
			field.setAccessible(false);
		}
		catch (IllegalAccessException _iae)
		{
			final String ILLEGAL_ACCESS = "Illegal access:";			
			this.toSystemOut(
    			"[CONFIGURATION][FAULT] " + 
					ILLEGAL_ACCESS + 
						ConfigurationSingleton.GAP + 
					_iae.toString()
			);
		}
		catch (IllegalArgumentException _iae)
		{
			final String ILLEGAL_ARGUMENT = "Illegal argument:";
            this.toSystemOut(
    			"[CONFIGURATION][FAULT] " + 
					ILLEGAL_ARGUMENT + 
						ConfigurationSingleton.GAP + 
					_iae.toString()
			);
		} 
		catch (NoSuchFieldException _nsfe) 
		{ 
			final String NO_FIELD = "No field:";
            this.toSystemOut(
    			"[CONFIGURATION][FAULT] " + 
					NO_FIELD + 
						ConfigurationSingleton.GAP + 
					_nsfe.toString()
			);
		} 
		catch (SecurityException _se) 
		{ 
			final String SECURITY = "Security:";            
            this.toSystemOut(
    			"[CONFIGURATION][FAULT] " + 
					SECURITY + 
						ConfigurationSingleton.GAP + 
					_se.toString()
			);
		}
    }

    private Properties getProperties()
    {
        final String
        	FILE_SEPARATOR = System.getProperty("file.separator"),
            PROPERTIES_NAME = "configuration.properties",
            PROPERTIES_PATH = 
            	ConfigurationSingleton.class.getProtectionDomain().
        			getCodeSource().getLocation().getPath() + FILE_SEPARATOR + 
        				"module" + FILE_SEPARATOR + "skyski_selenium" + FILE_SEPARATOR + 
        					"config";
        Properties properties = new Properties();
        InputStream inputStream = null;
        try
        {
            inputStream = 
        		new FileInputStream(
    				PROPERTIES_PATH + FILE_SEPARATOR + PROPERTIES_NAME
				);
            properties.load(inputStream);
        }
        catch (IOException _ioe1)
        {
            final String
                PROPERTIES_ERROR_MSG1 = "Problem with load",
                PROPERTIES_ERROR_MSG2 = "configuration file:";
            System.out.println(
                PROPERTIES_ERROR_MSG1 + ConfigurationSingleton.GAP + 
                	PROPERTIES_NAME + ConfigurationSingleton.GAP +
                		PROPERTIES_ERROR_MSG2 + ConfigurationSingleton.GAP + _ioe1.toString()
            );
            if (inputStream != null)
            {
                try { inputStream.close(); }
                catch (IOException _ioe2)
                {
                    final String STREAM_ERROR_MSG = "Problem with close input stream:";
                    System.out.println(STREAM_ERROR_MSG + ConfigurationSingleton.GAP + _ioe2.toString());
                }
            }
        }
        return properties;
    }

    /* GETTERS */
    public boolean isDebug() { return this.debug; }
    public int getTimeout() { return this.timeout; }   
    public StageDataDTO getStageDetails() { return this.stageDetails; }
    public WebDriverDTO getWebDriverDetails() { return this.webDriverDetails; }
    public ArrayList<String> getWebDrivers() { return this.webDrivers; }
    public ArrayList<StageDataDTO> getStagesData() { return this.stagesData; }
    
    /* SETTERS */
    public void setStageDetails(StageDataDTO _stage) { this.stageDetails = _stage; }

    /* VARIABLES */
    private boolean debug;
    private int timeout;
    private static final String 
        EMPTY = "", FAILURE = "failure", GAP = " ", SUCCESS = "success";
    private static ConfigurationSingleton singletonInstance;
    private StageDataDTO stageDetails;
    private WebDriverDTO webDriverDetails;    
    private ArrayList<String> webDrivers;
    private ArrayList<StageDataDTO> stagesData;
}