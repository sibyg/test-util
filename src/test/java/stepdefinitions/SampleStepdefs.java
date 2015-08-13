package stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import pages.ShopHomePage;

public class SampleStepdefs {

    private ShopHomePage shopHomepage;

    @Given("^I visit Sky Shop homepage$")
    public void I_visit_sky_shop_homepage() throws Throwable {
        shopHomepage = new ShopHomePage();
    }

    @And("^I search for (.*)$")
    public void searchFor(String text) throws Throwable {
        shopHomepage.searchFor(text);
    }

    @Then("^I should see (.*) related pages$")
    public void validateResultPages(String relatedPage) throws Throwable {
        shopHomepage.validateResultText(relatedPage);
    }
}
