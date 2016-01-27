package com.siby.automation.mobile_testing;

import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ChromeAndroidAppiumExample {

    WebDriver driver;

    @Before
    public void beforeTest() throws MalformedURLException {
        //set capabilities required
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Nexus 6P API 23");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Browser");


        //instantiate driver
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait( 30, SECONDS);
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
