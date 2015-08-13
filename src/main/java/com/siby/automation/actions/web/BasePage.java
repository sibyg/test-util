package com.siby.automation.actions.web;

import com.google.common.base.Function;
import com.siby.automation.actions.exception.CannotExecuteActionException;
import com.siby.automation.core.Configuration;
import com.siby.automation.core.TestContext;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

public class BasePage {

    private static final WebDriver driver = TestContext.getDriver();
    private static final Actions actions = new Actions(driver);
    private static final JavascriptExecutor JAVASCRIPT_EXECUTOR = (JavascriptExecutor) driver;
    public static long DEFAULT_TIME_OUT = 30L;
    public static long DEFAULT_POLL_TIME = 1L;
    private static Logger log = Logger.getLogger(BasePage.class.getCanonicalName());
    private static int connectionTimeout = 2 * 1000;


    public void navigateTo(String url) {
        get(url);
    }

    public void click(By by, final long timeout) {
        FluentWait<WebDriver> fluentWait = new WebDriverWait(driver, timeout);
        fluentWait.until(ExpectedConditions.elementToBeClickable(by));
        click(by);
    }

    public void click(By by) {
        fluentWait(by).click();
    }

    public void clear(By by) {
        WebElement element = fluentWait(by);
        try {
            element.clear();
        } catch (Exception e) {
            throw new CannotExecuteActionException("Element cannot be clear ", e);
        }
    }

    public List<WebElement> findElementsBy(By by) {
        return driver.findElements(by);
    }

    public void select(By by, String value) {
        try {
            WebElement element = fluentWait(by);
            Select select = new Select(element);
            select.selectByValue(value);
        } catch (Exception e) {
            throw new CannotExecuteActionException("Element cannot be selected ", e);
        }
    }

    public void verifyElementVisible(By... elements) {
        for (By by : elements) {
            FluentWait<WebDriver> fluentWait = new WebDriverWait(driver, DEFAULT_TIME_OUT).pollingEvery(DEFAULT_POLL_TIME, SECONDS);
            WebElement element = fluentWait.until(
                    ExpectedConditions.visibilityOfElementLocated(by));

            if ((element == null)) {
                throw new CannotExecuteActionException("By is not visible " + by);
            }
        }
    }

    public void verifyElementNotVisible(By... byItems) {
        for (By by : byItems) {
            Boolean elementNotVisible = new WebDriverWait(driver, DEFAULT_TIME_OUT).pollingEvery(DEFAULT_POLL_TIME, SECONDS)
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));

            if (!elementNotVisible) {
                throw new CannotExecuteActionException("By is visible " + by);
            }
        }
    }

    public void verifyElementsNotPresent(By... byItems) {
        for (By by : byItems) {
            List<WebElement> elements = driver.findElements(by);
            if (!elements.isEmpty()) {
                throw new CannotExecuteActionException("By is present " + by);
            }
        }
    }

    public void verifyElementsPresent(By... byItems) {
        for (By by : byItems) {
            List<WebElement> elements = driver.findElements(by);
            if (elements.isEmpty()) {
                throw new CannotExecuteActionException("By is not present " + by);
            }
        }
    }

    public boolean isElementNotPresent(By by) {
        return driver.findElements(by).isEmpty();
    }

    public boolean isElementPresent(By by) {
        try {
            final WebElement element = fluentWait(by);
            return element != null && element.isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementPresentWithOutWait(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    public boolean isAttributePresent(By elementCss, String attributeName) {
        final WebElement element = getElement(elementCss);
        return element != null && element.isDisplayed() && isNotBlank(element.getAttribute(attributeName));
    }

    public boolean isTextPresent(By by, Long timeout) {
        WebElement element = getElement(by, timeout);
        return element != null && element.isDisplayed() && isNotBlank(element.getText());
    }

    public boolean isTextPresent(By by) {

        final WebElement element = getElement(by);
        return element != null && element.isDisplayed() && isNotBlank(element.getText());
    }

    public boolean isROI() {
        return driver.getCurrentUrl().contains("ireland");
    }

    public Object waitForCondition(final By by, Condition condition) {
        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
        switch (condition) {
            case ELEMENT_TO_BE_CLICKABLE:
                return wait.until(ExpectedConditions.elementToBeClickable(by));
            case PRESENCE_OF_ELEMENT_LOCATED:
                return wait.until(ExpectedConditions.presenceOfElementLocated(by));
            case VISIBILITY_OF_ELEMENT_LOCATED:
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            case INVISIBILITY_OF_ELEMENT_LOCATED:
                return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            case TEXT_TO_BE_PRESENT_IN_ELEMENT:
                return wait.until(textToBePresentInElementLocated(by, "text"));
            case TITLE_CONTAINS:
                return wait.until(ExpectedConditions.titleContains("value"));
            case ALT_ATTRIBUTE_PRESENT:
                return wait.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver webDriver) {
                        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

                        return element.getAttribute("alt") != null;
                    }
                });
            default:
                throw new NotImplementedException();

        }
    }

    public void fillWithValue(By by, String value) {
        clear(by);
        WebElement element = fluentWait(by);

        if (value != null) {
            element.sendKeys(value);
        }
    }

    public WebElement getElement(final By by, Long timeout) {
        FluentWait<WebDriver> webDriverWait = new WebDriverWait(driver, timeout);
        try {
            return webDriverWait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    return webDriver.findElement(by);
                }
            });
        } catch (TimeoutException e) {
            return null;
        }
    }

    public WebElement getElement(final By by) {
        try {
            return fluentWait(by);
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

    public List<WebElement> getElements(By by) {
        try {
            return driver.findElements(by);
        } catch (NoSuchElementException exception) {
            return newArrayList();
        }
    }

    public String getElementText(By by) {
        return fluentWait(by).getText();
    }

    public boolean waitUntilTextPresent(By by, String text) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
        return wait.until(textToBePresentInElementLocated(by, text));
    }

    public boolean fluentWaitUntilTextPresent(By by, String text) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(DEFAULT_TIME_OUT, SECONDS)
                .pollingEvery(5, SECONDS)
                .ignoring(NoSuchElementException.class);
        return wait.until(textToBePresentInElementLocated(by, text));
    }

    public String getAttributeText(By by, String attribute) {
        return fluentWait(by).getAttribute(attribute);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public Object executeJScript(Object inputObjects, String script) throws CannotExecuteActionException {
        return JAVASCRIPT_EXECUTOR.executeScript(script, inputObjects);
    }

    public Object executeJScript(String script) throws CannotExecuteActionException {
        return JAVASCRIPT_EXECUTOR.executeScript(script);
    }

    public void waitForJQueryCompletionAction(final String script) throws CannotExecuteActionException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
            wait.until(new ExpectedCondition<Object>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return Boolean.parseBoolean(executeJScript(script, null).toString());
                }
            });
        } catch (TimeoutException tx) {
            throw new CannotExecuteActionException("Timed out after "
                    + DEFAULT_POLL_TIME
                    + " seconds while waiting for Ajax to complete.", tx);
        } catch (Exception e) {
            // jQuery libraries are not present, most likely. Should probably have some logging mechanism here
            throw new CannotExecuteActionException("Either driver not instantiated or JQuery library not present", e);
        }
    }

    public WebElement getSection(Integer rowNum) {
        return fluentWait(By.id("section-" + rowNum));
    }

    public WebElement getSection(String xpath) {
        return fluentWait(By.xpath(xpath));
    }

    public boolean pageSourceContains(String text) {
        return driver.getPageSource().contains(text);
    }

    public void moveToAndClick(By by) {
        WebElement element = getElement(by);
        actions.moveToElement(element);
        actions.click();
    }

    public void moveTo(By by) {
        Actions action = new Actions(driver);
        WebElement we = getElement(by);
        action.moveToElement(we).build().perform();
    }

    public void scrollTo(By element) {
        WebElement webElement = getElement(element);
        int y = webElement.getLocation().getY();
        String script = String.format("scroll(0, %d)", y);
        executeJScript(script);
    }

    public void get(String urlStr) {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(urlStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }

        if (TestContext.getConfiguration().getDeviceType() != Configuration.DeviceType.DESKTOP) {
            uriBuilder.addParameter("mobile", "true");
        }

        driver.get(uriBuilder.toString());
    }

    public void sleepInSecs(int secs) {
        try {
            Thread.sleep(1000 * secs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
        verifyElementVisible(By.id("signin"));
        sleepInSecs(1);
        getElement(By.name("signin")).click();
        verifyElementVisible(By.id("username"), By.id("password"), By.id("signinButton"));
        getElement(By.id("username")).sendKeys(username);
        getElement(By.id("password")).sendKeys(password);
        getElement(By.id("signinButton")).click();
    }

    public int getResponseCode(String url) throws IOException {
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setConnectTimeout(connectionTimeout);
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (con != null) {
            con.disconnect();
        }
        return responseCode;
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public void refreshAfterSecs(int seconds) {
        sleepInSecs(seconds);
        driver.navigate().refresh();
    }

    public WebElement fluentWait(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, SECONDS)
                .pollingEvery(5, SECONDS)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);

        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
    }

    public enum Condition {
        ELEMENT_TO_BE_CLICKABLE,
        PRESENCE_OF_ELEMENT_LOCATED,
        TEXT_TO_BE_PRESENT_IN_ELEMENT,
        TITLE_CONTAINS,
        VISIBILITY_OF_ELEMENT_LOCATED,
        INVISIBILITY_OF_ELEMENT_LOCATED,
        ALT_ATTRIBUTE_PRESENT
    }
}
