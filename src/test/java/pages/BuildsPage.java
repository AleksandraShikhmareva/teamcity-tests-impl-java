package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildsPage {
    private static Logger logger = LoggerFactory.getLogger(BuildsPage.class);
    private WebDriver driver;

    public BuildsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div//button[text()='Run']")
    private WebElement runBtn;

    @FindBy(xpath = "//td[@class=' status']/span/span")
    private WebElement statusIconElement;

    @Step("Run build")
    public void runBuild() {
        runBtn.click();
    }

    @Step("Check last build for {expectedStatus} status in UI")
    public boolean hasExpectedStatus(String expectedStatus) {
        return statusIconElement.getAttribute("class").contains(expectedStatus.toLowerCase());
    }

    @Step("Open information about build with id {buildId}")
    public void openBuildInfo(int buildId) {
        driver.findElement(By.xpath("//div[@data-build-id = '" + buildId + "']//*[contains(@class, 'MiddleEllipsis__searchable')]")).click();
    }
}