package one.tulasi.framework.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
 
public class Assert {
	private static final Logger log = LogManager.getLogger("Assert");
	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<>();

	public static void fail(String message) {
		log.error(message);
		org.testng.Assert.fail(message+"</br>");
	}
	
    public static void assertTrue(boolean condition) {
    	org.testng.Assert.assertTrue(condition);
    }
    
    public static void assertTrue(boolean condition, String message) {
    	org.testng.Assert.assertTrue(condition, message);
    }
    
    public static void assertFalse(boolean condition) {
    	org.testng.Assert.assertFalse(condition);
    }
    
    public static void assertFalse(boolean condition, String message) {
    	org.testng.Assert.assertFalse(condition, message);
    }
    
    public static void assertEquals(boolean actual, boolean expected) {
    	org.testng.Assert.assertEquals(actual, expected);
    }
    
    public static void assertEquals(boolean actual, boolean expected, String message) {
    	org.testng.Assert.assertEquals(actual, expected, message);
    }
    
    public static void assertEquals(Object actual, Object expected) {
    	org.testng.Assert.assertEquals(actual, expected);
    }
    
    public static void assertNotEquals(Object actual, Object expected) {
    	org.testng.Assert.assertNotEquals(actual, expected);
    }
    
    public static void assertEquals(Object actual, Object expected, String message) {
    	org.testng.Assert.assertEquals(actual, expected, message);
    }
    
    public static void assertEquals(String actual, String expected, String type, String message) {
    	if(type.equalsIgnoreCase("date"))
    	try {
    		SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
        	Date actualDate = formatter.parse(actual);
        	Date expectedDate = formatter.parse(expected);
        	if(actualDate.compareTo(expectedDate)!=0) {
        		fail(message);
        	}      	
    	}catch(ParseException e) {
    		assertEquals(actual, expected, message);
    	}
    	
    }
    
    public static void assertEquals(Object[] actual, Object[] expected) {
    	org.testng.Assert.assertEquals(actual, expected);
    }
    
    public static void assertEquals(Object[] actual, Object[] expected, String message) {
    	org.testng.Assert.assertEquals(actual, expected, message);
    }
    
    public static void verifyTrue(boolean condition) {
    	try {
    		assertTrue(condition);
    	} 
    	catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyTrue(boolean condition, String message) {
    	try {
    		assertTrue(condition, message);
    	} 
    	catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyFalse(boolean condition) {
    	try {
    		assertFalse(condition);
		} 
    	catch(Throwable e) {
    		addVerificationFailure(e);
		}
    }
    
    public static void verifyFalse(boolean condition, String message) {
    	try {
    		assertFalse(condition, message);
    	} 
    	catch(Throwable e) {
    		addVerificationFailure(e);
    	}
    }
    
    public static void verifyEquals(boolean actual, boolean expected, String message) {
    	try {
    		assertEquals(actual, expected);
		} 
    	catch(Throwable e) {
			addVerificationFailure(e, message);
			log.error("Expected value is ["+expected+"] but found actual value as ["+actual+"]");
    		Reporter.log("Expected value is [<b>"+expected+"</b>] but found actual value as [<b>"+actual+"</b>]</br></br>");
		}
    }

    public static void verifyEquals(Object actual, Object expected, String message) {
    	try {
    		assertEquals(actual, expected);
		} 
    	catch(Throwable e) {
    		addVerificationFailure(e, message);
    		log.error("Expected value is ["+expected+"] but found actual value as ["+actual+"]");
    		Reporter.log("Expected value is [<b>"+expected+"</b>] but found actual value as [<b>"+actual+"</b>]</br></br>");
		}
    }
    
    public static void verifyNotEquals(Object actual, Object expected, String message) {
    	try {
    		assertNotEquals(actual, expected);
		} 
    	catch(Throwable e) {
    		addVerificationFailure(e, message);
    		log.error("Expected value is ["+expected+"] but found actual value as ["+actual+"]");
    		Reporter.log("Expected value is [<b>"+expected+"</b>] but found actual value as [<b>"+actual+"</b>]</br></br>");
		}
    }
    
    public static void verifyEquals(String actual, String expected, String type, String message) {
    	try {
    		assertEquals(actual, expected, type, message);
		} 
    	catch(Throwable e) {
    		addVerificationFailure(e, message);
    		log.error("Expected value is ["+expected+"] but found actual value as ["+actual+"]");
    		Reporter.log("Expected value is [<b>"+expected+"</b>] but found actual value as [<b>"+actual+"</b>]</br></br>");
		}
    }
    
    
    public static void verifyEquals(Object[] actual, Object[] expected, String message) {
    	try {
    		assertEquals(actual, expected);
		} 
    	catch(Throwable e) {
			addVerificationFailure(e, message);
		}
    }
    
	public static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<>() : verificationFailures;
	}
	
	private static void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}
	
	private static void addVerificationFailure(Throwable e, String message) {
		log.error(message);
		Reporter.log(message+"</br>");
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}
}