package pages;


import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.SeleniumUtils;

public class MainPage {

    private WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(this.driver, this);
    }

    @FindBy(xpath = "//a[@title='Administration']/span")
    private WebElement administrationLink;

    @FindBy(xpath = "//a[@title='Projects']/span")
    private WebElement projectPageLink;

    @FindBy(xpath = "//a[@title='Edit project settings']")
    private WebElement editProjectSettingsLink;

    @Step("Open Project Link")
    public MainPage openProjectLink() {
        SeleniumUtils.waitElementToBeClickable(driver, projectPageLink, 5);
        projectPageLink.click();
        return this;
    }

    @Step("Edit Project Settings")
    public void editProjectSettings() {
        editProjectSettingsLink.click();
    }

    @Step("Open Project")
    public MainPage openProject(String projectName) {
        driver.findElement(By.xpath("//a[text() = '" + projectName + "']")).click();
        return this;
    }

    @Step("Open Administration Link")
    public void openAdministrationLink() {
        SeleniumUtils.waitForElementPresent(driver, By.xpath("//a[@title='Administration']/span"), 5);
        SeleniumUtils.waitElementToBeClickable(driver, administrationLink, 5);
        administrationLink.click();
    }

}
