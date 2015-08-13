package stepdefinitions;

import com.siby.automation.core.CurrentScenario;
import com.siby.automation.core.TestContext;
import com.siby.automation.util.Screenshot;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        CurrentScenario.setCurrentScenario(scenario);
    }

    @After
    public void tearDown(Scenario scenario) throws InterruptedException {
        Screenshot.embedScreenshot(scenario, TestContext.getDriver());
    }
}
