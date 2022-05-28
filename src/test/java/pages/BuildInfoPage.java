package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.SeleniumUtils;

public class BuildInfoPage {
    private WebDriver driver;

    public BuildInfoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[text()='Result:']/following-sibling::td[@class='st']")
    private WebElement statusIconElement;

    @FindBy(id = "buildLog_Tab")
    private WebElement buildLogTab;

    @FindBy(id = "buildParameters_Tab")
    private WebElement buildParametersTab;

    public void openBuildLogTab() {
        buildLogTab.click();
    }

    public void openParametersTab() {
        buildParametersTab.click();
    }

    public boolean hasExpectedResult(String expectedResult) {
        SeleniumUtils.waitElementToBeClickable(driver, statusIconElement, 20);
        return statusIconElement.getText().toLowerCase().contains(expectedResult.toLowerCase());
    }

    public boolean hasParameterWithValue(String parameter, String value) {
        return driver.findElement(By.xpath("//a[@name='ActualParametersOnAgent']/parent::h2/following-sibling::div//td[text()='" + parameter + "']/following-sibling::td/span")).getText().contains(value);
    }

    public boolean hasStringInLog(String str) {
        try {
            SeleniumUtils.waitForElementPresent(driver, By.xpath("//*[contains(text(),'" + str + "')]"), 5);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }


}
