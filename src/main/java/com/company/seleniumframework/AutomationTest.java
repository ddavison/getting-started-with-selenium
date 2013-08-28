package com.company.seleniumframework;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * the base test that includes all Selenium 2 functionality that you will need
 * to get you rolling.
 * @author ddavison
 *
 */
public class AutomationTest {

    WebDriver driver;
    
    public Map<String, By> props = new HashMap<String, By>();
    
    /**
     * The url that an automated test will be testing.
     */
    private String baseUrl;
    private Config configuration;
    
    public AutomationTest() {
        configuration = getClass().getAnnotation(Config.class);
        
        baseUrl = configuration.url();
        
        switch (configuration.browser()) {
        case CHROME: driver = new ChromeDriver(); break;
        case FIREFOX: driver = new FirefoxDriver(); break;
        case INTERNET_EXPLORER: driver = new InternetExplorerDriver(); break;
        case SAFARI: driver = new SafariDriver(); break;
        }
        
        // load the properties.
        Properties properties = new Properties();
        try {
        	properties.load(getClass().getResourceAsStream(getClass().getSimpleName().concat(".properties")));
        	
        	for (String key : properties.stringPropertyNames())
        		props.put(key, By.cssSelector(properties.getProperty(key)));
        } 
        catch (Exception x) {
        	x.printStackTrace();
        }
        
        driver.navigate().to(baseUrl);
    }
    
    @After
    public void teardown() {
    	driver.quit();
    }
    
    /**
     * Click an element.
     * @param by The element to click.
     * @return
     */
    public AutomationTest click(By by) {
        driver.findElement(by).click();
        return this;
    }
    
    /**
     * Sets the text of a particular element, with some text.
     * @param by The element to set the text of.
     * @param text The text that the element will have.
     * @return
     */
    public AutomationTest setText(By by, String text) {
    	System.out.println(by.toString());
        driver.findElement(by).sendKeys(text);
        return this;
    }
    
    /**
     * Checks if the element is checked or not.
     * @param by
     * @return
     */
    public boolean isChecked(By by) {
        return driver.findElement(by).isSelected();
    }
    
    /**
     * Get the text of an element.
     * @param by
     * @return
     */
    public String getText(By by) {
    	String text = null;
    	WebElement e = driver.findElement(by);
    	
    	if (e.getTagName().equalsIgnoreCase("input") || e.getTagName().equalsIgnoreCase("select"))
    		text = e.getAttribute("value");
    	else
    		text = e.getText();
    	
    	return text;
    }
    
    /**
     * Check a checkbox, or radio button.
     * @param by The element to check.
     * @return
     */
    public AutomationTest check(By by) {
	    if (!isChecked(by)) {
	    	driver.findElement(by).click();
	    	assertTrue(by.toString() + " did not check!", isChecked(by));
	    }
	    return this;
    }
    
    /**
     * Uncheck a checkbox, or radio button.
     * @param by The element to uncheck.
     * @return
     */
    public AutomationTest uncheck(By by) {
    	if (isChecked(by)) {
    		driver.findElement(by).click();
    		assertFalse(by.toString() + " did not uncheck!", isChecked(by));
    	}
    	return this;
    }
    
    /* Validation Functions for Testing */
    
    /**
     * Validate that the text of an element is correct.
     * @param by The element to validate the text of.
     * @param text The text to validate.
     * @return
     */
    public AutomationTest validateText(By by, String text) {
    	assertTrue("Text does not match!", getText(by).equals(text));
    	return this;
    }
    
}
