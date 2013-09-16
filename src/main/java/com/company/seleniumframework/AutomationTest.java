package com.company.seleniumframework;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.openqa.selenium.support.ui.Select;

/**
 * the base test that includes all Selenium 2 functionality that you will need
 * to get you rolling.
 * @author ddavison
 *
 */
public class AutomationTest {

    public WebDriver driver;
    
    // max seconds before failing a script.
    private final int MAX_ATTEMPTS = 5;
    
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
        		// css is arbitrary here.. USE IT! It rocks!
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
     * Private method that acts as an arbiter of implicit timeouts of sorts.. sort of like a Wait For Ajax method.
     */
    private WebElement waitForElement(By by) {
    	int attempts = 0;
    	int size = driver.findElements(by).size();
    	
    	while (size == 0) {
    		size = driver.findElements(by).size();
    		if (attempts == MAX_ATTEMPTS) fail(String.format("Could not find %s after %d seconds",
    		                                                 by.toString(),
    		                                                 MAX_ATTEMPTS));
    		attempts++;
    		try {
    			Thread.sleep(1000); // sleep for 1 second.
    		} catch (Exception x) {
    			fail("Failed due to an exception during Thread.sleep!");
    			x.printStackTrace();
    		}
    	}
    	
    	if (size > 0) System.err.println("WARN: There are more than 1 " + by.toString() + " 's!");
    	
    	return driver.findElement(by);
    }
    
    /**
     * Click an element.
     * @param by The element to click.
     * @return
     */
    public AutomationTest click(By by) {
    	waitForElement(by).click();
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
        waitForElement(by).sendKeys(text);
        return this;
    }
    
    /**
     * Checks if the element is checked or not.
     * @param by
     * @return <i>this method is not meant to be used fluently.</i><br><br>
     * Returns <code>true</code> if the element is checked. and <code>false</code> if it's not.
     */
    public boolean isChecked(By by) {
        return waitForElement(by).isSelected();
    }
    
    /**
     * Checks if the element is present or not.<br>
     * @param by
     * @return <i>this method is not meant to be used fluently.</i><br><br.
     * Returns <code>true</code> if the element is present. and <code>false</code> if it's not.
     */
    public boolean isPresent(By by) {
    	if (driver.findElements(by).size() > 0) return true;
    	return false;
    }
    
    /**
     * Get the text of an element.
     * <blockquote>This is a consolidated method that works on input's, as select boxes, and fetches the value rather than the innerHTMl.</blockquote>
     * @param by
     * @return
     */
    public String getText(By by) {
    	String text = null;
    	WebElement e = waitForElement(by);
    	
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
	    	waitForElement(by).click();
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
    		waitForElement(by).click();
    		assertFalse(by.toString() + " did not uncheck!", isChecked(by));
    	}
    	return this;
    }
    
    /**
     * Selects an option from a dropdown ({@literal <select> tag}) based on the text displayed.
     * @param by
     * @param text The text that is displaying.
     * @see #selectOptionByValue(By, String)
     * @return
     */
    public AutomationTest selectOptionByText(By by, String text) {
    	Select box = new Select(waitForElement(by));
    	box.selectByVisibleText(text);
    	return this;
    }
    
    /**
     * Selects an option from a dropdown ({@literal <select> tag}) based on the value.
     * @param by
     * @param value The <code>value</code> attribute of the option.
     * @see #selectOptionByText(By, String)
     * @return
     */
    public AutomationTest selectOptionByValue(By by, String value) {
    	Select box = new Select(waitForElement(by));
    	box.selectByValue(value);
    	return this;
    }
    
    /* Validation Functions for Testing */
    
    /**
     * Validates that an element is present.
     * @param by
     * @return
     */
    public AutomationTest validatePresent(By by) {
    	waitForElement(by);
    	assertTrue("Element " + by.toString() + " does not exist!",
    			isPresent(by));
    	return this;
    }
    
    /**
     * Validates that an element is not present.
     * @param by
     * @return
     */
    public AutomationTest validateNotPresent(By by) {
    	assertFalse("Element " + by.toString() + " exists!", isPresent(by));
    	return this;
    }
    
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
    
    /**
     * Validate that a checkbox or a radio button is checked.
     * @param by
     * @return
     */
    public AutomationTest validateChecked(By by) {
    	assertTrue(by.toString() + " is not checked!", isChecked(by));
    	return this;
    }
    
    /**
     * Validate that a checkbox or a radio button is unchecked.
     * @param by
     * @return
     */
    public AutomationTest validateUnchecked(By by) {
    	assertFalse(by.toString() + " is not unchecked!", isChecked(by));
    	return this;
    }
    
    /* ================================ */
    
    /**
     * Navigates the browser back one page.
     * Same as <code>driver.navigate().back()</navigate>
     * @return
     */
    public AutomationTest goBack() {
    	driver.navigate().back();
    	return this;
    }
    
}
