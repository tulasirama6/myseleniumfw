package one.tulasi.framework.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Created by TulasiRam A on 22 Dec 2016.
 */
public class RetryWrapper implements IRetryAnalyzer {
    Logger log = LogManager.getLogger(RetryWrapper.class);

    private int retryCount = 0;
    private int maxRetryCount = 1;

    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            log.info("=====================================================================================");
            log.info("Retry #" + retryCount + " for test: " + result.getMethod().getMethodName() + ", on thread: " + Thread.currentThread().getName());
            log.info("=====================================================================================");
            return true;
        }
        return false;
    }
}