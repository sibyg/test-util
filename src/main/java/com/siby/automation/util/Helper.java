package com.siby.automation.util;

import com.siby.automation.core.BrowserType;
import com.siby.automation.core.Configuration;
import com.siby.automation.core.TestContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

public class Helper {
    public static final String OS_NAME = System.getProperty("os.name");
    private static String librariesFolder = "/libraries/";

    public static void pause(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (Exception e) {
            //
        }
    }

    public static String getChromeDriverFileLocation() {
        if (OS_NAME.toLowerCase().contains("mac os x")) {
            return librariesFolder + "chromedriver_mac";
        } else if (OS_NAME.toLowerCase().contains("windows")) {
            return librariesFolder + "chromedriver_win.exe";
        } else {
            String arch = System.getProperty("os.arch");
            if (arch.contains("64")) {
                return librariesFolder + "chromedriver_linux_64";
            } else {
                return librariesFolder + "chromedriver_linux_32";
            }
        }
    }

    public static String getIEDriverFileLocation() {
        String windowsArchitecture = System.getProperty("os.arch");
        if (windowsArchitecture.contains("64")) {
            return librariesFolder + "IEDriverServer_x64_2.38.0.exe";
        } else {  //32 bit
            return librariesFolder + "IEDriverServer_Win32_2.38.0.exe";
        }
    }

    public static void selectByValue(WebElement element, String value) {
        if (TestContext.getConfiguration().getBrowser().equals(BrowserType.ANDROID)) {
            element.sendKeys(value);
        } else {
            Select existingProviderSelect = new Select(element);
            existingProviderSelect.selectByValue(value);
        }
    }

    public static void selectByIndex(WebElement element, int index) {
        Select existingProviderSelect = new Select(element);
        existingProviderSelect.selectByIndex(index);
    }

    public static String randomMagicNumber(String postfix) {
        Random random = new Random();
        return "020" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + postfix;
    }

    public static String getSetting(String key) {
        String result = System.getenv(key);
        if (result == null) {
            result = System.getProperty(key);
        }
        return result;
    }

    public static boolean isHudson() {
        return envIsSetInEnv() && Helper.getSetting("TEST_BROWSER") != null;
    }

    public static boolean envIsSetInEnv() {
        return Helper.getSetting("TEST_ENV") != null || Helper.getSetting("TEST_ENV_NAME") != null;
    }

    public static boolean isMobilePhoneDevice() {
        return TestContext.getConfiguration().getBrowser() == BrowserType.ANDROID || (TestContext.getConfiguration().getBrowser() == BrowserType.IOS && TestContext.getConfiguration().getDeviceType() == Configuration.DeviceType.MOBILE)
                || TestContext.getConfiguration().getBrowser() == BrowserType.FIREFOXMOBILE
                || TestContext.getConfiguration().getBrowser() == BrowserType.CHROMEMOBILE
                || TestContext.getConfiguration().getBrowser() == BrowserType.SAFARIMOBILE
                //treating tablet as mobile device to avoid maximizing screen
                || (TestContext.getConfiguration().getBrowser() == BrowserType.BROWSER_STACK && TestContext.getConfiguration().getDeviceType() == Configuration.DeviceType.TABLET)
                || (TestContext.getConfiguration().getBrowser() == BrowserType.BROWSER_STACK && TestContext.getConfiguration().getDeviceType() == Configuration.DeviceType.MOBILE);
    }

    public static void mobileCompatibleClick(WebElement elem) {
        if (TestContext.getConfiguration().getBrowser() != BrowserType.IOS) {
            elem.click();
        } else {
            elem.click();
            elem.click();
        }
    }

}
