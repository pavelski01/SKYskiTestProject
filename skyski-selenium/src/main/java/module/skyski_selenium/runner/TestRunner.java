package module.skyski_selenium.runner;

import java.lang.reflect.Field;

import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunNotifier;

import module.skyski_selenium.config.ConfigurationSingleton;
import module.skyski_selenium.suite.AllTests;

public class TestRunner
{
	public static void main(String[] _args) throws Exception
	{
		Computer aComputer = new Computer();
	    JUnitCore aJunitCore = new JUnitCore();
	    Field aField = JUnitCore.class.getDeclaredField("notifier");
	    aField.setAccessible(true);
	    RunNotifier aRunNotifier = (RunNotifier)aField.get(aJunitCore);
	    aJunitCore.run(aComputer, AllTests.class);
	    ConfigurationSingleton.getSingletonInstance().webDriverQuit();
	    aRunNotifier.pleaseStop();
	}
}