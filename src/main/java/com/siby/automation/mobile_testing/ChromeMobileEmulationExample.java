package com.siby.automation.mobile_testing;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

public class ChromeMobileEmulationExample {

    @Test
    public void simple() throws MalformedURLException {


        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", "Google Nexus 5");
//        mobileEmulation.put("deviceName", "Google Nexus 6");
        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        DesiredCapabilities capabilities = chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);


        // driver
        System.setProperty("webdriver.chrome.driver", "/apps/drivers/chromedriver");

        WebDriver driver = new ChromeDriver(capabilities);

        // And now use this to visit Google
        driver.get("http://www.google.com");


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        driver.quit();

    }
}
