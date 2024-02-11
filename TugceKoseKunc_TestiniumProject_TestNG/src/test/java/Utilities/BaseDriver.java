package Utilities;


import com.beust.jcommander.Parameter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class BaseDriver {
    protected WebDriver driver;
    protected String URL = "https://www.beymen.com/";


    @BeforeTest
    @Parameters({"browser"})
    public void setUp(String browser) {
        ChromeOptions Options = new ChromeOptions();
        Options.addArguments("--remote-allow-origins=*");
        Options.addArguments("disable-notifications");


        if (browser.equals("firefox")){

            WebDriverManager.chromedriver().setup();

            driver = new ChromeDriver(Options);

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            driver.get(URL);

        } else if (browser.equals("chrome")) {


            driver = new FirefoxDriver();

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            driver.get(URL);

        }


    }

    //  @AfterTest
    //  public void tearDown(){
    //    driver.quit();
    // }
}