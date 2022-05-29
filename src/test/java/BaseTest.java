import dataProvider.ConfigFileReader;
import listeners.BaseTestListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.*;
import utils.ApiHelper;
import java.time.Duration;


@Listeners({BaseTestListener.class})

public abstract class BaseTest {

    protected WebDriver driver;
    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);
    ConfigFileReader configFileReader = new ConfigFileReader();
    protected ApiHelper apiHelper = new ApiHelper();;

    @BeforeSuite
    public void setUp(ITestContext iTestContext) {
        logger.info("Starting chrome driver");
        System.setProperty("webdriver.chrome.driver", configFileReader.getProperty("pathToDriver"));
        if (configFileReader.getProperty("mode").equals("remoteRun")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(
                    "--headless",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "start-maximized",
                    "disable-infobars",
                    "--disable-extensions"
            );
            driver = new ChromeDriver(options);
        } else if (configFileReader.getProperty("mode").equals("localRun")) {
            driver = new ChromeDriver();
            driver.manage().window().fullscreen();
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(configFileReader.getProperty("defaultImplicitlyWait"))));
        driver.get(configFileReader.getProperty("url"));
        logger.info("Opening " + configFileReader.getProperty("url"));
        iTestContext.setAttribute("driver", driver);
    }

    @AfterSuite
    public void close() {
        driver.close();
        logger.info("Closing driver");
        driver.quit();
        logger.info("Closing browser");
    }

}
