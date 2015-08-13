package com.siby.automation.core;

import com.siby.automation.util.Helper;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class Configuration {

    public static final int DEFAULT_TIMEOUT_VALUE = 25;
    private Logger log = Logger.getLogger(Configuration.class.getCanonicalName());
    private EndPoint backend;
    private DslEndPoint dslBackend;
    private BrowserType browser;
    private DeviceType deviceType;
    private String browserStackBrowser;
    private Boolean browserStackTunnel;
    private String baseUrl;
    private boolean recordTest;
    private String environment;

    public Configuration() {
        /*
         * Always look for config.yml in classpath;
         * if none found in depending project classpath, default from test-common will be used
         */
        loadConfig("/files/config.yml");
        overrideConfigFromEnvironmentVariables();

        log.info("Test environment " + this.toString());

    }

    public String getBrowserStackBrowser() {
        return browserStackBrowser;
    }

    public EndPoint getBackend() {
        return backend;
    }

    public DslEndPoint getDslBackend() {
        return dslBackend;
    }

    public BrowserType getBrowser() {
        return browser;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Boolean getBrowserStackTunnel() {
        return browserStackTunnel;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isRecordTest() {
        return recordTest;
    }


    public boolean isLive() {
        throw new NotImplementedException();
    }

    public boolean isIntegrationEnvironment() {
        throw new NotImplementedException();
    }

    public boolean isMobilePhoneDevice() {
        return browser == BrowserType.ANDROID
                || (browser == BrowserType.IOS && deviceType == DeviceType.MOBILE)
                || browser == BrowserType.FIREFOXMOBILE
                || browser == BrowserType.CHROMEMOBILE
                || browser == BrowserType.SAFARIMOBILE
                //treating tablet as mobile device to avoid maximizing screen
                || (browser == BrowserType.BROWSER_STACK && deviceType == DeviceType.TABLET)
                || (browser == BrowserType.BROWSER_STACK && deviceType == DeviceType.MOBILE);
    }

    private void loadConfig(String filePath) {
        InputStream input = Configuration.class.getResourceAsStream(filePath);
        Yaml yaml = new Yaml();
        Map map = (Map) yaml.load(input);
        Map<String, Object> config = (Map<String, Object>) map.get("config");

        if (config.containsKey("backend")) {
            backend = EndPoint.valueOf((String) config.get("backend"));
        }
        if (config.containsKey("dslBackend")) {
            dslBackend = DslEndPoint.valueOf((String) config.get("dslBackend"));
        }
        if (config.containsKey("environment")) {
            environment = (String) config.get("environment");
        }
        if (config.containsKey("browser")) {
            browser = BrowserType.valueOf((String) config.get("browser"));
        }
        if (config.containsKey("deviceType")) {
            deviceType = DeviceType.valueOf((String) config.get("deviceType"));
        }
        if (config.containsKey("browserStackBrowser")) {
            browserStackBrowser = (String) config.get("browserStackBrowser");
        }
        if (config.containsKey("browserStackTunnel")) {
            browserStackTunnel = (Boolean) config.get("browserStackTunnel");
        }
        if (config.containsKey("baseUrl")) {
            baseUrl = (String) config.get("baseUrl");
        }
    }

    private void overrideConfigFromEnvironmentVariables() {

        String backendValue = Helper.getSetting("TEST_BACKEND");
        if (backendValue != null) {
            backend = EndPoint.valueOf(backendValue.toUpperCase());
        }

        String browserValue = Helper.getSetting("TEST_BROWSER");
        if (browserValue != null) {
            browser = BrowserType.valueOf(browserValue.toUpperCase());
        }

        String deviceTypeValue = Helper.getSetting("DEVICE_TYPE");
        if (deviceTypeValue != null) {
            deviceType = DeviceType.valueOf(deviceTypeValue);
        }

        String browserstack_browser = Helper.getSetting("BROWSERSTACK_BROWSER");
        if (browserstack_browser != null) {
            browserStackBrowser = browserstack_browser;
        }

        String browserstackTunnelValue = Helper.getSetting("BROWSERSTACK_TUNNEL");
        if (browserstackTunnelValue != null) {
            browserStackTunnel = Boolean.valueOf(browserstackTunnelValue);
        }

        String baseUrlValue = Helper.getSetting("TEST_BASEURL");
        if (baseUrlValue != null) {
            baseUrl = baseUrlValue;
        }
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "backend=" + backend +
                ", dslBackend=" + dslBackend +
                ", environment=" + environment +
                ", browser=" + browser +
                ", deviceType=" + deviceType +
                ", browserStackBrowser='" + browserStackBrowser + '\'' +
                ", browserStackTunnel=" + browserStackTunnel +
                ", baseUrl='" + baseUrl + '\'' +
                ", recordTest=" + recordTest +
                '}';
    }

    public enum EndPoint {
        E04,
        E05,
        TDM,
        F02,
        COMMERCE_WEBAPP,
        PROD
    }

    public enum DslEndPoint {
        DEV8080("LOCALHOST_8080"),
        DEV9090("LOCALHOST_9090"),
        BCE04("BC-E04"),
        BCE05("BC-E05"),
        BCTDM("BC-TDM"),
        E04("E04"),
        E05("E05"),
        TDM("TDM");

        private String dslBackend;

        private DslEndPoint(String dslBackend) {
            this.dslBackend = dslBackend;
        }

        public String getDslBackend() {
            return dslBackend;
        }
    }

    public enum DeviceType {
        TABLET,
        MOBILE,
        DESKTOP
    }

}
