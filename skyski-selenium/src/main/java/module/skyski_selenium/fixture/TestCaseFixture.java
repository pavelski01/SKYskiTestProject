package module.skyski_selenium.fixture;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.Description;

import module.skyski_selenium.config.ConfigurationSingleton;

public abstract class TestCaseFixture extends TestActionFixture
{
	@Rule
	public TestName testName = new TestName()
	{
		@Override
        protected void starting(Description _description)
		{ this.watcherToSystemOut("[START] " + _description); }
		
        @Override
        protected void failed(Throwable _throwable, Description _description)
        { this.watcherToSystemOut("[FAILURE] " + _description); }

        @Override
        protected void succeeded(Description _description)
        { this.watcherToSystemOut("[SUCCESS] " + _description); }

        @Override
        protected void finished(Description _description)
        { this.watcherToSystemOut("[FINISH] " + _description); }
        
        private void watcherToSystemOut(String _description)
        {
        	ConfigurationSingleton.getSingletonInstance().toSystemOut(
    			"[WATCHER]" + 
					_description.substring(0, _description.indexOf('(') + 1) +
					_description.substring(_description.lastIndexOf('.') + 1, _description.indexOf(')')) +
				_description.substring(_description.indexOf(')'), _description.length())
			);
    	}
	};
	
	@After
	public void tearDownEachTime()
	{ super.findAndClickElementByCss("form > a:first-of-type"); }
	
	@Before
    public void setUpBeforeEachTime()
	{
		ConfigurationSingleton.getSingletonInstance().getWebDriverDetails().getWebDriver().get(
			ConfigurationSingleton.getSingletonInstance().getStageDetails().getAppUrl()
		);
		super.titleAssertion("SKYski Project", "Load home page");
	}
}