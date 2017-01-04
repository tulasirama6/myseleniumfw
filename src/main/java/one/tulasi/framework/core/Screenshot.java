package one.tulasi.framework.core;

import one.tulasi.framework.util.ReportSetup;
import one.tulasi.framework.util.UtilBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;

/**
 * Created by TulasiRam A on 08 Dec 2016.
 */
public class Screenshot {

    private static final Logger log = LogManager.getLogger(Screenshot.class);

    private String appURL;
    private String absolutePath;
    private String name;
    private String outputDirectoryPath;

    public Screenshot(WebDriver driver) {
        try {
            if(driver!=null) {
                log.info("Saving screenshot of current browser window");
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                setName("Screenshot" + UtilBase.getRandomNumber(Integer.MAX_VALUE) + ".png");
                File targetFile = new File(ReportSetup.getImagesPath(), getName());
                Files.copy(screenshotFile.toPath(), targetFile.toPath());
                log.info("Screenshot created and : " + getName());
            }
        } catch(Exception e) {
            log.error("An exception occurred while saving screenshot of current browser window.."+e.getCause());
        }
    }

    public Screenshot(WebDriver driver, String testName) {
        try {
            if(driver!=null) {
                log.info("Saving screenshot of current browser window");
                setName(testName + ".png"); //set Test Name as screenshot name
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File targetFile = new File(ReportSetup.getImagesPath(), getName());
                Files.copy(screenshotFile.toPath(), targetFile.toPath());
                log.info("Screenshot created and : " + getName());
            }
        } catch(Exception e) {
            log.error("An exception occurred while saving screenshot of current browser window.."+e.getCause());
        }
    }

    public String getOutputDirectoryPath() {
        return outputDirectoryPath;
    }

    public void setOutputDirectoryPath(final String outputDirectoryPath) {
        this.outputDirectoryPath = outputDirectoryPath;
    }

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(final String appURL) {
        this.appURL = appURL;
    }

    public void setAbsolutePath(final String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        if(absolutePath==null) {
            absolutePath = ReportSetup.getImagesPath() + UtilBase.getFileSeparator() + getName();
        }
        return absolutePath;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "| APPLICATION URL:" + this.appURL + " | SCREENSHOT NAME:" + this.name + " | PATH:" + this.absolutePath;
    }
}