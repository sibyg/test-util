package com.siby.automation.core;

import com.siby.automation.browser.IBrowser;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class TestContext implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    static {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-webDriver.xml");
        context.registerShutdownHook();
    }

    public static WebDriver getDriver() {
        return getBrowser().getWebDriver();
    }

    public static Configuration getConfiguration() {
        return getBrowser().getConfiguration();
    }

    private static IBrowser getBrowser() {
        return (IBrowser) applicationContext.getBean("browser");
    }

    public void destroy() throws Exception {
        getBrowser().closeBrowser();
    }

    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.applicationContext = appContext;
    }
}
