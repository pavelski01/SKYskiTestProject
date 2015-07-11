package module.skyski_selenium.enforce;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Suite.SuiteClasses;

import module.skyski_selenium.casus.BibliographyPage;
import module.skyski_selenium.casus.HomePage;
import module.skyski_selenium.casus.LoginPage;

@RunWith(Parameterized.class)
@SuiteClasses({ BibliographyPage.class, HomePage.class, LoginPage.class })
public class AllTests {}