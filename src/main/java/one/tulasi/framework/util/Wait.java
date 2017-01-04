package one.tulasi.framework.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class Wait {
	
	private final WebDriver driver;
	private final Logger log = LogManager.getLogger("Wait");
	
	public Wait(WebDriver driver)
	{
		this.driver = driver;
	}
	
	/**
	* Sets implicitWait to ZERO. This helps in making explicitWait work...
	* @throws Exception
	*/
	public void nullifyImplicitWait()
	{
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
	}

	/**
	* Set driver's implicitlyWait() time according given waitTime.
	* @param waitTimeInSeconds (int) The time in seconds to specify implicit wait
	* @return void
	* @throws Exception
	*/
	public void setImplicitWait(int waitTimeInSeconds)
	{
		driver.manage().timeouts().implicitlyWait(waitTimeInSeconds, TimeUnit.SECONDS);
	}
	
	/**
	* Waits for the Condition of JavaScript.
	*
	*
	* @param javaScript (String) The javaScript condition we are waiting. e.g. "return (xmlhttp.readyState >= 2 && xmlhttp.status == 200)"
	* @param timeOutInSeconds (int) The time in seconds to wait until returning a failure
	* @return True (boolean) if javaScript condition is satisfied within timeOutInSeconds 
	**/
	public boolean forJavaScriptCondition(final String javaScript, int timeOutInSeconds)
	{
		boolean jscondition = false;
		nullifyImplicitWait();
		try
		{				
			new WebDriverWait(driver, timeOutInSeconds).until((ExpectedCondition<Boolean>) driverObject -> (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript));
			jscondition = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);		
		} 
		catch (Exception e) 
		{
			log.error("jscondition not satisfied..");
			Assert.fail("jscondition not satisfied.."+ UtilBase.getStackTrace());
		}
		setImplicitWait(TimeOut.IMPLICIT);
		return jscondition;
	}	

	

	/**
	 * Waits for an element till the timeout expires
	 * @param locator (By) locator object of the element to be found
	 * @param waitTime (int) The time in seconds to wait until returning a failure
	 * @return - True (Boolean) if element is located within timeout period else false
	 */
    public boolean isElementPresent(By locator, int waitTime)
	{    	
    	boolean bFlag = false;	
    	nullifyImplicitWait();
    	log.info("Waiting for presence of element " + locator);
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator)); 			
			if(driver.findElement(locator).isDisplayed()||driver.findElement(locator).isEnabled())
			{
				bFlag = true;
				log.info("Element " + locator + " is displayed");
			}
		}		
		catch (NoSuchElementException e)
		{
			log.info("Element " + locator + " was not found in DOM"+ UtilBase.getStackTrace());
		}
		catch (TimeoutException e)
		{
			log.info("Element " + locator + " was not displayed in time - "+waitTime);
		}
		catch (Exception e)
		{
			log.error("Element " + locator + " is not displayed"+ UtilBase.getStackTrace());
			Assert.fail("Element " + locator + " is not displayed"+ UtilBase.getStackTrace());
		}
		setImplicitWait(TimeOut.IMPLICIT);
		return bFlag;
	}
 
	/**
	 * Method -  Waits for an element till the element is clickable
	 * @param locator (By) locator object of the element to be found
	 * @param optionWaitTime (int) The time in seconds to wait until returning a failure
	 * @return - True (Boolean) if element is located and is clickable within timeout period else false
	 * @throws Exception
	 */
	public boolean untilClickable(By locator, int... optionWaitTime)
	{    	
		int waitTime =  getTime(optionWaitTime);
		boolean bFlag = false;
    	nullifyImplicitWait();
		try
		{
			log.info("Waiting until element " + locator+" is clickable");
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
			 
			if(driver.findElement((locator)).isDisplayed())
			{
				bFlag = true;
				log.info("Element " + locator + " is displayed and is clickable");
			}
		}
		
		catch (NoSuchElementException e)
		{
			log.error("Element " + locator + " was not displayed"+ UtilBase.getStackTrace());
			Assert.fail("Element " + locator + " was not displayed"+ UtilBase.getStackTrace());
		}
		catch (TimeoutException e)
		{
			log.error("Element " + locator + " was not clickable in time - "+waitTime+ UtilBase.getStackTrace());
			Assert.fail("Element " + locator + " was not clickable in time - "+waitTime+ UtilBase.getStackTrace());
		}
		catch (Exception e)
		{
			log.error("Element " + locator + " was not clickable"+ UtilBase.getStackTrace());
			Assert.fail("Element " + locator + " was not clickable" + UtilBase.getStackTrace());
		}
		setImplicitWait(TimeOut.IMPLICIT);
		return bFlag;
	}

	

	/**
	 * Method -  Waits for an element till the element is visible
	 * @param locator (By) locator object of the element to be found
	 * @param optionWaitTime (int) The time in seconds to wait until returning a failure
	 * @return - True (Boolean) if element is located and is visible on screen within timeout period else false
	 * @throws Exception
	 */
	public boolean isElementVisible(By locator, int... optionWaitTime)
	{
		int waitTime =  getTime(optionWaitTime);
		boolean bFlag = false;
		nullifyImplicitWait(); 
		log.info("Waiting until element " + locator+" is visible");
		try
		{			
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			setImplicitWait(TimeOut.IMPLICIT); 
			if(driver.findElement((locator)).isDisplayed())
			{
				bFlag = true;
			log.info("Element " + locator + " is displayed");
			}
		}
		catch (NoSuchElementException e)
		{	
			log.info("Element " + locator + " was not displayed"+ UtilBase.getStackTrace());
		}
		catch (TimeoutException e)
		{
			log.info("Element " + locator + " was not visible in time - "+waitTime);
		}
		
		catch (Exception e)
		{	
			log.error("Element " + locator + " was not displayed"+ UtilBase.getStackTrace());
			Assert.fail("Element " + locator + " was not displayed."+ UtilBase.getStackTrace());
		}
		return bFlag;
	}


	/**
	 * Purpose- Wait for an element till it is either invisible or not present on the DOM.
	 * @param locator (By) locator object of the element to be found
	 * @param optionWaitTime (int) The time in seconds to wait until returning a failure
	 * @return - True (Boolean) if the element disappears in specified waitTime.
	 * @throws Exception
	 */
	public boolean untilElementDisappears(By locator,int... optionWaitTime)
    {   
		int waitTime =  getTime(optionWaitTime);
		boolean isNotVisible = false;
		log.info("Waiting until element " + locator+" is invisible");
		try
		{						
			nullifyImplicitWait(); 
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)); 	
			isNotVisible = true;            
		}
		catch(Exception e)
		{           
			log.error("Element " + locator + " is not disappearing"+ UtilBase.getStackTrace());
			Assert.fail("Element " + locator + " is not disappearing." + UtilBase.getStackTrace());
		}
		setImplicitWait(TimeOut.IMPLICIT); 
		return isNotVisible;		
    }
    
    /** Waits for the completion of Ajax jQuery processing by checking "return jQuery.active == 0" condition.
    * @param timeOutInSeconds - The time in seconds to wait until returning a failure
    * @return True (Boolean) if jquery/ ajax is loaded within specified timeout.
    * @throws Exception
    * */
	public boolean forJQueryProcessing(int timeOutInSeconds)
	{
		log.info("Waiting ajax processing to complete..");
		boolean jQcondition = false;
		try 
		{
			Thread.sleep(500);
			new WebDriverWait(driver, timeOutInSeconds).until((ExpectedCondition<Boolean>) driverObject -> (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0"));
			jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return window.jQuery != undefined && jQuery.active === 0");
			System.out.println(jQcondition);
			return jQcondition;
		} 
		catch (Exception e) 
		{
			log.error("Page Loading is not completed"+ UtilBase.getStackTrace());
			Assert.fail("Page Loading is not completed"+ UtilBase.getStackTrace());
		}
		return jQcondition;
	}
	
	/**
	 * Method - Wait until a particular text appears on the screen
	 * @param text (String) - text until which the WebDriver should wait.
	 * @return void
	 */
	public void forPageToLoad(final String text) {
		log.info("Waiting for page to be loaded...");
		nullifyImplicitWait(); 
		(new WebDriverWait(driver, 20)).until((ExpectedCondition<WebElement>) d -> d.findElement(By.partialLinkText(text)));
		setImplicitWait(TimeOut.IMPLICIT); 
	}
	
	
	/**
	* Waits until page is loaded.
	* @param timeOutInSeconds - timeout in seconds
	* @return True (boolean)
	* @throws Exception
	*/
	public boolean forPageToLoad(int timeOutInSeconds)
	{
		log.info("Waiting for page to be loaded...");
		boolean isPageLoadComplete = false;
		nullifyImplicitWait(); //nullify implicitlyWait()
		try
		{
			new WebDriverWait(driver, timeOutInSeconds).until((ExpectedCondition<Boolean>) driverObject -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
			isPageLoadComplete = ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
		}
		catch(Exception e)
	   	{		
			log.error("Unable to load web page"+ UtilBase.getStackTrace());
			Assert.fail("unable to load webpage"+"\n"+ UtilBase.getStackTrace());
	   	}
		setImplicitWait(TimeOut.IMPLICIT);
		return isPageLoadComplete;
	}
	
	/**
	* Waits until page is loaded. Default timeout is 250 seconds. Poll time is every 500 milliseconds.
	*
	* @return void
	* @throws Exception
	*/
	
	public void forPageToLoad()
	{
		log.info("Waiting for page to be loaded...");
		try
		{
			int waitTime = 0;
			boolean isPageLoadComplete;
			do 
			{

				isPageLoadComplete = ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				log.debug(".");
				Thread.sleep(500);
				waitTime++;
				if(waitTime > 500)
				{
					break;
				}
			} 
			while(!isPageLoadComplete);

			if(!isPageLoadComplete) {	

				log.error("Unable to load webpage with in default timeout 250 seconds");
				Assert.fail("unable to load webpage with in default timeout 250 seconds");
			} else {
				log.info("Page load completed...");
			}
		}
		catch(Exception e)
		{		
			log.error("Unable to load web page"+ UtilBase.getStackTrace());
			Assert.fail("unable to load webpage"+"\n"+ UtilBase.getStackTrace());
		}

	}

    public boolean isElementPresent(By locator)
    {
    	log.info("Waiting for presence of element " + locator);
    	setImplicitWait(TimeOut.IMPLICIT);
    	return driver.findElements(locator).size()>0;	
    }
	
    public boolean isElementDisplayed(By locator)
    {
    	log.info("Verifying if element " + locator+ " is displayed");
    	boolean isDisplayed = false;
    	setImplicitWait(TimeOut.IMPLICIT);
    	
    	try
    	{
    		if(isElementPresent(locator))
	    	{
	    		isDisplayed = driver.findElement(locator).isDisplayed();	
	    	}
    	} 
 		catch (Exception e)
 		{
 			log.error("Exception occurred while determining state of " + locator + UtilBase.getStackTrace());
 		}	
    	return isDisplayed;
    }
    
    public boolean isElementEnabled(By locator)
    {
    	log.info("Verifying if element " + locator+ " is enabled");
    	boolean isEnabled = false;
    	setImplicitWait(TimeOut.IMPLICIT);
    	try
    	{
    		if(isElementPresent(locator))
        	{
    			isEnabled = driver.findElement(locator).isEnabled();
        	}
    	} 
 		catch (Exception e)
 		{
 			log.error("Exception occurred while determining state of " + locator + UtilBase.getStackTrace());
 		}
    	return isEnabled;
    }

    public boolean isElementSelected(By locator)
    {
    	log.info("Verifying if element " + locator+ " is selected");
    	boolean isSelected = false;
    	setImplicitWait(TimeOut.IMPLICIT);
    	try
    	{
    		if(isElementPresent(locator))
        	{
    			isSelected = driver.findElement(locator).isSelected();
           	}
    	}
 		catch (Exception e)
 		{
 			log.error("Exception occured while determining state of " + locator + UtilBase.getStackTrace());
 		}
    	return isSelected;
    }
    
    
	public int getTime(int[] optionalWaitArray)
	{
		if(optionalWaitArray.length<=0)
		{
			return TimeOut.MEDIUM;
		}
		else
		{
			return optionalWaitArray[0];
		}
	}
	
	public static void forFirefoxDownload(String dir) throws InterruptedException, IOException
    {
     boolean flag = false;
      Path _directoryToWatch = Paths.get(dir);
      File dirFolder = new File(dir);Assert.assertNotNull(dirFolder);
      for (File child : dirFolder.listFiles()) 
      {
    	  if(child.getCanonicalPath().endsWith(".part"))
    	  {
    		  flag = true;
    	  }
      }
      while(flag)
            {   
       WatchService watcherSvc = FileSystems.getDefault().newWatchService();
            
       WatchKey watchKey = _directoryToWatch.register(watcherSvc, ENTRY_DELETE);
            
       watchKey=watcherSvc.poll(60,TimeUnit.SECONDS);
       try
       {
                for (WatchEvent<?> event: watchKey.pollEvents()) {
                  WatchEvent.Kind<?> kind = event.kind();
                    if (kind == ENTRY_DELETE)
                    {
                     if (event.context().toString().endsWith(".part"))
                     {
                      System.out.println(".part file is deleted");
                      flag=false;
                      break;
                     }
                     else
                     {
                      System.out.println(event.context().toString()+" file is created");
                      continue;
                     }
                    }
                    watchKey.reset();
                }
         
       }
       catch(NullPointerException e)
       {
             System.out.println("timeout");
       }
      }
    }
	
	public void forFileCreation(String filePath)
	{
		 boolean flag = true;
		 int count = 1;
		 while(flag && (count <= 100)) {
			 File file = new File(filePath);
			 if(file.exists()&&file.length()>0) {
				 break;
			 } else {
				 Wait.sleep(250);
				 count = count+1;
				 log.info("waiting for file - "+filePath);
			 }
			 
		 }
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			one.tulasi.framework.wrapper.Assert.fail("thread was interuppted" + UtilBase.getStackTrace());
		}
	}
}
