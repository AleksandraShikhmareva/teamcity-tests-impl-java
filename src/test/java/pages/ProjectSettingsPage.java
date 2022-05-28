package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProjectSettingsPage {

    WebDriver driver;

    public ProjectSettingsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[text()='Create build configuration']/parent::a")
    private WebElement createBuildConfigurationBtn;

    @FindBy(xpath = "//span[text()='Create build configuration']/parent::a")
    private WebElement buildConfigurationLink;

    @Step("Create Build Configuration")
    public void createBuildConfiguration() {
        createBuildConfigurationBtn.click();
    }

    @Step("Open configuration of build with name \"{buildConfigurationName}\"")
    public void openBuildConfiguration(String buildConfigurationName) {
        driver.findElement(By.xpath("//table[@id='configurations']//strong[text()='" + buildConfigurationName + "']/parent::td")).click();
    }

}
