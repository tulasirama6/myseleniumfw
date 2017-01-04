package one.tulasi.framework.enums;

/**
 * Created by tulasirama on 12/8/16.
 */
public enum BrowserType {
    CHROME ("Chrome"),
    FIREFOX ("FireFox"),
    OPERA ("Opera"),
    EDGE ("Edge"),
    HTMLUNIT ("HtmlUnit"),
    SAFARI ("Safari"),
    PHANTOMJS ("PhantomJS");

    String browserType;
    BrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getValue() {
        return browserType;
    }

    public static BrowserType getBrowserType(String browserType) {
        for(BrowserType b:values()) {
            if(b.getValue().equalsIgnoreCase(browserType)) {
                return b;
            }
        }
        return BrowserType.CHROME; //By default the browserType would be CHROME.
    }
}
