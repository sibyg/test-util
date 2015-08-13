package com.siby.automation.util;

import com.siby.automation.core.CurrentScenario;
import com.siby.automation.core.TestContext;
import cucumber.api.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class Screenshot {

    private static final Logger log = Logger.getLogger(Screenshot.class.getCanonicalName());

    public static void embedScreenshot(Scenario scenario, WebDriver driver) {
        if (scenario.isFailed()) {
            log.info(scenario.getName() + " Failed, taking screenshot");
            sleepInSecs(1);
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");
            } catch (Exception e) {
                e.printStackTrace();
                log.throwing(Screenshot.class.getName(), "embedScreenshot", e);
            }
        }
    }

    private static void sleepInSecs(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    public static void captureScreenshot(String screenName) throws IOException {
        String rootFolder = System.getProperty("user.dir");
        String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String screenShotFileName = rootFolder + "/evidence/" + CurrentScenario.getCurrentScenario().getName() + "/" + formattedDate + "-" + screenName + ".png";

        File screenshot = ((TakesScreenshot) TestContext.getDriver()).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File(screenShotFileName));
    }

}
