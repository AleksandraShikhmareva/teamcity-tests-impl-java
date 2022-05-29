package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class SeleniumUtils {
    private static Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);

    public static void waitVisibilityOfElement(WebDriver driver, WebElement element, long timeOutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitElementToBeClickable(WebDriver driver, WebElement element, long timeOutInSeconds) {
        try {
            Thread.sleep(500);
            new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds))
                    .until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Wait element exception", e);
        }
    }

    public static void waitForElementPresent(WebDriver driver, final By by, long timeOutInSeconds) {
        try {
            Thread.sleep(500);
            new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).
                    until(ExpectedConditions.presenceOfElementLocated(by));

        } catch (Exception e) {
            logger.error("Wait element exception", e);
        }
    }

    public static void moveToElement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
    }

    public static void customSelect(WebDriver driver, String value, WebElement select, WebElement form) {
        select.clear();
        select.sendKeys(value);
        WebElement selectedValue = driver.findElement(By.xpath("//li[@data-title='" + value + "']"));
        SeleniumUtils.waitVisibilityOfElement(driver, selectedValue, 10);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        form.click();
    }
}
