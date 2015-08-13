package com.siby.automation.browser;


import com.siby.automation.core.BrowserType;
import com.siby.automation.core.Configuration;
import com.siby.automation.util.Recorder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.springframework.stereotype.Component;

@Component
public class Browser implements IBrowser {

    private static Recorder recorder;
    private WebDriver driver;
    private Configuration configuration;

    public Browser(Configuration config) {
        this.configuration = config;
        driver = BrowserFactory.initializeDriver(configuration);

        if (configuration.isRecordTest()) {
            recorder = new Recorder();
            recorder.start();
        }
    }

    @Override
    public WebDriver getWebDriver() {
        return driver;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void closeBrowser() {
        if (configuration.getBrowser() == BrowserType.CHROME) {
            driver.quit();
            BrowserFactory.getChromeDriverService().stop();
            TemporaryFilesystem tempFS = TemporaryFilesystem.getDefaultTmpFS();
            tempFS.deleteTemporaryFiles();
        } else if (configuration.getBrowser() == BrowserType.INTERNETEXPLORER) {
            deleteDomainAndSessionCookies();
            driver.quit();
        } else {
            driver.quit();
        }
        driver = null;
        if (recorder != null) {
            recorder.stop();
        }
    }

    private void deleteDomainAndSessionCookies() {
        driver.manage().deleteCookieNamed("evoroute");
        driver.manage().deleteCookieNamed("BackEnd");
        driver.manage().deleteCookieNamed("custype");
        driver.manage().deleteCookieNamed("BasketId");
    }

}
