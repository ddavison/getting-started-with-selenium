package com.company.seleniumframework.functional;

import org.junit.Test;

import com.company.seleniumframework.AutomationTest;
import com.company.seleniumframework.Browser;
import com.company.seleniumframework.Config;

/**
 * This is a sample test that can get you started.
 * <br><br>
 * This test shows how you can use the concept of "method chaining" to create successful, and independent tests. 
 * @author ddavison
 *
 */

@Config(url = "http://google.com", browser = Browser.CHROME) // You are able to specify a "base url" for your test, from which you will test. You may leave `browser` blank.
public class SampleFunctionalTest extends AutomationTest {
    @Test 
    public void testGoogle() {
        setText(props.get("google.txtSearch"), "test")
        .click(props.get("google.btnSearch"));
    }
}
