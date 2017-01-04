package one.tulasi.framework.core;

import one.tulasi.framework.util.ReportSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;

import java.io.File;

/**
 * This class defines all methods required to initialize FireFoxDriver
 * Two methods are written so far - FFDriverDefault and with profile
 */
public class FirefoxBrowser  
{
	static WebDriver driver;
	static FirefoxProfile firefoxProfile;
	private static final Logger log = LogManager.getLogger("FirefoxBrowser");
	static String fileSeparator = System.getProperty("file.separator");
	
	public static WebDriver init()
	{	
		System.setProperty("webdriver.gecko.driver", getDriverPath());
    	if(isProfilePresent())
    	{
    		log.info("Firefox profile Exists");
    		log.info("Launching firefox with specified profile");
			driver = FirefoxDriver(getProfilePath());
		}
		else
		{
			log.info("Launching firefox with a new profile");
			driver = FirefoxDriver(); 	    
		}
	    return driver;
	}
	
	
	public static String getDriverPath()
	{
		return System.getProperty("user.dir")+"\\drivers\\geckodriver.exe";
	}
	
	/**
	 * Returns FirefoxDriver with default profile. This method will also disables the auto update of 
	 * firefox browser to next versions and takes care of accepting untrusted certificates 
	 * @return Webdriver initialized with FirefoxDriver
	 * @throws Exception
	 */
	public static WebDriver FirefoxDriver()
	{
		firefoxProfile = new FirefoxProfile();
		setProfilePreferences();
		if(isBinaryPathPresent())
		{	
			File pathToBinary = new File(getBinaryPath());
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			driver = new FirefoxDriver(ffBinary, firefoxProfile);
		}
		else
			driver = new FirefoxDriver(firefoxProfile);
        return driver;
	}
	

	/**
	 * Returns FirefoxDriver with specific profile. This method will also disables the auto update of 
	 * firefox browser to next versions 
	 * @param profilePath where firefox profile is stored
	 * @return Webdriver intialized with Firefoxdriver
	 * @throws Exception
	 */
	
	public static WebDriver FirefoxDriver(String profilePath)
	{
		//Initialize firefox browser with FF profile
		firefoxProfile = new FirefoxProfile(new File(profilePath));
		//set the Firefox preference "auto upgrade browser"  to false and to prevent compatibility issues
		setProfilePreferences();
		if(isBinaryPathPresent())
		{	
			File pathToBinary = new File(getBinaryPath());
			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			driver = new FirefoxDriver(ffBinary, firefoxProfile);
		}
		else
			driver = new FirefoxDriver(firefoxProfile);
       	return driver;
	}
	
	public static void setProfilePreferences()
	{
		firefoxProfile.setAcceptUntrustedCertificates(true);
		firefoxProfile.setPreference("app.update.enabled", false);        
		firefoxProfile.setPreference("browser.download.folderList",2);
		firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false);
		firefoxProfile.setPreference("browser.download.dir", ReportSetup.getDownloadsPath());
		String mimetypes = "application/pdf, application/x-pdf, application/acrobat, applications/vnd.pdf, text/pdf, text/x-pdf, application/octet-stream, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/x-rar-compressed,application/zip,application/vnd.ms-excel,application/msexcel,application/x-msexcel,application/x-ms-excel,application/x-excel,application/x-dos_ms_excel,application/xls,application/x-xls";
		firefoxProfile.setPreference("browser.helperApps.neverAsk.openFile", mimetypes);
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimetypes);
		firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
		firefoxProfile.setPreference("browser.download.manager.showAlertOnComplete",false);
		firefoxProfile.setPreference("browser.download.panel.shown",false);
		firefoxProfile.setPreference("browser.download.manager.closeWhenDone",true);
	}
	

	/**
	 * Method to retrieve the profile path given in Properties file
	 * @return - returns Firefox profile directory
	 * @throws Exception
	 */
	public static String getProfilePath()
	{
		return ""; //provider firefox profile path
			
	}
	
	
	public static boolean isProfilePresent() {
		String profilePath = getProfilePath();
		try {
			if(!profilePath.isEmpty()) {
				File profileFolder = new File(profilePath);
				return profileFolder.exists();
			}
			else {
				return false;
			}
		} catch(NullPointerException e) {
			log.error("Firefox Profile does not exist - "+profilePath);
			Assert.fail("Firefox Profile does not exist - "+profilePath);
			return false;
		}
	}


	public static String getBinaryPath() {
		return ""; //provider firefox binary that is executable path
			
	}
	
	
	public static boolean isBinaryPathPresent() {
		String binaryPath = getBinaryPath();
		try {
			if(!binaryPath.isEmpty()) {
				File binaryFile = new File(binaryPath);
				return binaryFile.exists();
			}
			else {
				return false;
			}
		} catch(NullPointerException e) {
			log.error("Firefox Profile does not exist - "+binaryPath);
			Assert.fail("Firefox Profile does not exist - "+binaryPath);
			return false;
		}
	}
}
