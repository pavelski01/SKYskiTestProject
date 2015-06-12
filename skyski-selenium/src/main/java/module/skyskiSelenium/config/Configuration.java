package module.skyskiSelenium.config;

import com.opera.core.systems.OperaDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.StringTokenizer;

public class Configuration
{
    /* CONSTRUCTOR */
    public Configuration()
    {
        final String
            BROWSER_KEY = "browser", DEBUG_KEY = "debug",
            STAGE_KEY = "stage", TIMEOUT_KEY = "timeout";
        Properties properties = this.getProperties();
        this.browser = properties.getProperty(BROWSER_KEY);
        if (this.browser == null) this.browser = Configuration.EMPTY;
        else this.analyzeBrowser(this.browser);
        this.debug = Boolean.parseBoolean(properties.getProperty(DEBUG_KEY));
        this.timeout = Integer.parseInt(properties.getProperty(TIMEOUT_KEY));
        this.stage = properties.getProperty(STAGE_KEY);
        if (this.stage == null) this.stage = Configuration.EMPTY;
        else this.analyzeStage(this.stage, properties);
    }

    public static Configuration getSingletonInstance()
    {
        if (singletonInstance == null) singletonInstance = new Configuration();
        return singletonInstance;
    }
    
    public static String getStatusToString(boolean _status)
    {
        if (_status) return Configuration.SUCCESS;
        else return Configuration.FAILURE;
    }

    private void analyzeStage(String _stage, Properties _properties)
    {
        if (_stage.equals(Configuration.EMPTY)) return;
        final String DELIMITER = "|";
        final char D_CASE = 'd', P_CASE = 'p', T_CASE = 't';
        final String DEV_STAGE = "dev", PROD_STAGE = "prod", TEST_STAGE = "test";
        StringTokenizer stageStringTokenizer = new StringTokenizer(_stage, DELIMITER);
        String token;
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
        if (_browser.equals(Configuration.EMPTY)) return;
        final String DELIMITER = "|";
        final char C_CASE = 'c', F_CASE = 'f', O_CASE = 'o';
        StringTokenizer browserStringTokenizer = new StringTokenizer(_browser, DELIMITER);
        String token;
        while (browserStringTokenizer.hasMoreTokens())
        {
            token = browserStringTokenizer.nextToken().toLowerCase();
            switch (token.charAt(0))
            {
                case C_CASE:
                    this.chromeWebDriver = this.getChromeWebDriverInstance();
                    break;
                case F_CASE:
                    this.firefoxWebDriver = this.getFirefoxWebDriverInstance();
                    break;
                case O_CASE:
                    this.operaWebDriver = this.getOperaWebDriverInstance();
                    break;
            }
        }
    }

    private WebDriver getChromeWebDriverInstance()
    {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return new ChromeDriver(desiredCapabilities);
    }

    private WebDriver getFirefoxWebDriverInstance()
    {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.private.browsing.autostart", true);
        return new FirefoxDriver(firefoxProfile);
    }

    private WebDriver getOperaWebDriverInstance()
    {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.operaBlink();
        desiredCapabilities.setCapability("opera.arguments", "fullscreen");
        return new OperaDriver(desiredCapabilities);
    }

    private void initializeStage(String _stagePrefix, Properties _properties)
    {
        String coreKey1 = _stagePrefix + this.core[0];
        String coreKey2 = _stagePrefix + this.core[1];
        String coreValue1 = _properties.getProperty(coreKey1);
        String coreValue2 = _properties.getProperty(coreKey2);
        if
        (
            coreValue1 != null && !coreValue1.equals(Configuration.EMPTY) &&
                coreValue2 != null && !coreValue2.equals(Configuration.EMPTY)
        )
        {
            this.setUpReflectionData(coreKey1, coreValue1);
            this.setUpReflectionData(coreKey2, coreValue2);
            final String[] realFake = { "Real", "Fake" };
            String temporaryKey, temporaryValue;
            for (String state : realFake)
                for (String record : this.data)
                {
                    temporaryKey = _stagePrefix + state + record;
                    temporaryValue = _properties.getProperty(temporaryKey);
                    if (temporaryValue == null) temporaryValue = Configuration.EMPTY;
                    this.setUpReflectionData(temporaryKey, temporaryValue);
                }
        }
    }

    private void setUpReflectionData(String temporaryKey, String temporaryValue)
    {
        try { this.getFiled(temporaryKey).set(this, temporaryValue); }
        catch (IllegalAccessException iaEX)
        {
            final String ILLEGAL = "Illegal access:";
            System.out.println(ILLEGAL + Configuration.GAP + iaEX.toString());
        }
    }

    private Field getFiled(String _name)
    {
        try { return this.getClass().getDeclaredField(_name); }
        catch (NoSuchFieldException nsfEX)
        {
            final String NO_FIELD = "Field not exist:";
            System.out.print(NO_FIELD + Configuration.GAP + nsfEX.toString());
            return null;
        }
    }

    private Properties getProperties()
    {
        final String
            PROPERTIES_NAME = "configuration.properties",
            PROPERTIES_PATH = "src/config/";
        Properties properties = new Properties();
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(PROPERTIES_PATH + PROPERTIES_NAME);
            properties.load(inputStream);
        }
        catch (IOException ioEX)
        {
            final String
                PROPERTIES_ERROR_MSG1 = "Problem with load",
                PROPERTIES_ERROR_MSG2 = "configuration file:";
            System.out.println(
                PROPERTIES_ERROR_MSG1 + Configuration.GAP + PROPERTIES_NAME + Configuration.GAP +
                    PROPERTIES_ERROR_MSG2 + Configuration.GAP + ioEX.toString()
            );
            if (inputStream != null)
            {
                try { inputStream.close(); }
                catch (IOException io2EX)
                {
                    final String STREAM_ERROR_MSG = "Problem with close input stream:";
                    System.out.println(STREAM_ERROR_MSG + Configuration.GAP + io2EX.toString());
                }
            }
        }
        return properties;
    }

    /* GETTERS */
    public boolean isDebug() { return this.debug; }
    public WebDriver getFirefoxWebDriver() { return this.firefoxWebDriver; }
    public WebDriver getChromeWebDriver() { return this.chromeWebDriver; }
    public WebDriver getOperaWebDriver() { return this.operaWebDriver; }
    public int getTimeout() { return this.timeout; }
    public String getStage() { return this.stage; }
    public String getDevBaseURL() { return this.devBaseURL; }
    public String getDevBasePort() { return this.devBasePort; }
    public String getDevRealLogin() { return this.devRealLogin; }
    public String getDevRealPassword() { return this.devRealPassword; }
    public String getDevRealEmail() { return this.devRealEmail; }
    public String getDevRealForename() { return this.devRealForename; }
    public String getDevRealSurname() { return this.devRealSurname; }
    public String getDevRealStreetAddress() { return this.devRealStreetAddress; }
    public String getDevRealPostalCity() { return this.devRealPostalCity; }
    public String getDevRealPostalCode() { return this.devRealPostalCode; }
    public String getDevRealPhone() { return this.devRealPhone; }
    public String getDevFakeLogin() { return this.devFakeLogin; }
    public String getDevFakePassword() { return this.devFakePassword; }
    public String getDevFakeEmail() { return this.devFakeEmail; }
    public String getDevFakeForename() { return this.devFakeForename; }
    public String getDevFakeSurname() { return this.devFakeSurname; }
    public String getDevFakeStreetAddress() { return this.devFakeStreetAddress; }
    public String getDevFakePostalCity() { return this.devFakePostalCity; }
    public String getDevFakePostalCode() { return this.devFakePostalCode; }
    public String getDevFakePhone() { return this.devFakePhone; }
    public String getTestBaseURL() { return this.testBaseURL; }
    public String getTestBasePort() { return this.testBasePort; }
    public String getTestRealLogin() { return this.testRealLogin; }
    public String getTestRealPassword() { return this.testRealPassword; }
    public String getTestRealEmail() { return this.testRealEmail; }
    public String getTestRealForename() { return this.testRealForename; }
    public String getTestRealSurname() { return this.testRealSurname; }
    public String getTestRealStreetAddress() { return this.testRealStreetAddress; }
    public String getTestRealPostalCity() { return this.testRealPostalCity; }
    public String getTestRealPostalCode() { return this.testRealPostalCode; }
    public String getTestRealPhone() { return this.testRealPhone; }
    public String getTestFakeLogin() { return this.testFakeLogin; }
    public String getTestFakePassword() { return this.testFakePassword; }
    public String getTestFakeEmail() { return this.testFakeEmail; }
    public String getTestFakeForename() { return this.testFakeForename; }
    public String getTestFakeSurname() { return this.testFakeSurname; }
    public String getTestFakeStreetAddress() { return this.testFakeStreetAddress; }
    public String getTestFakePostalCity() { return this.testFakePostalCity; }
    public String getTestFakePostalCode() { return this.testFakePostalCode; }
    public String getTestFakePhone() { return this.testFakePhone; }
    public String getProdBaseURL() { return this.prodBaseURL; }
    public String getProdBasePort() { return this.prodBasePort; }
    public String getProdRealLogin() { return this.prodRealLogin; }
    public String getProdRealPassword() { return this.prodRealPassword; }
    public String getProdRealEmail() { return this.prodRealEmail; }
    public String getProdRealForename() { return this.prodRealForename; }
    public String getProdRealSurname() { return this.prodRealSurname; }
    public String getProdRealStreetAddress() { return this.prodRealStreetAddress; }
    public String getProdRealPostalCity() { return this.prodRealPostalCity; }
    public String getProdRealPostalCode() { return this.prodRealPostalCode; }
    public String getProdRealPhone() { return this.prodRealPhone; }
    public String getProdFakeLogin() { return this.prodFakeLogin; }
    public String getProdFakePassword() { return this.prodFakePassword; }
    public String getProdFakeEmail() { return this.prodFakeEmail; }
    public String getProdFakeForename() { return this.prodFakeForename; }
    public String getProdFakeSurname() { return this.prodFakeSurname; }
    public String getProdFakeStreetAddress() { return this.prodFakeStreetAddress; }
    public String getProdFakePostalCity() { return this.prodFakePostalCity; }
    public String getProdFakePostalCode() { return this.prodFakePostalCode; }
    public String getProdFakePhone() { return this.prodFakePhone; }

    /* VARIABLES */
    private boolean debug;
    private WebDriver chromeWebDriver, firefoxWebDriver, operaWebDriver;
    private int timeout;
    private final String[] core = { "URL", "Port" };
    private final String[] data =
    {
        "Login", "Password", "Email", "Forename", "Surname",
        "StreetAddress", "PostalCity", "PostalCode", "Phone"
    };
    private String browser, stage;
    private String devBaseURL, devBasePort;
    private String
        devRealLogin, devRealPassword, devRealEmail, devRealForename, devRealSurname,
        devRealStreetAddress, devRealPostalCity, devRealPostalCode, devRealPhone;
    private String
        devFakeLogin, devFakePassword, devFakeEmail, devFakeForename, devFakeSurname,
        devFakeStreetAddress, devFakePostalCity, devFakePostalCode, devFakePhone;
    private String testBaseURL, testBasePort;
    private String
        testRealLogin, testRealPassword, testRealEmail, testRealForename, testRealSurname,
        testRealStreetAddress, testRealPostalCity, testRealPostalCode, testRealPhone;
    private String
        testFakeLogin, testFakePassword, testFakeEmail, testFakeForename, testFakeSurname,
        testFakeStreetAddress, testFakePostalCity, testFakePostalCode, testFakePhone;
    private String prodBaseURL, prodBasePort;
    private String
        prodRealLogin, prodRealPassword, prodRealEmail, prodRealForename, prodRealSurname,
        prodRealStreetAddress, prodRealPostalCity, prodRealPostalCode, prodRealPhone;
    private String
        prodFakeLogin, prodFakePassword, prodFakeEmail, prodFakeForename, prodFakeSurname,
        prodFakeStreetAddress, prodFakePostalCity, prodFakePostalCode, prodFakePhone;
    private static final String 
        EMPTY = "", FAILURE = "failure", GAP = " ", SUCCESS = "success";
    private static Configuration singletonInstance;
}
