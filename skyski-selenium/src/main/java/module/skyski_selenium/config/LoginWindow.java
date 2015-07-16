package module.skyski_selenium.config;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;

public class LoginWindow implements Runnable
{
	@Override
    public void run()
    { 
    	try { login(); }
    	catch (Exception _e) 
    	{ ConfigurationSingleton.getSingletonInstance().toSystemOut("[WATCHER] " + _e.getMessage()); }
    }

    public void login() throws AWTException, InterruptedException, UnsupportedFlavorException 
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
    		new StringSelection(
				ConfigurationSingleton.getSingletonInstance().getStageDetails().getBasicCredentialUser()
			);
        clipboard.setContents(username, null);            
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        Thread.sleep(2000);
        StringSelection passwd = 
    		new StringSelection(
				ConfigurationSingleton.getSingletonInstance().getStageDetails().getBasicCredentialPassword()
			);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(passwd, null);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}