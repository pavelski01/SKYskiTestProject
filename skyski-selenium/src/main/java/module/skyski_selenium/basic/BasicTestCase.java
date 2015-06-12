package module.skyski_selenium.basic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.openqa.selenium.Alert;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicTestCase extends BasicTestAction
{
	@Rule
	public TestName testName = new TestName()
	{		
		@Override
        protected void starting(Description _description)
		{ this.watcherToSystemOut("START" + _description); }
		
        @Override
        protected void failed(Throwable _throwable, Description _description)
        { this.watcherToSystemOut("FAILURE " + _description); }

        @Override
        protected void succeeded(Description _description)
        { this.watcherToSystemOut("SUCCESS " + _description); }

        @Override
        protected void finished(Description _description)
        { this.watcherToSystemOut("FINISH " + _description); }
        
        private void watcherToSystemOut(Description _description)
        { BasicTestCase.super.toSystemOut("[WATCHER] " + _description.toString()); }
	};
	
	@Before
    public void setUp() throws Exception
	{
		super.getWebDriver().get("http://localhost:8080/skyski");
		WebDriverWait wait = new WebDriverWait(super.getWebDriver(), 10);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.authenticateUsing(new UserAndPassword("user", "admin"));
	}
}