package one.tulasi.framework.core;

import one.tulasi.framework.enums.BrowserType;
import one.tulasi.framework.util.ReportSetup;
import one.tulasi.framework.util.TimeOut;
import one.tulasi.framework.util.UtilBase;
import one.tulasi.framework.util.Wait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

/**
 * Created by tulasiram on 12/8/16.
 * This is the base abstract TestClass. Every TestClass should be extending this class.
 */
public abstract class TestBase
{
	private WebDriver driver;
	private final Logger log = LogManager.getLogger(TestBase.class);
	private boolean isReportFolderCreated = false;
	
	/**
	 * Getter method for WebDriver
	 * @return WebDriver
	*/
    public WebDriver getDriver() {
        return driver;
    }

	/**
	 * Setter method for WebDriver
	 * @param driver
	 */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
     
    @BeforeSuite
    public void beforeSuite() throws Exception {
		if (!isReportFolderCreated) {
			ReportSetup.createFolderStructure();
			isReportFolderCreated = true;
		}
    }
    

	public void initialize(String browserType, ITestContext context) {
    	try {
	    	initiateDriver(browserType);
	    	log.info("Initiated WebDriver...");
	    	context.setAttribute("driver", driver);		
			driver.manage().window().maximize();
			Wait wait = new Wait(driver);
			wait.setImplicitWait(TimeOut.IMPLICIT);
    	}
    	catch (Exception e) {		    		
       		log.error(e.getMessage() +"---" + UtilBase.getStackTrace());
    	}
	}



    /**
     * Purpose - to initiate driver based on the browser
     */
	private void initiateDriver(String browserType) {
		log.info("Browser name present in config file : " + browserType.toUpperCase());
		log.info("-----------------STARTED RUNNING SELENIUM TESTS ON LOCAL MACHINE------------------");
		setDriver(browserType);		
	}
	

	private void setDriver(String browserType) {
		switch(BrowserType.getBrowserType(browserType)) {
			case CHROME:
				driver = (new ChromeBrowser()).getDriver();
				break;
			case FIREFOX:
				driver = FirefoxBrowser.init();
				break;
			case HTMLUNIT:
				driver = new HtmlUnitBrowser();
				break;
		}
	}
    
    /**
     * This method since added in "AfterClass" group and when this class is inherited from a TestSuite class, it will be called automatically
     */
    @AfterMethod
	public void closeBrowser() {
    	if(driver != null) {
    		driver.quit();
    		driver=null;
    	}
	}
    
    @AfterSuite
    public void addLogFileToReport() {
		//// TODO: 12/6/2016
    	log.info("After Suite");
		String logFilePath = ReportSetup.getReportsPath() + UtilBase.getFileSeparator() + "Log.log";
		Reporter.log("<br>");
    	Reporter.log("<a class=\"cbutton\" href=\""+logFilePath+"\">Click to Open Log File</a>");
    }
    
	
    public void setPageLoadTimeOut(int timeOut) {
		driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);					
    }
    
    protected abstract void login();

}
