import dataProvider.ConfigFileReader;
import listeners.BaseTestListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import utils.ApiHelper;

import java.util.concurrent.TimeUnit;

@Listeners({BaseTestListener.class})

public abstract class BaseTest {

    protected WebDriver driver;
    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);
    ConfigFileReader configFileReader = new ConfigFileReader();
    protected ApiHelper apiHelper = new ApiHelper();;

    @BeforeClass
    public void setUp(ITestContext iTestContext) {
        logger.info("Starting chrome driver");
        if (configFileReader.getProperty("mode").equals("remoteRun")) {
            System.setProperty("webdriver.chrome.driver", configFileReader.getProperty("pathToDriver"));
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "start-maximized", "disable-infobars", "--disable-extensions");
            driver = new ChromeDriver(options);
        } else if (configFileReader.getProperty("mode").equals("localRun")) {
            System.setProperty("webdriver.chrome.driver", configFileReader.getProperty("pathToDriver"));
            driver = new ChromeDriver();
            driver.manage().window().fullscreen();
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Long.parseLong(configFileReader.getProperty("defaultImplicitlyWait")), TimeUnit.SECONDS);
        driver.get(configFileReader.getProperty("url"));
        logger.info("Opening " + configFileReader.getProperty("url"));
        iTestContext.setAttribute("driver", driver);
    }

    @AfterClass
    public void close() {
        driver.close();
        logger.info("Closing driver");
        driver.quit();
        logger.info("Closing browser");
    }

}
