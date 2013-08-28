package com.company.seleniumframework;

/**
 * An enumeration of Browsers that Selenium 2 uses.
 * @author ddavison
 *
 */
public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    INTERNET_EXPLORER("ie"),
    SAFARI("safari");
    
    String moniker;
    
    Browser(String moniker) {
        this.moniker = moniker;
    }
}
