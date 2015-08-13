package com.siby.automation.browser;

import com.siby.automation.core.Configuration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BrowserFactory {

    private static Logger log = Logger.getLogger(BrowserFactory.class.getCanonicalName());
    private static ChromeDriverService chromeDriverService;

    public static WebDriver initializeDriver(Configuration configuration) {
        log.info("Initialising browser " + configuration.getBrowser().name());
        WebDriver webDriver = configuration.getBrowser().createWebDriver(configuration);

        if (webDriver == null) {
            FirefoxProfile ffProfile = new FirefoxProfile();
            ffProfile.setEnableNativeEvents(false);
            DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
            desiredCapabilities.setCapability(FirefoxDriver.PROFILE, ffProfile);
            webDriver = new FirefoxDriver(desiredCapabilities);
        }

        if (!(webDriver instanceof RemoteWebDriver)) {
            webDriver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
            webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        }

        if (!configuration.isMobilePhoneDevice()) {
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    public static ChromeDriverService getChromeDriverService() {
        return chromeDriverService;
    }

    public static void setChromeDriverService(ChromeDriverService chromeDriverService) {
        BrowserFactory.chromeDriverService = chromeDriverService;
    }
}