package module.skyski_selenium.basic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.Description;

public class BasicTestCase extends BasicTestAction
{
	@Rule
	public TestName testName = new TestName()
	{		
		@Override
        protected void starting(Description _description)
		{ this.watcherToSystemOut(_description); }
		
        @Override
        protected void failed(Throwable _throwable, Description _description)
        { this.watcherToSystemOut(_description); }

        @Override
        protected void succeeded(Description _description)
        { this.watcherToSystemOut(_description); }

        @Override
        protected void finished(Description _description)
        { this.watcherToSystemOut(_description); }
        
        private void watcherToSystemOut(Description _description)
        { BasicTestCase.super.toSystemOut("[WATCHER] " + _description.toString()); }
	};
	
	@Before
    public void setUp() throws Exception {}
}