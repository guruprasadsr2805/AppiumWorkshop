package com.workshop.android;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidUiAutomatorWebAppDemo {

    /*
     * This code will be opening a web page in Android Chrome browser using Appium
     * Appium capability - browserName = Chrome
     */
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:platformVersion", "11");
        capabilities.setCapability("appium:deviceName", "Pixel2_API30_AndroidR11");
        capabilities.setCapability("appium:avd","Pixel2_API30_AndroidR11");
        capabilities.setCapability("appium:newCommandTimeout", 900000);
        capabilities.setCapability("appium:adbExecTimeout", 70000);
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("browserName", "Chrome");
        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Opening URL
        driver.get("https://gitlab.com/");

        Thread.sleep(2000);
        // Extracting page title
        System.out.println("Page title = " + driver.getTitle());

        Thread.sleep(2000);

        driver.findElement(By.cssSelector("div.phone  button:nth-child(3)")).click();
        driver.findElement(By.cssSelector("div.nav-menu > div a[href='https://gitlab.com/users/sign_in/']")).click();

        Thread.sleep(10000);
        // Quiting the driver
        driver.quit();
    }
}
