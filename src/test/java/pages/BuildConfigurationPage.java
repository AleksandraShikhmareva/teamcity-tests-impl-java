package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import utils.SeleniumUtils;


public class BuildConfigurationPage {
    private WebDriver driver;

    public BuildConfigurationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /*Actions menu*/

    @FindBy(xpath = "//span[@id='sp_span_btActions']/button")
    private WebElement actionsBtn;

    @FindBy(xpath = "//a[text()='Copy configuration...']")
    private WebElement copyConfigurationItem;

    @FindBy(id = "newBuildTypeName")
    private WebElement newBuildNameInput;

    @FindBy(id = "copyBuildTypeFormDialog")
    private WebElement copyBuildTypeFormDialog;

    @FindBy(id = "-ufd-teamcity-ui-copyBuildTypeProjectId")
    private WebElement copyToProjectInput;

    @FindBy(id = "copyBuildTypeButton")
    private WebElement copyBtn;

    /* Dependencies Tab */

    @FindBy(xpath = "//li[@id='dependencies_Tab']/p/a")
    private WebElement dependenciesTab;

    @FindBy(xpath = "//span[text()='Add new snapshot dependency']")
    private WebElement addNewSnapshotDependencyBtn;

    @FindBy(id = "option:run-build-if-dependency-failed-to-start")
    private WebElement runBuildIfDependencyFailedToStartSelect;

    @FindBy(xpath = "//div[@id='sourceDependenciesDialog']//input[@value='Save']")
    private WebElement sourceDialogSaveBtn;

    @FindBy(id = "option:take-started-build-with-same-revisions")
    private WebElement doNotRunNewCheckbox;

    /*Build Step Tab*/

    @FindBy(xpath = "//li[@id='runType_Tab']/p/a")
    private WebElement buildStepsTab;

    @FindBy(xpath = "//div[@id = 'advancedSettingsToggle_editBuildTypeForm']/a")
    private WebElement advancedOptionsLink;

    @FindBy(id = "runnerArgs")
    private WebElement runnerArgsInput;

    @FindBy(xpath = "//span[contains(@class,'addNew')]")
    private WebElement addNewBtn;

    @FindBy(xpath = "//a[text()='Go to build configuration page']")
    private WebElement goToConfigurationPageBtn;

    @FindBy(id = "goals")
    private WebElement goalsInput;

    @FindBy(id = "buildStepName")
    private WebElement stepNameInput;

    @FindBy(id = "buildTypeSettingsContainer")
    private WebElement buildTypeSettingsContainer;

    @FindBy(xpath = "//div[@id='mainContent']//input[@type='submit']")
    private WebElement saveBuildStepBtn;

    /*Parameters tab*/

    @FindBy(xpath = "//li[@id='buildParams_Tab']/p/a")
    private WebElement parametersTab;

    @FindBy(id = "parameterName")
    private WebElement parameterNameInput;

    @FindBy(id = "parameterValue")
    private WebElement parameterValueInput;

    @FindBy(xpath = "//div[@id='editParamFormDialog']//input[@type='submit']")
    private WebElement submitBtn;

    @FindBy(xpath = "//table[@id='confTable']//tr[@class='ownParam']")
    private WebElement parameterRaw;

    @FindBy(id = "-ufd-teamcity-ui-runTypeInfoKey")
    private WebElement optionInput;

    @FindBy(xpath = "//div//button[text()='Run']")
    private WebElement runBtn;

    @FindBy(id = "footer")
    private WebElement footer;


    public void goToConfigurationPage() {
        SeleniumUtils.waitElementToBeClickable(driver, goToConfigurationPageBtn, 5);
        goToConfigurationPageBtn.click();
        SeleniumUtils.waitElementToBeClickable(driver, runBtn, 5);
    }

    /*Build Actions*/

    @Step("Select copy configuration item in Action menu")
    public void copyConfiguration(String projectName, String buildName) {
        actionsBtn.click();
        copyConfigurationItem.click();
        SeleniumUtils.customSelect(driver, projectName, copyToProjectInput, copyBuildTypeFormDialog);
        newBuildNameInput.clear();
        newBuildNameInput.sendKeys(buildName);
        copyBtn.click();
    }

    /*Methods for Dependencies tab*/

    @Step("Open Dependency tab")
    public BuildConfigurationPage openDependencyTab() {
        SeleniumUtils.waitElementToBeClickable(driver, dependenciesTab, 5);
        dependenciesTab.click();
        return this;
    }

    @Step("Add new snapshot dependency with depends on {buildName}")
    public BuildConfigurationPage addNewSnapshotDependency(boolean hasDoNotRunNewOption, String buildName) {
        SeleniumUtils.waitElementToBeClickable(driver, addNewSnapshotDependencyBtn, 10);
        addNewSnapshotDependencyBtn.click();
        driver.findElement(By.xpath("//span[text()='" + buildName + "']/../parent::span/preceding-sibling::input[@class='ring-checkbox-input']")).click();
        if (hasDoNotRunNewOption) {
            doNotRunNewCheckbox.click();
        }
        Select select = new Select(runBuildIfDependencyFailedToStartSelect);
        select.selectByValue("CANCEL");
        sourceDialogSaveBtn.click();
        return this;
    }

    /*Methods for Parameter tab*/

    @Step("Open Parameters tab")
    public BuildConfigurationPage openParametersTab() {
        SeleniumUtils.waitElementToBeClickable(driver, parametersTab, 10);
        parametersTab.click();
        return this;
    }

    @Step("Edit parameter {name}. Set value {value}")
    public BuildConfigurationPage editParameter(String name, String value) {
        SeleniumUtils.waitElementToBeClickable(driver, addNewBtn, 10);
        driver.findElement(By.xpath("//a[@name='" + name + "']/parent::td/parent::tr/child::td[@class='edit highlight']/a")).click();
        parameterValueInput.clear();
        parameterValueInput.sendKeys(value);
        submitBtn.click();
        SeleniumUtils.waitForElementPresent(driver, By.xpath("//table[@id='confTable']//tr[@class='ownParam']"), 2);
        return this;
    }

    @Step("Add parameter {name} with value {value}")
    public BuildConfigurationPage addNewParameter(String name, String value) {
        SeleniumUtils.waitElementToBeClickable(driver, addNewBtn, 10);
        addNewBtn.click();
        parameterNameInput.sendKeys(name);
        parameterValueInput.sendKeys(value);
        submitBtn.click();
        SeleniumUtils.waitForElementPresent(driver, By.xpath("//table[@id='confTable']//tr[@class='ownParam']"), 2);
        return this;
    }

    /*Methods for Build Steps Tab*/

    @Step("Add new build step \"{stepName}\" with runner type \"{runner}\"")
    public BuildConfigurationPage addNewBuildStep(String runner, String stepName, String goals, String runnerArgs) {
        SeleniumUtils.waitElementToBeClickable(driver, addNewBtn, 10);
        addNewBtn.click();
        SeleniumUtils.customSelect(driver, runner, optionInput, buildTypeSettingsContainer);
        SeleniumUtils.waitElementToBeClickable(driver, stepNameInput, 5);
        stepNameInput.sendKeys(stepName);
        goalsInput.sendKeys(goals);
        SeleniumUtils.moveToElement(driver, footer);
        if (advancedOptionsLink.getText().equals("Show advanced options")) {
            advancedOptionsLink.click();
        }
        runnerArgsInput.sendKeys(runnerArgs);
        saveBuildStepBtn.click();
        return this;
    }

    public BuildConfigurationPage openBuildStepsTab() {
        buildStepsTab.click();
        return this;
    }

}
