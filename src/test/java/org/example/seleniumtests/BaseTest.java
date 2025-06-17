package org.example.seleniumtests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;



public class BaseTest {
    protected WebDriver driver;
    protected final String baseUrl = "https://www.saucedemo.com/";

    @BeforeMethod
    public void setUp(){
        WebDriverManager.chromedriver().setup();

        //Добавил этот блок из-за выскакивающего окна браузера "смены пароля...", не давал закончить тест

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-save-password-bubble");
//        options.addArguments("--user-data-dir=/tmp/selenium-profile");


        driver = new ChromeDriver(options);
        driver.get(baseUrl);
    }

    @AfterMethod
    public void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }
}
