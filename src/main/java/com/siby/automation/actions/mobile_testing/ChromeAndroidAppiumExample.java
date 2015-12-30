package com.siby.automation.actions.mobile_testing;

import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ChromeAndroidAppiumExample {

    WebDriver driver;

    @Before
    public void beforeTest() throws MalformedURLException {
        //set capabilities required
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setPlatform(Platform.ANDROID);
        capabilities.setCapability("device", "android");
        capabilities.setCapability("app", "chrome");


        //instantiate driver
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
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
