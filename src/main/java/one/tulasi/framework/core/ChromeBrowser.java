package one.tulasi.framework.core;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import one.tulasi.framework.util.ReportSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import java.io.File;
import java.util.HashMap;


/**
 * This class defines all methods required to initialize ChromeDriver
 */
public class ChromeBrowser extends Browser {
	private static final Logger log = LogManager.getLogger(ChromeBrowser.class);

	@Override
	public WebDriver getDriver() {
		if(super.getDriver()==null) {
			ChromeDriverManager.getInstance().setup();
			if (isUserDataDirPresent()) {
				driver = initChromeDriver(getUserDataPath(), getProfileName());
			} else {
				driver = initChromeDriver();
			}
		}
		return super.getDriver();
	}

	private WebDriver initChromeDriver() {
		log.info("Launching google chrome with new profile..");
//		System.setProperty("webdriver.chrome.driver", getDriverPath());
	    ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", getChromePrefs());
		options.addArguments("start-maximized");
		log.info("chrome driver initialized..");
		return new ChromeDriver(options);
	}


	private WebDriver initChromeDriver(String UserDataPath, String ProfileName) {
		log.info("Launching google chrome with specified profile - "+ ProfileName);
//		System.setProperty("webdriver.chrome.driver", getDriverPath());
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", getChromePrefs());
		options.addArguments("start-maximized");
		if(isProfileDirPresent()) {
			log.info("Running with specified chrome profile");
			options.addArguments("user-data-dir=" + UserDataPath);
			options.addArguments("--profile-directory=" + ProfileName);
			log.info("Init chrome driver with custom profile is completed..");
		} else {
			log.info("Specified chrome profile does not exists in 'User Data' folder");
			log.info("Hence Chrome Browser is launched with a new profile..");	
		}
		return new ChromeDriver(options);
	}

	private HashMap<String,Object> getChromePrefs() {
		HashMap<String, Object> chromePrefs = new HashMap<>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", ReportSetup.getDownloadsPath());
		return chromePrefs;
	}

	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * @return - returns chrome user data path
	*/
	public String getUserDataPath() {
		return ""; //provide userdata path of chrome driver
	}
	
	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * @return - returns chrome user data path
	*/
	public String getProfileName() {
		return ""; //provide profile name of chrome
	}
	
	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * @return - returns chrome user data path
	*/
	public boolean isUserDataDirPresent() {
		String userDataPath= getUserDataPath();
		try {
			if(!userDataPath.isEmpty()) {
				File userDataFolder = new File(userDataPath);
				return userDataFolder.exists();
			} else {
				return false;
			}
		} catch(NullPointerException e) {
			log.error("folder does not exists"+userDataPath);
			return false;
		}
	}
	
	/**
	 * Method to retrieve the Chrome 'User Data' path given in Properties file
	 * @return - returns chrome user data path
	*/
	public boolean isProfileDirPresent() {
		String profilePath = getUserDataPath()+"/"+getProfileName();
		try {
			if(!profilePath.isEmpty()) {
				File profileFolder = new File(profilePath);
				return profileFolder.exists();
			} else {
				return false;
			}
		} catch(NullPointerException e) {
			log.error("Profile does not exists"+getProfileName());
			Assert.fail("Profile does not exists"+getProfileName());
			return false;
		}
	}
}
