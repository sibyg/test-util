package com.siby.automation.browser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siby.automation.core.Configuration;
import com.siby.automation.core.CurrentScenario;
import com.siby.automation.util.Helper;
import cucumber.api.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

public class BrowserStackBrowser implements IBrowser {

    private WebDriver driver;
    private String username;
    private String authkey;
    private boolean localTunnel;
    private String browserName;
    private String buildName;
    private String projectName;

    public BrowserStackBrowser(String username, String authkey, boolean localTunnel, String browserName, String buildName) {

        if (isBlank(username) || isBlank(authkey)) {
            throw new IllegalArgumentException("BS_USERNAME and BS_AUTHKEY must be set");
        }

        if (isBlank(browserName)) {
            throw new IllegalArgumentException("BROWSERSTACK_BROWSER must be set");
        }

        this.buildName = buildName;
        this.username = username;
        this.authkey = authkey;
        this.localTunnel = localTunnel;
        this.browserName = browserName;
    }

    @Override
    public WebDriver getWebDriver() {
        if (driver == null) {
            try {
                driver = initialiseWebDriver();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return driver;
    }

    private String getBrowserstackUrl() {
        return "http://" + this.username + ":" + this.authkey + "@hub.browserstack.com/wd/hub";
    }

    private WebDriver initialiseWebDriver() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserstack.debug", "true");
        caps.setCapability("browserstack.local", localTunnel);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Map<String, Object>> browsers = objectMapper.readValue(getClass().getResourceAsStream("/browsers.json"), Map.class);

        Map<String, Object> browserConfig = browsers.get(this.browserName);

        if (browserConfig == null) {
            throw new RuntimeException("no config found for " + this.browserName);
        }

        if (this.projectName != null) {
            caps.setCapability("project", this.projectName);
        }

        // for hudson and jenkins
        String jobName = Helper.getSetting("JOB_NAME");
        if (jobName != null) {
            buildName = jobName + " " + Helper.getSetting("BUILD_NUMBER");
        }

        if (this.buildName != null) {
            caps.setCapability("build", buildName);
        }

        Scenario currentScenario = CurrentScenario.getCurrentScenario();

        if (currentScenario != null) {
            caps.setCapability("name", currentScenario.getName());
        }

        for (String key : browserConfig.keySet()) {
            Object value = browserConfig.get(key);
            if (value != null) {
                caps.setCapability(key, value);
            }
        }

        try {
            return new RemoteWebDriver(new URL(getBrowserstackUrl()), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    public Configuration getConfiguration() {
        return null;
    }
}
