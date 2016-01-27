package com.siby.automation.web_testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Example {

    public static void main(String args[]) {
        // Create a new instance of the html unit driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = new ChromeDriver();

        driver.get("https://www.google.co.in");

        driver.quit();
    }
}
