package Utilities;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;


public class BaseDriver {
    protected static WebDriver driver;
    protected String URL = "https://www.beymen.com/";
    public static WebDriver initializeDriver(){
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY,"true");
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver();
        return driver;
    }



}