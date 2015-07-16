package module.skyski_selenium.runner;

import java.lang.reflect.Field;

import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunNotifier;

import module.skyski_selenium.suite.AllTests;

public class TestRunner
{
	public static void main(String[] _args) throws Exception
	{
		Computer computer = new Computer();
	    JUnitCore jUnitCore = new JUnitCore();
	    Field field = JUnitCore.class.getDeclaredField("notifier");
	    field.setAccessible(true);
	    RunNotifier runNotifier = (RunNotifier)field.get(jUnitCore);
	    jUnitCore.run(computer, AllTests.class);
	    runNotifier.pleaseStop();
	}
}