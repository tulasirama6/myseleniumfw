package one.tulasi.framework.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserActions {
	
	private final WebDriver driver;
	private final Wait wait;
	private final Logger log = LogManager.getLogger(UserActions.class);
	
	public UserActions(WebDriver driver)
	{
		this.driver = driver;
		wait = new Wait(driver);
	}

	/**
	 * Method - Safe Method for User Click, waits until the element is loaded and then performs a click action
	 * @param locator
	 * @param optionWaitTime
	 */
	public void click(By locator, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.untilClickable(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element = driver.findElement(locator);		
			element.click();		
			log.info("Clicked on the element " + locator);
		}
		else
		{
			log.error("Unable to click the element " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to click the element " + locator+ UtilBase.getStackTrace());
		}
	}


	/**
	 * Method - Safe Method for User Double Click, waits until the element is loaded and then performs a double click action
	 * @param locator
	 * @param optionWaitTime
	 */
	public void doubleClick(By locator, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.untilClickable(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element = driver.findElement(locator);	
			Actions userAction = new Actions(driver).doubleClick(element);
			userAction.perform();
			log.info("Double clicked the element " + locator);
		}
		else
		{			
			log.error("Unable to double click the element " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to double click the element " + locator+ UtilBase.getStackTrace());
		}
	}

	
	
	/**
	 * Method - Safe Method for User Clear and Type, waits until the element is loaded and then enters some text
	 * @param locator
	 * @param text
	 * @param optionWaitTime
	 */
	public void clearAndType(By locator, String text, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element=driver.findElement(locator);		
			if(!text.equals(""))
			{
				element.clear();
				element.sendKeys(text);
			}
			log.info("Cleared the field and entered - '"+text+"' in the element - " + locator);
		}
		else
		{			
			log.error("Unable to clear and enter " + text + " in field "+locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to clear and enter " + text + " in field "+locator+ UtilBase.getStackTrace());
		}
	}

	 /** Method - Safe Method for user to Ccear, waits until the element is loaded and then clears text field
	 * @param locator
	 * @param optionWaitTime
	 */
	public void clear(By locator, int... optionWaitTime) {
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element=driver.findElement(locator);
			element.clear();
			log.info("Cleared the field - " + locator);
		}
		else
		{
			log.error("Unable to clear  field" + locator + UtilBase.getStackTrace());
			Assert.fail("Unable to clear field" + locator + UtilBase.getStackTrace());
		}
	}

	/**
	 * Method - Safe Method for User Type, waits until the element is loaded and then enters some text
	 * @param locator
	 * @param text
	 * @param optionWaitTime
	 */
	public void type(By locator, String text, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element=driver.findElement(locator);		
			element.sendKeys(text);
		}
		else
		{
			log.error("Unable to enter " + text + " in field " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to enter " + text + " in field " + locator+ UtilBase.getStackTrace());
		}
	}


	
	/**
	 * Method - Safe Method for Radio button selection, waits until the element is loaded and then selects Radio button
	 * @param locator
	 * @param optionWaitTime
	 * @return - boolean (returns True when the Radio button is selected else returns false)
	 * @throws Exception
	 */
	public void selectRadioButton(By locator, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.untilClickable(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element = driver.findElement(locator);			
			element.click();		
			log.info("Clicked on element " + locator);
		}
		else			
		{
			log.error("Unable to select Radio button "+locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to select Radio button "+locator+ UtilBase.getStackTrace());
		}	
	}
	
	
	/**
	 * Method - Safe Method for checkbox selection, waits until the element is loaded and then selects checkbox
	 * @param locator
	 * @param optionWaitTime
	 * @return - boolean (returns True when the checkbox is selected else returns false)
	 * @throws Exception
	 */
	public void check(By locator, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement checkBox = driver.findElement(locator);
			if(checkBox.isSelected())
				log.info("CheckBox " + locator + "is already selected");
			else
				checkBox.click();
		}
		else
		{			
			log.error("Unable to select checkbox " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to select checkbox " + locator+ UtilBase.getStackTrace());
		}	
	}

	

	
	/**
	 * Method - Safe Method for checkbox deselection, waits until the element is loaded and then deselects checkbox
	 * @param locator
	 * @param optionWaitTime
	 * @return - boolean (returns True when the checkbox is deselected else returns false)
	 * @throws Exception
	 */
	public void unCheck(By locator, int... optionWaitTime)
	{
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement checkBox = driver.findElement(locator);
			if(checkBox.isSelected())
				checkBox.click();
			else
				log.info("CheckBox " + locator + "is already deselected");

		}
		else
		{			
			log.error("Unable to uncheck the checkbox " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to uncheck the checkbox " + locator+ UtilBase.getStackTrace());
		}	
	}
	

	
	/**
	 * Method - Safe Method for checkbox Selection or Deselection based on user input, waits until the element is loaded and then deselects/selects checkbox
	 * @param locator
	 * @param checkOption
	 * @param optionWaitTime
	 * @return - boolean (returns True when the checkbox is status is toggled else returns false)
	 * @throws Exception
	 */
	public void checkByOption(By locator, boolean checkOption, int... optionWaitTime) {
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime)) {
			scrollIntoElementView(locator);
			WebElement checkBox = driver.findElement(locator);
			//noinspection PointlessBooleanExpression,PointlessBooleanExpression
			if((checkBox.isSelected() && !checkOption)||(!checkBox.isSelected() && checkOption))
			{
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBox);
			}
				
			else
				log.info("CheckBox " + locator + "is already deselected");
		}
		else {
			log.error("Unable to Select or Deselect checkbox " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to Select or Deselect checkbox " + locator+ UtilBase.getStackTrace());
		}		
	}
	
	
	/**
	 * Method - Safe Method for getting checkbox value, waits until the element is loaded and then deselects checkbox
	 * @param locator
	 * @param optionWaitTime
	 * @return - boolean (returns True when the checkbox is enabled else returns false)
	 * @throws Exception
	 */
	public boolean getCheckboxValue(By locator, int... optionWaitTime)
	{
		boolean isSelected = false;
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			wait.isElementPresent(locator, waitTime);
			scrollIntoElementView(locator);
			WebElement checkBox = driver.findElement(locator);
			if (checkBox.isSelected())		
				isSelected = true; 
		}
		else
		{			
			log.error("Unable to get the status of checkbox " + locator+ UtilBase.getStackTrace());
			Assert.fail("Unable to get the status of checkbox " + locator+ UtilBase.getStackTrace());
		}
		return isSelected;
	}
	
	
	/**
	 * Purpose- For selecting multiple check boxes at a time
	 * @param waitTime
	 * @param locator
	 * @functionCall - SelectMultipleCheckboxs(MEDIUMWAIT, By.id("Checkbox1"),By.id("Checkbox2"), By.xpath("checkbox")); u can pass 'N' number of locators at a time
	 */
	public void selectCheckboxes(int waitTime ,By... locator)
	  {			
		if(locator.length>0)
		{
			for(By currentLocator:locator)
			{  
				wait.untilClickable(currentLocator, waitTime);
				scrollIntoElementView(currentLocator);
				WebElement checkBox = driver.findElement(currentLocator);
				if(checkBox.isSelected())
					log.info("CheckBox " + currentLocator + " is already selected");
				else
					checkBox.click();
			}
		}
		else
		{
			log.error("Expected atleast one locator as argument to selectCheckboxes function"+ UtilBase.getStackTrace());
			Assert.fail("Expected atleast one locator as argument to selectCheckboxes function"+ UtilBase.getStackTrace());
		}
	  }
	
	/**
	 * Purpose- For deselecting multiple check boxes at a time
	 * @param waitTime
	 * @param locator
	 * @throws Exception
	 * @functionCall - DeselectMultipleCheckboxs(MEDIUMWAIT, By.id("Checkbox1"),By.id("Checkbox2"), By.xpath("checkbox")); u can pass 'N' number of locators at a time
	 */
	public void deselectCheckboxes(int waitTime ,By...locator)
	{	
		if(locator.length>0)
		{		
			for(By currentLocator:locator)
			{  
				wait.untilClickable(currentLocator,  waitTime);
				WebElement checkBox = driver.findElement(currentLocator);
				scrollIntoElementView(currentLocator);
				if(checkBox.isSelected())
					checkBox.click();
				else					
					log.info("CheckBox " + currentLocator + " is already deselected");
			}
		}
		else
		{
			log.error("Expected atleast one locator as argument to deselectCheckboxes function"+ UtilBase.getStackTrace());
			Assert.fail("Expected atleast one locator as argument to deselectCheckboxes function"+ UtilBase.getStackTrace());
		}
	}
	
	

	/**
	 * Method - Safe Method for User Select option from Drop down by option name, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param optionToSelect
	 * @param optionWaitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)
	 * @throws Exception
	 */
	public void selectOptionInDropDown(By locator, String optionToSelect, int... optionWaitTime) {
		List<WebElement> options;
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime)) {
			if(!optionToSelect.equals("")) {
			scrollIntoElementView(locator);
				WebElement selectElement = driver.findElement(locator); 
				Select select = new Select(selectElement); 
				//Get a list of the options 
				options = select.getOptions(); 
				// For each option in the list, verify if it's the one you want and then click it
				if(!options.isEmpty()) {
					for (WebElement option: options) {
						if (option.getText().contains(optionToSelect)) {
							option.click(); 
							log.info("Selected " + option + " from " + locator + " dropdown");
							break; 
						}
					}
				}
			}
		}
		else
		{
			log.error("Unable to select " + optionToSelect + " from " + locator + "\n" + UtilBase.getStackTrace());
			Assert.fail("Unable to select " + optionToSelect + " from " + locator + "\n" + UtilBase.getStackTrace());
		}
	}
	
	
	/**
	 * Method - Defining Selenium locator for working with duplicate elements when only 1 is active at a given time
	 * @param locator
	 * @return
	 * @throws Exception
	 */
	 public WebElement getActivelocatorInSet(By locator) {
		 wait.setImplicitWait(TimeOut.IMPLICIT);
		 WebElement activeElem = null;
		 int activeElemCount = 0;
		 try {
			 ArrayList<WebElement> elems = (ArrayList<WebElement>)driver.findElements(locator);
			 for (WebElement elem : elems) {
				 if (elem.isDisplayed()) {
					 if (++activeElemCount > 1)
						 log.error("More than 1 active visible locator found on page, expecting only 1" + UtilBase.getStackTrace());
					 else
						 activeElem = elem;
				 }
			 }
		 }
		 catch(NoSuchElementException e)
		 {
			 log.error("Element not found - "+ locator + UtilBase.getStackTrace());
			 Assert.fail("Element not found - "+ locator +  UtilBase.getStackTrace());
		 }
		 return activeElem;
	 }

	 
	/**
	 * Method - Safe Method for User Select option from Drop down by option index, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param iIndexofOptionToSelect
	 * @param optionWaitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)	
	 * @throws Exception
	 */
	public void selectOptionInDropDown(By locator, int iIndexofOptionToSelect, int... optionWaitTime)
	{ 
		int waitTime = wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement selectElement = driver.findElement(locator);
			Select select = new Select(selectElement);
			select.selectByIndex(iIndexofOptionToSelect);
			log.info("Selected option from " + locator + " dropdown");
		}
		else
		{
	    	log.error("Unable to select option from " + locator + "\n" + UtilBase.getStackTrace());
	    	Assert.fail("Unable to select option from " + locator + "\n" + UtilBase.getStackTrace());
		}
	} 
	
	/**
	 * Method - Safe Method for User Select option from Drop down by option value, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param valueOfOptionToSelect
	 * @param optionWaitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)	
	 * @throws Exception
	 */
	public void selectOptionInDropDownByValue(By locator, String valueOfOptionToSelect, int... optionWaitTime)
	{ 
		int waitTime = wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement selectElement = driver.findElement(locator);
			Select select = new Select(selectElement);
			select.selectByValue(valueOfOptionToSelect);
			log.info("Selected option from " + locator + " dropdown");
		}
		else
		{
	    	log.error("Unable to select option from " + locator + "\n" + UtilBase.getStackTrace());
	    	Assert.fail("Unable to select option from " + locator + "\n" + UtilBase.getStackTrace());
		}
	} 
	
	/**
	 * Method - Safe Method for User Select option from Drop down by option lable, waits until the element is loaded and then selects an option from drop down
	 * @param locator
	 * @param visibleTextOptionToSelect
	 * @param optionWaitTime
	 * @return - boolean (returns True when option is selected from the drop down else returns false)	
	 * @throws Exception
	 */
	public void selectOptionInDropDownByVisibleText(By locator, String visibleTextOptionToSelect, int... optionWaitTime)
	{ 
		int waitTime = wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			if(!visibleTextOptionToSelect.equals(""))
			{	
				scrollIntoElementView(locator);
				WebElement selectElement = driver.findElement(locator);
				Select select = new Select(selectElement);
				select.selectByVisibleText(visibleTextOptionToSelect);
				log.info("Selected option from " + locator + " dropdown");
			}
		}
		else
		{
	    	log.error("Unable to select option from " + locator + "\n" + UtilBase.getStackTrace());
	    	Assert.fail("Unable to select option from " + locator + "\n" + UtilBase.getStackTrace());
		}
	} 
	
	
	/**
	 * Method - Safe Method for User Select option from list menu, waits until the element is loaded and then selects an option from list menu
	 * @param locator
	 * @param optionToSelect
	 * @param optionWaitTime
	 * @return
	 * @throws Exception
	 */
	public void selectListBox(By locator, String optionToSelect, int... optionWaitTime)
    { 
		int waitTime =  wait.getTime(optionWaitTime);
		List<WebElement> options;
		if(wait.isElementPresent(locator, waitTime))
		{
			//First, get the WebElement for the select tag 
			WebElement selectElement = driver.findElement(locator); 
			//Then instantiate the Select class with that WebElement 
			Select select = new Select(selectElement); 
			//Get a list of the options 
			options = select.getOptions(); 
			if(!options.isEmpty())
			{
				// For each option in the list, verify if it's the one you want and then click it 
				for (WebElement option: options) 
				{ 
					if (option.getText().contains(optionToSelect))
					{ 
						option.click(); 
						log.info("Selected " + option + " from " + locator + " Listbox");
						break; 
					}
					log.error("Unable to select " + option + " from " + locator + "\n"+ UtilBase.getStackTrace());
					Assert.fail("Unable to select " + option + " from " + locator + "\n"+ UtilBase.getStackTrace());
				}
			}
		}
    }
	
	
	
	/**
	 * Method - Method to hover on an element based on locator using Actions,it waits until the element is loaded and then hovers on the element
	 * @param locator
	 * @param waitTime
	 * @throws Exception
	 */
	public void mouseHover(By locator,int waitTime)
	{
	    if(wait.isElementVisible(locator, waitTime))
		{
		    Actions builder = new Actions(driver);
		    WebElement HoverElement = driver.findElement(locator);
		    builder.moveToElement(HoverElement).build().perform();
		    try {
		    	builder.wait(4000);
			} catch (InterruptedException e) {
				log.error("Exception occurred while waiting"+ UtilBase.getStackTrace());
			}
		    log.info("Hovered on element " + locator);
	    }
	    else
		{	    
			log.error("Element was not visible to hover "+"\n"+ UtilBase.getStackTrace());
			Assert.fail("Element was not visible to hover "+"\n"+ UtilBase.getStackTrace());
		}
	}
	

	/**
	 * Method - Method to hover on an element based on locator using Actions and click on given option,it waits until the element is loaded and then hovers on the element
	 * @param locator
	 * @param waitTime
	 * @throws Exception
	 */
	public void mouseHoverAndSelectOption(By locator,By byOptionlocator,int waitTime)
	{
	    if(wait.isElementPresent(locator, waitTime))
		{
		    Actions builder = new Actions(driver);
		    WebElement HoverElement = driver.findElement(locator);
		    builder.moveToElement(HoverElement).build().perform();
		    try {
		    	builder.wait(4000);
			} catch (InterruptedException e) {
				log.error("Exception occurred while waiting"+ UtilBase.getStackTrace());
			}
		    WebElement element = driver.findElement(byOptionlocator);
		    element.click();
		    log.info("Hovered on element and select the Option" + locator);
	    }
	    else
		{	    
			log.error("Element was not visible to hover "+"\n"+ UtilBase.getStackTrace());
			Assert.fail("Element was not visible to hover "+"\n"+ UtilBase.getStackTrace());
		}
	}

	/**
	 * Method - Method to hover on an element based on locator using JavaScript snippet,it waits until the element is loaded and then hovers on the element
	 * @param locator
	 * @param Choice
	 * @param waitTime
	 * @throws Exception
	 */
	public void mouseHoverJScript(By locator,String Choice,int waitTime)
	{
		try 
		{
			wait.isElementPresent(locator, waitTime);	        
		    WebElement HoverElement = driver.findElement(locator);
		    String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		    ((JavascriptExecutor) driver).executeScript(mouseOverScript, HoverElement);
		    Thread.sleep(4000);	
		    log.info("Hovered on element " + locator);
		}
		catch(Exception e)
		{		    
			e.printStackTrace();
			log.error("Some exception occurred while hovering"+ UtilBase.getStackTrace());
			Assert.fail("Some exception occurred while hovering"+ UtilBase.getStackTrace());
		}
	}

	
	/**
	 * Method - Safe Method for User Click, waits until the element is loaded and then performs a click action
	 * @param locatorToClick
	 * @param locatorToCheck
	 * @param waitElementToClick
	 * @param waitElementToCheck
	 * @return - boolean (returns True when click action is performed else returns false)
	 */
	public void click(By locatorToClick, By locatorToCheck, int waitElementToClick,int waitElementToCheck ) {
		boolean clicked = false;
        int iAttempts = 0;
        wait.nullifyImplicitWait();
        WebDriverWait wait1 = new WebDriverWait(driver, waitElementToClick);
        WebDriverWait wait2 = new WebDriverWait(driver,waitElementToCheck);
        while(iAttempts < 3) {
            try {
              	wait1.until(ExpectedConditions.visibilityOfElementLocated(locatorToClick));
            	wait1.until(ExpectedConditions.elementToBeClickable(locatorToClick));
    			WebElement element = driver.findElement(locatorToClick);
    			
    			if(element.isDisplayed()) {
        			element.click();
        			wait.forPageToLoad();
        			wait.forJQueryProcessing(waitElementToCheck);
        			wait2.until(ExpectedConditions.visibilityOfElementLocated(locatorToCheck));
        			WebElement elementToCheck = driver.findElement(locatorToCheck);
        			if(elementToCheck.isDisplayed()) {
                        log.info("Clicked on element " + locatorToClick);
						clicked = true;
        				break;
        			} else {
        				Thread.sleep(1000);
        				continue;
        			}
    			}
            } catch(Exception e) {
            	log.info("Attempt: "+iAttempts +"\n Unable to click on element " + locatorToClick);
            }
            iAttempts++;
        }
        if (!clicked) {
        	Assert.fail("Unable to click on element " + locatorToClick+ UtilBase.getStackTrace());
        }
	}
	
	
	/**
	 * Purpose- Method For performing drag and drop operations 
	 * @param Sourcelocator,Destinationlocator
	 * @param waitTime
	 * @function_call - eg: DragAndDrop(By.id(Sourcelocator), By.xpath(Destinationlocator), "MEDIUMWAIT");
	 */
	public void dragAndDrop(By Sourcelocator, By Destinationlocator, int waitTime)		{
		  try {
			  wait.isElementPresent(Sourcelocator, waitTime);  
			  WebElement source = driver.findElement(Sourcelocator);
			  wait.isElementPresent(Destinationlocator, waitTime);
			  WebElement destination = driver.findElement(Destinationlocator);
			  Actions action = new Actions(driver);
			  action.dragAndDrop(source, destination).build().perform();
			  log.info("Dragged the element "+ Sourcelocator + " and dropped in to " + Destinationlocator);
		  } catch(Exception e) {
			  log.error("Some exception occurred while performing drag and drop operation "+ UtilBase.getStackTrace());
			  Assert.fail("Some exception occurred while performing drag and drop operation "+ UtilBase.getStackTrace());
		  }
	  }

	/**
	 * Method: for verifying if accept exists and accepting the alert
	 * @return - boolean (returns True when accept action is performed else returns false)
	 */
	public void acceptAlert() {
		try {
		  Alert alert = driver.switchTo().alert();
		   alert.accept();
		   log.info("Accepted the alert:"+ alert.getText());
		} catch(Exception e) {
			log.error("Unable to accept the alert."+ UtilBase.getStackTrace());
			Assert.fail("Unable to accept the alert."+ UtilBase.getStackTrace());
		}
	}
	

	
	/**
	 * Method: for verifying if accept exists and rejecting/dismissing the alert
	 * @return - boolean (returns True when dismiss action is performed else returns false)
	 */
	public void dismissAlert() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
			log.info("Dismissed the alert:"+ alert.getText());
		} catch(Exception e) {
			log.error("Unable to dismiss the alert."+ UtilBase.getStackTrace());
			Assert.fail("Unable to accept the alert."+ UtilBase.getStackTrace());
		}

	}	
	
	
	/**
	 * Purpose - To select the context menu option for the given element
	 * @param locator
	 * @param iOptionIndex
	 * @param waitTime
	 * @throws Exception
	 */
	public void selectContextMenuOption(By locator, int iOptionIndex, int waitTime)
	{
	    try
	    {
	    	wait.isElementPresent(locator, waitTime);
			selectContextMenuOption(locator, iOptionIndex);
	    }
	    catch(Exception e)
	    {
			log.error("Unable to select context menu option"+ UtilBase.getStackTrace());
		   	Assert.fail("Unable to select context menu option"+ UtilBase.getStackTrace());
	    }
	}
	
	
	private void selectContextMenuOption(By locator, int iOptionIndex) 
	{		
		WebElement Element = driver.findElement(locator);
		Actions _action = new Actions(driver);
		for (int count=1; count<=iOptionIndex; count++)
		{
			_action.contextClick(Element).sendKeys(Keys.ARROW_DOWN);
		}
		_action.contextClick(Element).sendKeys(Keys.RETURN).build().perform();
	}
    
		

	/**
	 * Method: for uploading file
	 * @return - boolean (returns True when upload is successful else returns false)
	 */
	public boolean uploadFile(By locator, String filePath, int... optionWaitTime) {
		boolean hasTyped = false;
		int waitTime =  wait.getTime(optionWaitTime);
		if(wait.isElementPresent(locator, waitTime))
		{
			scrollIntoElementView(locator);
			WebElement element=driver.findElement(locator);
			
			element.sendKeys(filePath);
			log.info("Entered - '"+filePath+" in the element - " + locator);
			hasTyped = true;
		}
		else
		{			
			log.error("Unable to upload file - "+filePath+" using upload field - "+locator);
			Assert.fail("Unable to upload file - "+filePath+" using upload field - "+locator);
		}
		return hasTyped;			
	}	
	
	
	
	/**
	 * Method to paste the text using key strokes
	 */
	public void pasteCopiedText()
	{
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
			log.error("Could not paste due an exception + " + e.getCause() + "\n" + UtilBase.getStackTrace());
		}
	}

	/**
	 * Method to paste the text using key strokes
	 * @param robot
	 */
	public void pasteCopiedText(Robot robot)
	{
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	
	/**
	 * Method to copy the given text to clip board
	 * @param sText
	 */
	public void copyTextToClipboard(String sText)
	{
		StringSelection stringSelection = new StringSelection(sText);
		Clipboard _clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		_clpbrd.setContents(stringSelection, null);
	}
	
	/**
	 * Method: for uploading file by using Robot class
	 * @return - null
	 */
	public void uploadFileRobot(By slocator, String sFileLocation, int waitTime) {
		try
		{
			wait.isElementPresent(slocator, waitTime);
		copyTextToClipboard(sFileLocation); 		
		Actions builder = new Actions(driver);

		Action myAction = builder.click(driver.findElement(slocator))
		       .release()
		       .build();
		 
		    myAction.perform();
		    Robot robot = new Robot();
		    pasteCopiedText(robot);
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    Thread.sleep(20000);
		  
		}
		catch (Exception e)
		{
			log.error(Arrays.toString(e.getStackTrace()));
		}
		
	}
	
	/**
	 * 
	 * JavaScript method for clicking on an element
	 *
	 * @param locator -locator value by which element is recognized
	 * @param waitTime - Time to wait for an element
	 * @return
	 * @throws Exception
	 */
	public void javaScriptClick(By locator, int waitTime)
	{
		try
		{
			wait.untilClickable(locator,waitTime);
			scrollIntoElementView(locator);
			WebElement element = driver.findElement(locator);
			
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}

		catch (Exception e)
		{
			log.error("Unable to click on element " + locator + UtilBase.getStackTrace());
			Assert.fail("Unable to click on element " + locator + UtilBase.getStackTrace());
		}
	}
	
	/**
	 * 
	 * JavaScript method for entering a text in a field
	 *
	 * @param locator - locator value by which text field is recognized
	 * @param sText - ConfigManager to be entered in a field
	 * @param waitTime - Time to wait for an element
	 */
	public void javaScriptType(By locator,String sText, int waitTime) {
		try
		{
			if(wait.isElementPresent(locator, waitTime))
			{
				scrollIntoElementView(locator);
				WebElement element = driver.findElement(locator);
				
				((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '"+sText+"');",element);
			}
		}

		catch (Exception e)
		{
			log.error("Unable to enter " + sText + " in field " + locator + UtilBase.getStackTrace());
			Assert.fail("Unable to enter " + sText + " in field " + locator + UtilBase.getStackTrace());
		}
	}
	
	/**
	 * 
	 * JavaScript Safe Method for Clear and Type
	 *
	 * @param locator - locator value by which text field is recognized
	 * @param sText - ConfigManager to be entered in a field
	 * @param waitTime - Time to wait for an element
	 * @return
	 * @throws Exception
	 */
	public void javaScriptClearType(By locator,String sText, int waitTime)
	{
		try
		{
			wait.isElementPresent(locator, waitTime);
			scrollIntoElementView(locator);
			WebElement element = driver.findElement(locator);
			
			((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '');",element);
			((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '"+sText+"');",element);
		}

		catch (Exception e)
		{	
			log.error("Unable to enter " + sText + " in field " + locator + UtilBase.getStackTrace());
			Assert.fail("Unable to enter " + sText + " in field " + locator + "\n"+ UtilBase.getStackTrace());
		}
	}
	
	/**
	 * 
	 * Safe method to get the attribute value
	 *
	 * @param locator - locator value by which an element is located
	 * @param attributeName - attribute type
	 * @param waitTime - Time to wait for an element
	 * @return - returns the attribute value of type string
	 */
	public String getAttribute(By locator, String attributeName, int waitTime)
	{
		String value = "";
		try
		{
			if(wait.isElementPresent(locator, waitTime))
			{
				value = driver.findElement(locator).getAttribute(attributeName);
			}
			else
			{
				log.error("Unable to get attribute value of type " + attributeName + " from the element "+ locator+ "as element is not found\n"+ UtilBase.getStackTrace());
				Assert.fail("Unable to get attribute value of type " + attributeName + " from the element "+ locator+ "as element is not found\n"+ UtilBase.getStackTrace());
			}
		}
		catch(Exception e)
		{
			log.error("Unable to get attribute value of type " + attributeName + " from the element "+ locator+ "\n"+ UtilBase.getStackTrace());
			Assert.fail("Unable to get attribute value of type " + attributeName + " from the element "+ locator+ "\n"+ UtilBase.getStackTrace());
		}		
		return value;		
	}
	
	/**
	 * 
	 * Safe method to get the attribute value
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns the attribute value of type string
	 */
	public String getValue(By locator, int waitTime)
	{
		return getAttribute(locator, "value", waitTime);
	}
	
	/**
	 * 
	 * Safe method to get text from an element
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns the text value from element
	 */
	public String getText(By locator,int waitTime)
	{
		String sValue = "";
		try
		{
			if(wait.isElementPresent(locator, waitTime))
			{
				sValue = driver.findElement(locator).getText();
			}
			else
			{
				log.error("Unable to get the text as element is not present "+ locator+ "\n"+ UtilBase.getStackTrace());
				Assert.fail("Unable to get the text as element is not present "+ locator+ "\n"+ UtilBase.getStackTrace());
			}
			
		}
		catch(Exception e)
		{
			log.error("Unable to get the text from the element "+ locator+ "\n"+ UtilBase.getStackTrace());
			Assert.fail("Unable to get the text from the element "+ locator+ "\n"+ UtilBase.getStackTrace());
		}		
		return sValue;		
	}
	
	
	public String getTagName(By locator,int waitTime)
	{
		String sValue = "";
		try
		{
			if(wait.isElementPresent(locator, waitTime))
			{
				sValue = driver.findElement(locator).getTagName();
			}
			else
			{
				Assert.fail("Unable to get the text from the element "+ locator);
			}
			
		}
		catch(Exception e)
		{
			log.error("Unable to get the text from the element "+ locator+ "\n"+ UtilBase.getStackTrace());
			Assert.fail("Unable to get the text from the element "+ locator+ "\n"+ UtilBase.getStackTrace());
		}		
		return sValue;		
	}
	
	/**
	 * 
	 * scroll method to scroll the page down until expected element is visible	 *
	 * @param locator - locator value by which an element is located
	 */
	public void scrollIntoElementView(By locator)
	{
		try
		{
			WebElement element = driver.findElement(locator);
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);window.scrollBy(0," + (-200) + ");",element);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("Unable to scroll the page to find "+ locator+ "\n"+ UtilBase.getStackTrace());
			Assert.fail("Unable to scroll the page to find "+ locator+ "\n"+ UtilBase.getStackTrace());
		}
	}
	
	public void scrollPage(int size)
	{
		try
		{
			JavascriptExecutor jsx = (JavascriptExecutor)driver;
			jsx.executeScript("window.scrollBy(0,"+size+")", "");
		}
		catch(Exception e)
		{
			log.error("Unable to scroll page "+e.getCause()+"\n"+ UtilBase.getStackTrace());
 		   	Assert.fail("Unable to scroll page "+e.getCause()+"\n"+ UtilBase.getStackTrace());
		}
		
	}
	
	/**
	 * 
	 * JavaScript Safe method to get the attribute value
	 *
	 * @param locator - locator value by which an element is located
	 * @param sAttributeValue - attribute type
	 * @param waitTime - Time to wait for an element
	 * @return - returns the attribute value of type string
	 */
	public String javaScriptGetAttribute(By locator,String sAttributeValue,int waitTime)
	{
		String sValue ="";
		try
		{
			if(wait.isElementPresent(locator, waitTime))
			{
				final String scriptGetValue = "return arguments[0].getAttribute('"+sAttributeValue+"')";
				WebElement element = driver.findElement(locator);			
				sValue = (String)((JavascriptExecutor) driver).executeScript(scriptGetValue,element);
			}
		}
		catch(Exception e)
		{
			Assert.fail("Unable to get attribute value of type " + sAttributeValue + " from the element "+ locator+ UtilBase.getStackTrace());
		}		
		return sValue;		
	} 

	/**
	 * 
	 * Safe method to retrieve the option selected in the drop down list
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns the option selected in the drop down list
	 */
	public String getSelectedOptionInDropDown(By locator, int waitTime)
	{
		String dropDownSelectedValue = null;
		
		try
		{
			//return getSelectedOptionInDropDown(locator, sWaitTime);
			wait.isElementPresent(locator, waitTime);
			Select dropDownName = new Select(driver.findElement(locator));
			dropDownSelectedValue = dropDownName.getFirstSelectedOption().getText();			
		}
		catch(Exception e)
		{
 			log.error("Unable to retrieve drop down field value:"+locator+ UtilBase.getStackTrace());
 		   	Assert.fail("Unable to retrieve drop down field value:"+locator+ UtilBase.getStackTrace());
		}
		return dropDownSelectedValue;
	}
	
	public List<String> getAllOptionsInDropDown(By locator, int sWaitTime) {
		List<WebElement> dropDownElements;
		List<String> dropDownValues = new ArrayList<>();
		try
		{
			//return getSelectedOptionInDropDown(locator, sWaitTime);
			wait.isElementPresent(locator, sWaitTime);
			Select dropDownName = new Select(driver.findElement(locator));
			dropDownElements = dropDownName.getOptions();

			if(dropDownElements.size()>0)
			{
				for(WebElement option:dropDownElements)
				{
					dropDownValues.add(option.getText());
				}
			}
		}
		catch(Exception e)
		{
 			log.error("Unable to retrieve drop down field options:"+locator+ UtilBase.getStackTrace());
 		   	Assert.fail("Unable to retrieve drop down field options:"+locator+ UtilBase.getStackTrace());
		}
		return dropDownValues;
	}
	
	/**
	 * 
	 * Safe method to verify whether the element is exists in the list box or not
	 *
	 * @param locator - locator value by which an element is located
	 * @param waitTime - Time to wait for an element
	 * @return - returns 'true' if the mentioned value exists in the list box else returns 'false'
	 * 
	 */	
	public boolean verifyListBoxValue(By locator, String value, int waitTime) {
		boolean isExpected = false;
		try
		{						
			if(wait.isElementPresent(locator, waitTime))
			{
				WebElement listBox = driver.findElement(locator);
				java.util.List<WebElement> listBoxItems = listBox.findElements(By.tagName("li"));			            
			    for(WebElement item : listBoxItems)
			    {
			    	if(item.getText().equals(value))      		
			    		isExpected = true;
			    }
				
			}
		
		}
		catch(Exception e)
		{
 			log.error("Unable to verify Listbox value:"+locator+ UtilBase.getStackTrace());
 		   	Assert.fail("Unable to verify Listbox value:"+locator+ UtilBase.getStackTrace());
		}
		return isExpected;
	}
	
	
	/**
	 * Method for switching to frame using frame id
	 * @param driver
	 * @param frame
	 */
	public void selectFrame(WebDriver driver, String frame)
	{
		try
		{
			driver.switchTo().frame(frame);
			log.info("Navigated to frame with id " + frame);	
		}
		catch(Exception e)
		{
			log.error("Unable to navigate to frame with id " + frame + UtilBase.getStackTrace());
			Assert.fail("Unable to navigate to frame with id " + frame + UtilBase.getStackTrace());
		}
	}
	

	/**
	 * Method - Method for switching to frame in a frame
	 * @param driver
	 * @param ParentFrame
	 * @param ChildFrame
	 */
	public void selectFrame(WebDriver driver, String ParentFrame, String ChildFrame)
	{
		try
		{
			driver.switchTo().frame(ParentFrame).switchTo().frame(ChildFrame);
			log.info("Navigated to innerframe with id " + ChildFrame + "which is present on frame with id" + ParentFrame);
		}
		catch(Exception e)
		{
			log.error("Unable to navigate to innerframe with id " + ChildFrame + "which is present on frame with id" + ParentFrame + UtilBase.getStackTrace());
			Assert.fail("Unable to navigate to innerframe with id " + ChildFrame + "which is present on frame with id" + ParentFrame + UtilBase.getStackTrace());
		}
	}

	
	/**
	 * Method - Method for switching to frame using any locator of the frame
	 * @param driver
	 * @param Framelocator
	 * @param waitTime
	 */
	public void selectFrame(WebDriver driver, By Framelocator, int waitTime)
	{
		try
		{
			if(wait.isElementPresent(Framelocator,waitTime))
			{
				WebElement Frame = driver.findElement(Framelocator);             
			    driver.switchTo().frame(Frame);
			    log.info("Navigated to frame with locator " + Framelocator);	
			}
		}
		catch(Exception e)
		{
			log.error("Unable to navigate to frame with locator " + Framelocator + UtilBase.getStackTrace());
			Assert.fail("Unable to navigate to frame with locator " + Framelocator + UtilBase.getStackTrace());
		}
	}
	
	
	/**
	 * Method - Method for switching back to webpage from frame
	 * @param driver
	 */
	public void defaultFrame(WebDriver driver)
	{
		try
		{
			driver.switchTo().defaultContent();
			log.info("Navigated to back to webpage from frame");
		}
		catch(Exception e)
		{
			log.error("unable to navigate back to main webpage from frame"+ UtilBase.getStackTrace());
			Assert.fail("unable to navigate back to main webpage from frame"+ UtilBase.getStackTrace());
		}
	}
	
	/**
	@Method Highlights on current working element or locator
	@param element
	@return void (nothing)
	*/
	public void setHighlight(WebElement element)
	{

	        String attributevalue = "border:3px solid red;";
	        JavascriptExecutor executor = (JavascriptExecutor) driver;
	        String getattrib = element.getAttribute("style");
	        executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
	        try 
	        {
				Thread.sleep(100);
			} 
	        catch (InterruptedException e) 
			{
				log.error("Sleep interrupted - "+ UtilBase.getStackTrace());
			}
	        executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);
	}       
	

	/**
	 * Method: Highlights on current working element or locator
	 * @param element
	 * @throws Exception
	 */
	public void highlightElement(WebElement element) throws Exception
	{
        String attributevalue="border:3px solid green;";//change border width and color values if required
        JavascriptExecutor executor= (JavascriptExecutor) driver;
        String getattrib=element.getAttribute("style");
        executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
        Thread.sleep(100);
        executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);     
    }
	
	public void jsClickOnElement(String cssSelector)
	{
        JavascriptExecutor js = (JavascriptExecutor) driver;
		String stringBuilder = ("var x = $(\'" + cssSelector + "\');") +
				"x.click();";
		js.executeScript(stringBuilder);
    }
	
	/**
	 * Method - Safe Method for getting checkbox value, waits until the element is loaded and then deselects checkbox
	 * @param locator
	 * @param waitTime
	 * @return - boolean (returns True when the checkbox is enabled else returns false)
	 * @throws Exception
	 */
	public boolean getRadioButtonState(By locator, int waitTime)
	{
		try
		{
			wait.isElementPresent(locator, waitTime);
			scrollIntoElementView(locator);
			WebElement RadioButton = driver.findElement(locator);
            return RadioButton.isEnabled();
		}
		catch(Exception e)
		{			
			log.error("Radio button is not enabled " + locator +"\n" +e.getMessage());
			Assert.fail("Radio button is not enabled " + locator);
		}
		return false;
	}
	
	public void pressEnter() {
		try
		{
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ENTER);
			r.keyRelease(KeyEvent.VK_ENTER);
		}
		catch(AWTException e)
		{
			log.error("Exception occurred while trying to click ENTER Key");
		}
	}
	
	public void pressEscape()
	{
		try
		{
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ESCAPE);
			r.keyRelease(KeyEvent.VK_ESCAPE);
		}
		catch(AWTException e)
		{
			log.error("Exception occurred while trying to click EXCAPE Key");
		}
		
	}
	
	
    public void drawOnCanvas(By locator)
    {
        try
        {
            Actions actionBuilder = new Actions(driver);
            Action drawOnCanvas = actionBuilder.clickAndHold(driver.findElement(locator))
                .moveByOffset(15, 15)
                .moveByOffset(4, -4)
                .moveByOffset(-7, 10)
                .release(driver.findElement(locator))
                .build();

               drawOnCanvas.perform();
        }
        catch (Exception e)
        {
            log.error("Exception occured when drawing on element: "+locator+" Exception is "+e.getCause()+ UtilBase.getStackTrace());
        }
    }
    
    
    public void setAttribute(By locator, String attribute, String value)
	{
	 if(wait.isElementPresent(locator))
	 {
		 ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute("+attribute+","+value+");",driver.findElement(locator));
	 }
	}
    
    public void setStyle(By locator, String value)
	{
	 if(wait.isElementPresent(locator))
	 {
		 ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style',arguments[1]);",driver.findElement(locator),value);
	 }
	}
    
    public String getCssValue(By locator, String cssProperty) {
    	if(wait.isElementPresent(locator)) {
    		return driver.findElement(locator).getCssValue(cssProperty);
    	}
    	else {
    		return null;
    	}
    }
}
