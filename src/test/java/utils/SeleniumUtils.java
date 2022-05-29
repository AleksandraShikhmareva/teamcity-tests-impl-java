package utils;

import dataProvider.ConfigFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SeleniumUtils {
    private static Logger logger = LoggerFactory.getLogger(SeleniumUtils.class);

    public static void waitVisibilityOfElement(WebDriver driver, WebElement element, long timeOutInSeconds) {
        new WebDriverWait(driver, timeOutInSeconds)
                .until(ExpectedConditions.visibilityOfAllElements(element));
    }

    public static void waitElementToBeClickable(WebDriver driver, WebElement element, long timeOutInSeconds) {
        ConfigFileReader configFileReader = new ConfigFileReader();
        try {
            Thread.sleep(1000);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Wait element exception", e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Long.parseLong(configFileReader.getProperty("defaultImplicitlyWait")), TimeUnit.SECONDS); //reset implicitlyWait
        }
    }

    public static WebElement waitForElementPresent(WebDriver driver, final By by, long timeOutInSeconds) {
        WebElement element;
        ConfigFileReader configFileReader = new ConfigFileReader();

        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

            driver.manage().timeouts().implicitlyWait(Long.parseLong(configFileReader.getProperty("defaultImplicitlyWait")), TimeUnit.SECONDS); //reset implicitlyWait
            return element;
        } catch (Exception e) {
            logger.error("Wait element exception", e);
        }
        return null;
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
