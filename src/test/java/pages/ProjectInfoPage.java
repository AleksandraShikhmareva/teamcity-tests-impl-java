package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.CommonUtils;
import utils.SeleniumUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ProjectInfoPage {

    WebDriver driver;

    public ProjectInfoPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[text()='Build Chains']")
    private WebElement buildChainTab;

    @FindBy(xpath = "//a[@title='Edit project settings']")
    private WebElement editProjectSettings;

    @Step("Open Build Chain Tab")
    public void openBuildChainTab() {
        buildChainTab.click();
    }

    @Step("Open Project Settings")
    public void editProjectSettings() {
        editProjectSettings.click();

    }

    public boolean hasAllBuildsInBuildChain(String... builds) {
        for (String build : builds) {
            try {
                driver.findElement(By.xpath("//div[@class='buildChainBuild']//a[text()=' " + build + "']"));
            } catch (NoSuchElementException e) {
                return false;
            }
        }
        return true;
    }

}
