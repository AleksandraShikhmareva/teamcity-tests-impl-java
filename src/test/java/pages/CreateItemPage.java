package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.SeleniumUtils;

public class CreateItemPage {

    WebDriver driver;

    public CreateItemPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "url")
    private WebElement urlInput;

    @FindBy(xpath = "//table[@class='parametersTable']")
    private WebElement parametersTable;

    @FindBy(xpath = "//input[@value = 'Proceed']")
    private WebElement proceedBtn;

    @FindBy(xpath = "//a[@href='#createFromUrl']")
    private WebElement createFromUrl;

    @FindBy(xpath = "//a[@href='#createManually']")
    private WebElement createManually;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "externalId")
    private WebElement projectIdInput;

    @FindBy(id = "createProject")
    private WebElement createBtn;

    @FindBy(xpath = "//a[@name='projectCreated']/following-sibling::div[@class='successMessage']")
    private WebElement successMessage;

    @FindBy(xpath = "//div[@class='saveButtonsBlock']/input[@value='Save']")
    private WebElement saveBtn;

    @Step("Create project from GitHub public repository with URL = {projectUrl}")
    public void addProjectFromRemoteRepository(String projectUrl) {
        createFromUrl.click();
        SeleniumUtils.waitElementToBeClickable(driver, urlInput, 5);
        urlInput.sendKeys(projectUrl);
        proceedBtn.click();
    }

    @Step("Create project {projectName} manually")
    public void createProjectManually(String projectName) {
        createManually.click();
        SeleniumUtils.waitElementToBeClickable(driver, nameInput, 5);
        nameInput.sendKeys(projectName);
        createBtn.click();
        SeleniumUtils.waitVisibilityOfElement(driver, successMessage, 5);
        saveBtn.click();
    }
}
