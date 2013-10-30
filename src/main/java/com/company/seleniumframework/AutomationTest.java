package com.company.seleniumframework;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
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
    
    private int attempts = 0;
    
    public Actions actions;
    
    public Map<String, By> props = new HashMap<String, By>();
    
    /**
     * The url that an automated test will be testing.
     */
    private String baseUrl;
    private Config configuration;
    
    private Pattern p;
    private Matcher m;
    
    public AutomationTest() {
        configuration = getClass().getAnnotation(Config.class);
        
        baseUrl = configuration.url();
        
        switch (configuration.browser()) {
        case CHROME: driver = new ChromeDriver(); break;
        case FIREFOX: driver = new FirefoxDriver(); break;
        case INTERNET_EXPLORER: driver = new InternetExplorerDriver(); break;
        case SAFARI: driver = new SafariDriver(); break;
        case HTMLUNIT: // If you are designing a regression system, HtmlUnit is NOT recommended.  
            driver = new HtmlUnitDriver(); 
            ((HtmlUnitDriver)driver).setJavascriptEnabled(true);
        break;
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
//            x.printStackTrace();
            System.err.println("WARN: No css properties file for this test was found.  You can create one under /src/tests/resources/<package(s)>/" + getClass().getSimpleName().concat(".properties"));
        }
        
        actions = new Actions(driver);
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
        
        if (size > 1) System.err.println("WARN: There are more than 1 " + by.toString() + " 's!");
        
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
     * Clears the text from a text field, and sets it.
     * @param by The element to set the text of.
     * @param text The text that the element will have.
     * @return
     */
    public AutomationTest setText(By by, String text) {
        System.out.println(by.toString());
        WebElement element = waitForElement(by);
        element.clear();
        element.sendKeys(text);
        return this;
    }
    
    /**
     * Hover over an element.
     * @param by The element to hover over.
     * @return
     */
    public AutomationTest hoverOver(By by) {
        actions.moveToElement(driver.findElement(by)).perform();
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
    
    /* Window / Frame Switching */
    
    /**
     * Waits for a window to appear, then switches to it.
     * @param regex Regex enabled. Url of the window, or title.
     * @return
     */
    public AutomationTest waitForWindow(String regex) {
        Set<String> windows = driver.getWindowHandles();

        for (String window : windows) {
            try {
                driver.switchTo().window(window);

                p = Pattern.compile(regex);
                m = p.matcher(driver.getCurrentUrl());

                if (m.find()) {
                	attempts = 0;
                	return switchToWindow(regex);
                }
                else {
                    // try for title
                    m = p.matcher(driver.getTitle());
                    
                    if (m.find()) {
                        attempts = 0;
                        return switchToWindow(regex);
                    }
                }
            } catch(NoSuchWindowException e) {
                if (attempts <= MAX_ATTEMPTS) {
                    attempts++;
                    
                    try {Thread.sleep(1);}catch(Exception x) { x.printStackTrace(); }

                    return waitForWindow(regex);
                } else {
                    fail("Window with url|title: " + regex + " did not appear after " + MAX_ATTEMPTS + " tries. Exiting.");
                }
            }
        }

        // when we reach this point, that means no window exists with that title..
        if (attempts == MAX_ATTEMPTS) {
            fail("Window with title: " + regex + " did not appear after 5 tries. Exiting.");
            return this;
        } else {
            System.out.println("#waitForWindow() : Window doesn't exist yet. [" + regex + "] Trying again. " + attempts + "/" + MAX_ATTEMPTS);
            attempts++;
            return waitForWindow(regex);
        }
    }
    
    /**
     * Switch's to a window that is already in existance.
     * @param regex Regex enabled. Url of the window, or title.
     * @return
     */
    public AutomationTest switchToWindow(String regex) {
        Set<String> windows = driver.getWindowHandles();
        
        for (String window : windows) {
            driver.switchTo().window(window);
            System.out.println(String.format("#switchToWindow() : title=%s ; url=%s",
                    driver.getTitle(),
                    driver.getCurrentUrl()));
            
            p = Pattern.compile(regex);
            m = p.matcher(driver.getTitle());
            
            if (m.find()) return this;
            else {
                m = p.matcher(driver.getCurrentUrl());
                if (m.find()) return this;
            }
        }
        
        fail("Could not switch to window with title / url: " + regex);
        return this;
    }
    
    /**
     * Close an open window.
     * <br>
     * If you have opened only 1 external window, then when you call this method, the context will switch back to the window you were using before.<br>
     * <br>
     * If you had more than 2 windows displaying, then you will need to call {@link #switchToWindow(String)} to switch back context.
     * @param regex The title of the window to close (regex enabled). You may specify <code>null</code> to close the active window. If you specify <code>null</code> then the context will switch back to the initial window.
     * @return
     */
    public AutomationTest closeWindow(String regex) {
    	if (regex == null) {
    		driver.close();
    		
    		if (driver.getWindowHandles().size() == 1)
    			driver.switchTo().window(driver.getWindowHandles().iterator().next());
    		
    		return this;
    	}
    	
        Set<String> windows = driver.getWindowHandles();
        
        for (String window : windows) {
            try {
                driver.switchTo().window(window);
                
                p = Pattern.compile(regex);
                m = p.matcher(driver.getTitle());
                
                if (m.find()) {
                    switchToWindow(regex); // switch to the window, then close it.
                    driver.close();
                    
                    if (windows.size() == 2) // just default back to the first window.
                        driver.switchTo().window(windows.iterator().next());
                } else {
                    m = p.matcher(driver.getCurrentUrl());
                    if (m.find()) {
                        switchToWindow(regex);
                        driver.close();
                        
                        if (windows.size() == 2) driver.switchTo().window(windows.iterator().next());
                    }
                }
                    
            } catch(NoSuchWindowException e) {
                fail("Cannot close a window that doesn't exist. ["+regex+"]");
            }
        }
        return this;
    }
    
    /**
     * Closes the current active window.  Calling this method will return the context back to the initial window.
     * @return
     */
    public AutomationTest closeWindow() {
        return closeWindow(null);
    }
    
    /**
     * Switches to a frame or iframe.
     * @param idOrName The id or name of the frame.
     * @return
     */
    public AutomationTest switchToFrame(String idOrName) {
        try {
            driver.switchTo().frame(idOrName);
        } catch (Exception x) {
            fail("Couldn't switch to frame with id or name [" + idOrName + "]");
        }
        return this;
    }
    
    /* ************************ */
    
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
        String actual = getText(by);
        
        assertTrue(String.format("Text does not match! [expected: %s] [actual: %s]", text, actual), text.equals(actual));
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
    
    /**
     * Validates an attribute of an element.<br><br>
     * Example:<br>
     * <blockquote>
     * {@literal <input type="text" id="test" />}
     * <br><br>
     * <code>.validateAttribute(css("input#test"), "type", "text") // validates that the "type" attribute equals "test"</code>
     * </blockquote>
     * @param by The element
     * @param attr The attribute you'd like to validate
     * @param regex What the attribute <b>should</b> be.  (this method supports regex)
     * @return
     */
    public AutomationTest validateAttribute(By by, String attr, String regex) {
        String actual = null;
        try {
            actual = driver.findElement(by).getAttribute(attr);
            if (actual.equals(regex)) return this; // test passes.
        } catch (NoSuchElementException e) {
            fail("No such element [" + by.toString() + "] exists.");
        } catch (Exception x) {
            fail("Cannot validate an attribute if an element doesn't have it!");
        }
        
        p = Pattern.compile(regex);
        m = p.matcher(actual);
        
        assertTrue(String.format("Attribute doesn't match! [Selector: %s] [Attribute: %s] [Desired value: %s] [Actual value: %s]", 
                by.toString(),
                attr,
                regex,
                actual
                ), m.find());
        
        return this;
    }
    
    /**
     * Validate the Url
     * @param regex Regular expression to match
     * @return
     */
    public AutomationTest validateUrl(String regex) {
        p = Pattern.compile(regex);
        m = p.matcher(driver.getCurrentUrl());
        
        assertTrue("Url does not match regex [" + regex + "] (actual is: \""+driver.getCurrentUrl()+"\")", m.find());
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
    
    /**
     * Navigates to an absolute or relative Url.
     * @param url Use cases are:<br>
     * <blockquote>
     * <code>navigateTo("/login") // navigate to a relative url. slash meaning start fresh from the base url.</code><br><br>
     * <code>navigateTo("path") // navigate to a relative url. will simply append "path" to the current url.</code><br><br>
     * <code>navigateTo("http://google.com") // navigates to an absolute url.</code>
     * </blockquote>
     * @return
     */
    public AutomationTest navigateTo(String url) {
        // absolute url
        if (url.contains("://"))      driver.navigate().to(url);
        else if (url.startsWith("/")) driver.navigate().to(baseUrl.concat(url));
        else                          driver.navigate().to(driver.getCurrentUrl().concat(url));
        
        return this;
    }
}
