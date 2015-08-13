package com.siby.automation.browser;

import com.siby.automation.core.Configuration;
import org.openqa.selenium.WebDriver;

public interface IBrowser {
    WebDriver getWebDriver();

    void closeBrowser();

    Configuration getConfiguration();
}
