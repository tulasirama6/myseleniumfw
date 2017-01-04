package one.tulasi.framework.core;

import org.openqa.selenium.WebDriver;

/**
 * Created by TulasiRam A on 12/7/2016.
 */
public abstract class Browser {

    protected WebDriver driver;

    public WebDriver getDriver() {
        return this.driver;
    }

}
