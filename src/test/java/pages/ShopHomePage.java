package pages;

import com.siby.automation.actions.web.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ShopHomePage extends BasePage {

    By inputText = By.id("skycom-search-text");

    public ShopHomePage() {
        get("http://www.sky.com/shop");
    }

    public ShopHomePage searchFor(String text) {
        getElement(inputText).sendKeys(text + Keys.RETURN);
        return this;
    }

    public void validateResultText(String resultText) {
        assertThat(pageSourceContains(resultText), is(true));
    }
}