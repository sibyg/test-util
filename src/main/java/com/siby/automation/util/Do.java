package com.siby.automation.util;

import com.siby.automation.core.Configuration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Do {

    public static <T> T until(WebDriver driver, ExpectedCondition<T> expectedCondition, int timeout) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, timeout);
        return webDriverWait.until(expectedCondition);
    }

    public static <T> T until(WebDriver driver, ExpectedCondition<T> expectedCondition) {
        return until(driver, expectedCondition, Configuration.DEFAULT_TIMEOUT_VALUE);
    }
}
