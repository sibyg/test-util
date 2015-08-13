package com.siby.automation.core;


import com.siby.automation.browser.BrowserFactory;
import com.siby.automation.browser.BrowserStackBrowser;
import com.siby.automation.devices.AndroidDevice;
import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import static com.siby.automation.util.Helper.*;

public enum BrowserType {

    INTERNETEXPLORER {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            File iefile = new File(getIEDriverFileLocation());
            iefile.setExecutable(true);
            System.setProperty("webdriver.ie.driver", iefile.getAbsolutePath());
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            ieCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            ieCapabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);
            ieCapabilities.setCapability("ignoreZoomSetting", true);
            return new InternetExplorerDriver(ieCapabilities);
        }
    },

    FIREFOX {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            return null;
        }
    },

    SAFARI {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            return new SafariDriver();
        }
    },

    CHROME {

        Logger log = Logger.getLogger(BrowserType.class.getCanonicalName());

        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            URL resource = BrowserType.class.getResource(getChromeDriverFileLocation());
            File file = new File(resource.getPath());
            if (file.exists()) {
                log.info("CHROME DRIVER exists:" + file.getAbsolutePath());
                file.setExecutable(true);
            }

            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(file)
                    .usingAnyFreePort()
                    .build();
            BrowserFactory.setChromeDriverService(service);
            try {
                service.start();
            } catch (IOException e) {
                throw new RuntimeException("Chrome service failed to start");
            }
            return new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        }
    },

    FIREFOXMOBILE {

        String userAgentStringMobi = "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";

        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("general.useragent.override", userAgentStringMobi);
            FirefoxDriver dummyMobileDriver = new FirefoxDriver(profile);
            dummyMobileDriver.manage().window().setSize(new Dimension(500, 900));
            return dummyMobileDriver;
        }
    },

    ANDROID {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            //AndroidDevice.setUp();
            AndroidDevice.launchSelendroid();

            DesiredCapabilities caps = SelendroidCapabilities.android();
            WebDriver selendroidDriver;
            try {
                selendroidDriver = new SelendroidDriver(caps);
            } catch (Exception e) {
                throw new RuntimeException("selendroid driver didnt start");
            }
            return selendroidDriver;  //new AndroidDriver();
        }
    },

    IOS {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            return null;
        }
    },

    CHROMEMOBILE {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            File file2 = new File(getChromeDriverFileLocation());
            file2.setExecutable(true);
            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(file2)
                    .usingAnyFreePort()
                    .build();
            BrowserFactory.setChromeDriverService(service);
            try {
                service.start();
            } catch (IOException e) {
                throw new RuntimeException("Chrome service failed to start");
            }
            RemoteWebDriver remoteWebDriver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
            remoteWebDriver.manage().window().setSize(new Dimension(500, 900));
            return remoteWebDriver;
        }
    },

    SAFARIMOBILE {
        @Override
        public WebDriver createWebDriver(Configuration configuration) {
            SafariDriver safariDriver = new SafariDriver();
            safariDriver.manage().window().setSize(new Dimension(500, 900));
            return safariDriver;
        }
    },

    BROWSER_STACK {
        Logger log = Logger.getLogger(BrowserType.class.getCanonicalName());

        @Override
        public WebDriver createWebDriver(Configuration configuration) {

            BrowserStackBrowser stackBrowser = new BrowserStackBrowser(getSetting("BS_USERNAME"),
                    getSetting("BS_AUTHKEY"), configuration.getBrowserStackTunnel(),
                    configuration.getBrowserStackBrowser(), getSetting("BUILD_NAME"));

            stackBrowser.setProjectName("storefront-tests");
            try {
                return stackBrowser.getWebDriver();
            } catch (WebDriverException e) {
                log.warning("Failed to connect to browser stack. Is the tunneling application running?");
                throw e;
            }
        }
    };

    public abstract WebDriver createWebDriver(Configuration configuration);
}
