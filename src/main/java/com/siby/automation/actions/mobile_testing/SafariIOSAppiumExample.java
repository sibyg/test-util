package com.siby.automation.actions.mobile_testing;

import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SafariIOSAppiumExample {

    WebDriver driver;

    @Before
    public void beforeTest() throws MalformedURLException {
        //set capabilities required
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "iPhone 6");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "9.2");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Safari");

        //instantiate driver
        driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS);
    }

    @Test
    public void testSearchPage()  {
        driver.get("https://www.google.co.in");
    }

    @After
    public void afterTest() {
        driver.quit();
    }
}
