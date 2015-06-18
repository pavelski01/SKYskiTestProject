package module.skyski_selenium.basic;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.AfterClass;
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
		{ this.watcherToSystemOut("START " + _description); }
		
        @Override
        protected void failed(Throwable _throwable, Description _description)
        { this.watcherToSystemOut("FAILURE " + _description); }

        @Override
        protected void succeeded(Description _description)
        { this.watcherToSystemOut("SUCCESS " + _description); }

        @Override
        protected void finished(Description _description)
        { this.watcherToSystemOut("FINISH " + _description); }
        
        private void watcherToSystemOut(String _description)
        { BasicTestCase.super.toSystemOut("[WATCHER] " + _description.toString()); }
	};
	
	@AfterClass
    public static void tearDownAfterClass() { setUpIsDone = false; }
	
	@After
	public void tearDown()
	{ super.retryingFindClickElementByCss("form > a:first-of-type"); }
	
	@Before
    public void setUpBefore() throws Exception
	{		
		if (!BasicTestCase.setUpIsDone)
		{
			new Thread(new LoginWindow()).start();
			super.getWebDriver().get("http://localhost:8080/skyski");
			super.setUpTimeout(super.config.getTimeout());
			BasicTestCase.setUpIsDone = true;
		}
		super.getWebDriver().get("http://localhost:8080/skyski");		
		super.titleAssertion("SKYski Project", "Load home page");
	}
	
	public class LoginWindow implements Runnable
	{		
        @Override
        public void run()
        { 
        	try { login(); }
        	catch (Exception ex) 
        	{ BasicTestCase.super.toSystemOut("[WATCHER] " + ex.getMessage()); }
        }

        public void login() throws Exception
        {
        	Thread.sleep(5000);
        	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();        	
        	clipboard.setContents(
    			new Transferable()
    			{
    				public DataFlavor[] getTransferDataFlavors() { return new DataFlavor[0]; }

    				public boolean isDataFlavorSupported(DataFlavor _flavor) { return false; }

    				public Object getTransferData(DataFlavor _flavor) throws UnsupportedFlavorException 
    				{ throw new UnsupportedFlavorException(_flavor); }
    			}, 
    			null
			);        	
            Robot robot = new Robot();
            StringSelection username = 
        		new StringSelection(BasicTestCase.super.config.getDevBasicCredentialUser());
            clipboard.setContents(username, null);            
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            Thread.sleep(2000);
            StringSelection passwd = 
        		new StringSelection(BasicTestCase.super.config.getDevBasicCredentialPassword());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(passwd, null);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(5000);
        }
    }
	
	private static boolean setUpIsDone = false;
}