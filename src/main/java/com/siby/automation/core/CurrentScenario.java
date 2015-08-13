package com.siby.automation.core;

import cucumber.api.Scenario;

import java.util.HashMap;

public class CurrentScenario {
    private static Scenario currentScenario;
    private static HashMap scenarioSession = new HashMap();

    public static Scenario getCurrentScenario() {
        return currentScenario;
    }

    public static void setCurrentScenario(Scenario currentScenario) {
        CurrentScenario.currentScenario = currentScenario;
    }

    public static void setScenarioSession(String key, Object value) {
        scenarioSession.put(key, value);
    }

    public static Object getScenarioSession(String key) {
        return scenarioSession.get(key);
    }
}
