package listeners;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;
import utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseTestListener implements IResultListener {


    private static Logger logger = LoggerFactory.getLogger(BaseTestListener.class);

    @Override
    public void onConfigurationSuccess(ITestResult iTestResult) {
    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
    }

    @Override
    public void onConfigurationSkip(ITestResult iTestResult) {
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        this.logger.info("============================");
        this.logger.info(this.getTestCaseName(iTestResult) + " started" + System.lineSeparator());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        this.logger.info(this.getTestCaseName(iTestResult) + " passed" + System.lineSeparator());
        this.logger.info("============================");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if (iTestResult.getThrowable() != null) {
            logErrorMessage(iTestResult.getThrowable());
        }
        this.logger.error("Test case " + this.getTestCaseName(iTestResult) + " failed " + iTestResult.getMethod().getTestClass());
        this.logger.info("============================");

        ITestContext context = iTestResult.getTestContext();
        WebDriver driver = (WebDriver) context.getAttribute("driver");

        CommonUtils.saveScreenShot(driver);
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
    }

    protected String getTestCaseName(ITestResult iTestResult) {
        String name = iTestResult.getName() != null ? iTestResult.getName() : iTestResult.getMethod().getMethodName();
        return name;
    }

    public static void logErrorMessage(Throwable throwable) {
        logger.error("Test Failure: ", throwable);
    }

}
