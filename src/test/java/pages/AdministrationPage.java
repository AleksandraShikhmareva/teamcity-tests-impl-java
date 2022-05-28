package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdministrationPage {

    public AdministrationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//p[@class='createProject']/a")
    private WebElement createProjectBtn;

    @Step("Create Project")
    public void createProject() {
        createProjectBtn.click();
    }
}
