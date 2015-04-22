package com.company.seleniumframework.functional_area;

import io.ddavison.conductor.Locomotive;
import io.ddavison.conductor.Browser;
import io.ddavison.conductor.Config;
import org.junit.Test;
import org.openqa.selenium.By;

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
// RIGHT CLICK SampleFunctionTest and run the test suite, or run each method individually.
public class SampleFunctionalTest extends Locomotive {

    @Test
    public void yourFirstTest() {
        setText("#setTextField", "some text")
        .click("div#click")
        .log("done")
        ;
    }

    @Test
    public void basicActions() {
        click(By.cssSelector("div#click"))
        .validateAttribute(By.cssSelector("div#click"), "class", "success")

        .setText(By.id("setTextField"), "woot!")
        .validateText(By.id("setTextField"), "woot!") // validates that it indeed set.

        .check(By.id("checkbox"))
        .validateChecked(By.id("checkbox")) // validate that it checked

        .check(By.cssSelector("input[type='radio']#radio2"))
        .validateUnchecked(By.cssSelector("input[type='radio']#radio1"))

        .selectOptionByText(By.xpath("//select[@id='select']"), "Second") // just as a proof of concept that you can select on anything. But don't use xpath!!
        .validateText(By.id("select"), "2") // validateText() will actually return the value="" attr of a dropdown, so that's why 2 works but "Second" will not.

        .selectOptionByValue(By.cssSelector("select#select"), "3")
        .validateText(By.id("select"), "3")

        .validateTextPresent("selectOptionByText()|selectOptionByValue()")
        .validateTextNotPresent("some random text")
        ;
    }

    @Test
    public void switchingFramesAndWindows() {
        switchToFrame("frame")
        .validatePresent(By.cssSelector("div#frame_content"))

        .switchToDefaultContent() // switch back to default context

        .switchToWindow("Getting Started with Selenium")
        .click(By.linkText("Open a new tab / window"))
        .waitForWindow("Google")
        .setText(By.name("q"), "google search!")
        .closeWindow()
        ;
    }

    @Test
    public void manipulatingData() {
        setText(By.id("setTextField"), "something")
        .store("myVar", getText(By.id("setTextField")))
        .validateText(By.id("setTextField"), get("myVar"))
        ;
    }
}
