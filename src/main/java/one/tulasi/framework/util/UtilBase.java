package one.tulasi.framework.util;

import one.tulasi.framework.enums.BrowserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.*;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public abstract class UtilBase {

	private static final Logger log = LogManager.getLogger(UtilBase.class);

	/**
	 * Purpose - to get the system name
	 * @return - String (returns system name)
	 */
   public static String machineName()
   {
   	String computerName = null;
   	try 
   	{
			computerName=InetAddress.getLocalHost().getHostName();
		} 
   	catch (UnknownHostException e) 
   	{
			log.error("Unable to get the hostname..."+ e.getCause());
		}
		return computerName;
   }
   
   /**
    * To get the entire exception stack trace
    * 
    * @return returns the stack trace
    */
   public static String getStackTrace()
   {
		String trace = "";
		int i;
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for(i=2;i<stackTrace.length;i++)
		{
			trace = trace+"\n"+stackTrace[i];
		}
		return trace;
   }

   

   /**
    * Purpose - to get current date and time
    * @return - String (returns date and time)
    */
   public static String getCurrentDateTime()
   {
   		Calendar currentDate = Calendar.getInstance();
       SimpleDateFormat formatter= new SimpleDateFormat("dd-MMM-yyyy:HH.mm.ss");
       return formatter.format(currentDate.getTime());
   }
   
   /**
	 * Purpose - To convert given time in "yyyy-MM-dd-HH:mm:ss" to IST time
	 * @returns date in String format
	 */
	
   public static String convertToISTTime(String origTime) 
	{
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
       TimeZone obj = TimeZone.getTimeZone("GMT");
       formatter.setTimeZone(obj);
       try 
       {
			Date date = formatter.parse(origTime);
			formatter = new SimpleDateFormat("dd-MMM-yyyy:HH.mm.ss");
			//System.out.println(date);
			return formatter.format(date);
		} catch (ParseException e) {
			log.error("Cannot parse given date .." + origTime);
			log.info("returning current date and time .." + origTime);
		}
		return getCurrentDateTime();
   }    

   
   /**
    * Purpose - to stop the execution
    */
   public static void suspendRun() {
		log.info("TEST RUN HAS BEEN SUSPENDED BY FORCE");
		System.exit(1);
   }
   
   

   /**
    * Purpose - to generate the random number which will be used while saving a screenshot
    * @return - returns a random number
    */
	public static int getRandomNumber(int maxValue)
	{
		Random rand = new Random();
		if(maxValue==0) {
			return 0;
		}
		else {
			return Math.abs(rand.nextInt(maxValue));
		}
    }
	
	
	/**
	 * 
	 * Gives the name of operating system your are currently working on
	 * 
	 * @return returns the OS name
	 */
	public static String getOSName()
	{
		return System.getProperty("os.name");
	
	}	
	
	/**
	 * 
	 * Gives the separator value according to Operation System
	 * 
	 * @return returns the separator with respect to Operation System
	 */
	public static String getFileSeparator()
	{		
		return System.getProperty("file.separator");
	}
	
	
	public static boolean compareLists(List<String> list1,List<String> list2) {
		if(list1.size()==list2.size()) {
			Collection<String> collection1 = new ArrayList<>();
			collection1.addAll(list1);
			collection1.removeAll(list2);
            return collection1.size() == 0;
        }
		else
		{
			return false;
		}
	}
	
	 /**
		 * 
		 * @param colorStr e.g. "#FFFFFF"
		 * @return Color object
		 */
	public static Color hex2Rgb(String colorStr) {
		return new Color(
				Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
				Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
				Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);

		if(file.exists()) {
			return file.delete();
		} else {
			log.info("file - " + filePath + " - does not exists to delete");
			log.trace(UtilBase.getStackTrace());
			return false;
		}
	}

	public static BrowserType getBrowserType(WebDriver driver) {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		return BrowserType.getBrowserType(cap.getBrowserName().toLowerCase());
	}

	public static boolean isHTMLUnitBrowser(WebDriver driver) {
		try {
			return ((HtmlUnitDriver)driver).getCapabilities().getBrowserName().equals("htmlunit");
		} catch (ClassCastException e) {
			return false;
		}
	}
}
