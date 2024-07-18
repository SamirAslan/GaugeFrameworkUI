package util;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;


public class Driver{
    public static WebDriver driver;

    @BeforeScenario
    public void setUp() {
        String BaseUrl = "https://www.google.com";
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS).implicitlyWait(10,TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(BaseUrl);
        }
    }
    @AfterScenario
    public void closeDriver(){
        driver.quit();
    }
}
