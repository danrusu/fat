package utils;
import static base.Logger.logLines;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openqa.selenium.os.WindowsUtils;
//import org.sikuli.script.App;
//import org.sikuli.script.FindFailed;
//import org.sikuli.script.Screen;

/**
 * Class for simulating different windows action: clipboard, keyboard ...
 * @author Dan.Rusu
 *
 */
public final class WinUtils {
	
	private WinUtils(){
		throw new AssertionError("This helper class must not be istantiated!");
	}
	

	public static void toClipboard(String s){
		StringSelection ss = new StringSelection(s);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
	}

	
	public static void paste(){
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);

		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	
	public static void pressEnter(){
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	
	public static boolean thisIsWindows(){
		 return WindowsUtils.thisIsWindows();
	}
	
	
/*	
     public static void sikuliBrowse(String path){
		Screen s = new Screen();
		App.focus("Open");
		try {
			s.type(null, path + "\n", 0);
		} catch (FindFailed e) {
			throw new TestCaseFailure("Sikuli failed!");
		}
	 }
*/
	
	
	public static String getHostName(){
		String hostname = "unknownHost";
		try {
			hostname = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logLines("Could not get hostname: " + e);
		}
		return hostname;
	}
}
