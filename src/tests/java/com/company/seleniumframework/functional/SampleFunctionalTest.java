package com.company.seleniumframework.functional;

import org.junit.Test;
import org.openqa.selenium.By;

import com.company.seleniumframework.AutomationTest;
import com.company.seleniumframework.Browser;
import com.company.seleniumframework.Config;

/**
 * This is a sample test that can get you started.
 * <br><br>
 * This test shows how you can use the concept of "method chaining" to create successful, and independent tests, as well as the validations method that can get you started.
 * @author ddavison
 *
 */

@Config(
    url = "http://ddavison.github.io/tests/getting-started-with-selenium.htm", // base url that the test launches against
    browser = Browser.CHROME, // the browser to use.
    hub = "" // you can specify a hub hostname / ip here.
)
public class SampleFunctionalTest extends AutomationTest {
    
    /**
     * You are able to fire this test right up and see it in action.  Right click the test() method, and click "Run As... jUnit test".
     * 
     * The purpose of this is to show you how you can continue testing, just by taking the semi colon out, and continuing with your test.
     */
    @Test
    public void test() {

    	// click / validateAttribute
        click(props.get("click"))
        .validateAttribute(props.get("click"), "class", "success") // validate that the class indeed added.
        
        // setText / validateText
        .setText(By.id("setTextField"), "woot!")
        .validateText(By.id("setTextField"), "woot!") // validates that it indeed set.
        
        // check / uncheck
        .check(By.id("checkbox"))
        .validateChecked(By.id("checkbox")) // validate that it checked

        .check(props.get("radio.2")) // remember that props come from <class name>.properties, and are always CSS selectors. (why use anything else, honestly.)
        .validateUnchecked(props.get("radio.1")) // since radio 1 was selected by default, check the second one, then validate that the first radio is no longer checked.
        
        // select from dropdowns.
        .selectOptionByText(By.xpath("//select[@id='select']"), "Second") // just as a proof of concept that you can select on anything. But don't use xpath!!
        .validateText(By.id("select"), "2") // validateText() will actually return the value="" attr of a dropdown, so that's why 2 works but "Second" will not.
        
        .selectOptionByValue(By.cssSelector("select#select"), "3")
        .validateText(props.get("select"), "3")
        
        // frames
        .switchToFrame("frame") // the id="frame"
        .validatePresent(By.cssSelector("div#frame_content"))
        
        // windows
        .switchToWindow("Getting Started with Selenium") // switch back to the test window.
        .click(By.linkText("Open a new tab / window"))
        .waitForWindow("Google") // waits for the url.  Can also be the you are expecting. :) (regex enabled too)
        .setText(By.name("q"), "google!")
        .closeWindow(); // we've closed google, and back on the getting started with selenium page.
        
    }
}
