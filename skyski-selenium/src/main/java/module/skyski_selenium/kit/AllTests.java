package module.skyski_selenium.kit;

import java.util.Collection;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

import module.skyski_selenium.casus.BibliographyPage;
import module.skyski_selenium.casus.HomePage;
import module.skyski_selenium.casus.LoginPage;
import module.skyski_selenium.dto.StageDataDTO;
import module.skyski_selenium.dto.WebDriverDTO;
import module.skyski_selenium.marker.FirefoxTests;

@Category(FirefoxTests.class)
@RunWith(Parameterized.class)
@SuiteClasses({ BibliographyPage.class, HomePage.class, LoginPage.class })
public class AllTests
{
	@Parameters
    public static Collection<Object[]> data() { return null; }
    @Parameter
    public StageDataDTO stageData;
    @Parameter
    public WebDriverDTO webDriver;
}