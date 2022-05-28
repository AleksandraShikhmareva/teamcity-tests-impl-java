package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.SeleniumUtils;

public class CreateFromURLPage {
    private WebDriver driver;

    public CreateFromURLPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@class='connectionSuccessful']")
    private WebElement successVcsConnectMessage;

    @FindBy(id = "projectName")
    private WebElement projectNameInput;

    @FindBy(id = "branch")
    private WebElement branchInput;

    @FindBy(id = "buildTypeName")
    private WebElement buildTypeNameInput;

    @FindBy(xpath = "//input[@value = 'Proceed']")
    private WebElement proceedBtn;

    @FindBy(xpath = "//input[@value='Use this']")
    private WebElement useThisBtn;


    public String checkSuccessConnectMessage() {
        return successVcsConnectMessage.getText();
    }

    @Step("Add project \"{projectName}\". Set Default branch = {branch}")
    public void addGeneralProjectProperties(String branch, String projectName) {
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);
        branchInput.clear();
        branchInput.sendKeys(branch);
        proceedBtn.click();
    }

    @Step("Add build {buildName}. Set Default branch = {branch}")
    public CreateFromURLPage addGeneralBuildProperties(String branch, String buildName) {
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildName);
        branchInput.clear();
        branchInput.sendKeys(branch);
        proceedBtn.click();
        return this;
    }

    @Step("Select \"Use this\" in \"Dublicate VCS Roots\" dialog")
    public void selectUseThisInDialog() {
        SeleniumUtils.waitElementToBeClickable(driver, useThisBtn, 5);
        useThisBtn.click();
    }

}
