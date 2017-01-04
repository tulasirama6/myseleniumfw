package one.tulasi.framework.listener;

import one.tulasi.framework.core.Screenshot;
import one.tulasi.framework.util.ReportSetup;
import one.tulasi.framework.wrapper.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.internal.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TestListener extends TestListenerAdapter implements IInvokedMethodListener
{
	
	private static final char quote = '"';
	private static final Logger log = LogManager.getLogger(TestListener.class);
   	
	/**
	 * Purpose - For attaching captured screenshots in ReportNG report 
	 */
	@Override
	public void onTestFailure(ITestResult result)
	{
		log.error("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" );
		log.error("ERROR ----------"+result.getName()+" has failed-----------------" );
		log.error("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" );

		ITestContext context = result.getTestContext();
		WebDriver driver = (WebDriver)context.getAttribute("driver");
		System.setProperty("org.uncommons.reportng.escape-output", "false"); //ReportNG related
		Reporter.setCurrentTestResult(result);
		Screenshot screenshot = new Screenshot(driver, result.getName() + System.currentTimeMillis());
		Reporter.log("<a href="+ quote +screenshot.getAbsolutePath()+ quote +">"+" <img src="+ quote +screenshot.getAbsolutePath()+ quote +" height=48 width=48 ></a>");
		Reporter.setCurrentTestResult(null);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" );
		log.warn("WARN ------------"+result.getName()+" has skipped-----------------" );
		log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" );			
		Reporter.setCurrentTestResult(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		log.info("###############################################################" );
		log.info("SUCCESS ---------"+result.getName()+" has passed-----------------" );
		log.info("###############################################################" );
		Reporter.setCurrentTestResult(result);
	}

	@Override
	public void onTestStart(ITestResult result) {
		log.info("****************************************************************" );
		log.info("**************CURRENTLY RUNNING TEST************ "+result.getName() );
		log.info("****************************************************************" );
	}

	@Override
	public void onStart(ITestContext context) {
		log.info("****************************************************************" );
		log.info("**************STARTED RUNNING SUITE************ " + context.getSuite().getName() );
		log.info("****************************************************************" );
	}

	@Override
	public void onFinish(ITestContext context) {
		Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
		for (ITestResult temp : failedTests) {
			ITestNGMethod method = temp.getMethod();
			if (context.getFailedTests().getResults(method).size() > 1) {
				failedTests.remove(temp);
			} else {
				if (context.getPassedTests().getResults(method).size() > 0) {
					failedTests.remove(temp);
				}
			}
		}
		log.info("****************************************************************" );
		log.info("**************FINISHED RUNNING SUITE************ " + context.getSuite().getName() );
		log.info("****************************************************************" );
	}

	@Override
	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		Reporter.setCurrentTestResult(result);
		if (method.isTestMethod()) {
			List<Throwable> verificationFailures = Assert.getVerificationFailures();
			if (verificationFailures.size() > 0) { //if there are verification failures...
				result.setStatus(ITestResult.FAILURE); //set the test to failed
				if (result.getThrowable() != null) { //if there is an assertion failure add it to verificationFailures
					verificationFailures.add(result.getThrowable());
				}
				int size = verificationFailures.size();
				if (size == 1) { //if there's only one failure just set that
					result.setThrowable(verificationFailures.get(0));
				} else { //create a failure message with all failures and stack traces (except last failure)
					StringBuilder failureMessage = new StringBuilder("Multiple failures (").append(size-1).append("):\n\n");
					for (int i = 0; i < size-1; i++) {
						failureMessage.append("Failure ").append(i+1).append(" of ").append(size-1).append(":\n");
						Throwable t = verificationFailures.get(i);
						String fullStackTrace = Utils.stackTrace(t, false)[1];
						failureMessage.append(fullStackTrace).append("\n\n");
					}
					//final failure
					Throwable last = verificationFailures.get(size-1);
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":\n");
					failureMessage.append(last.toString());
					failureMessage.append(Arrays.toString(last.getStackTrace()));
					//set merged throwable
					Throwable merged = new Throwable(failureMessage.toString());
					merged.setStackTrace(last.getStackTrace());
					result.setThrowable(merged);
				}
			}
		}
	}

	/**
	 *
	 * To identify the latest captured screenshot
	 * @return fileName of screenshot
	 */
	private String capturedScreenshot() {
		File mediaFolder=new File(ReportSetup.getImagesPath());
		File[] files = mediaFolder.listFiles();
		if(files!=null) {
			Arrays.sort(files, (o1, o2) -> {
				return -1 * (new Long(o1.lastModified()).compareTo(o2.lastModified())); //for descending order
			});
			return files[0].getName();
		} else {
			return null;
		}
	}
 
}
