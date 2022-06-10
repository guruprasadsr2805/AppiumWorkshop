package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;


public class BrowserStackDemo {
    private AndroidDriver driver;
    private String userName = "<username>";
    private String accessKey = "<accesskey>";
    /*
        Setting up desired capabilities to to run the tests on browser stack
    */

    @BeforeSuite
    public void setup() {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();

            // Set your access credentials
            browserstackOptions.put("userName", userName);
            browserstackOptions.put("accessKey", accessKey);

            // Set other BrowserStack capabilities
            browserstackOptions.put("appiumVersion", "1.22.0");
            browserstackOptions.put("projectName", "First Java Project");
            browserstackOptions.put("buildName", "browserstack-build-1");
            browserstackOptions.put("sessionName", "first_test");

            // Passing browserstack capabilities inside bstack:options
            caps.setCapability("bstack:options", browserstackOptions);

            // Set URL of the application under test
            caps.setCapability("app", "bs://a8ec26893a6ddf8ba1a6f052cb1f9e7a8e100d71");

            // Specify deviceName and platformName for testing
            caps.setCapability("deviceName", "Google Pixel 3");
            caps.setCapability("platformName", "android");
            caps.setCapability("platformVersion", "9.0");
            driver = new AndroidDriver(new URL("http://hub.browserstack.com/wd/hub"), caps);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runTest() {
        try {
            driver.findElement(AppiumBy.accessibilityId("Webview Demo")).click();
            driver.findElement(AppiumBy.accessibilityId("urlInput")).sendKeys("https://appiumpro.com");
            driver.findElement(AppiumBy.androidUIAutomator("textContains(\"Go\")")).click();

            Thread.sleep(2000);
            String pageURL = driver.findElement(By.className("android.webkit.WebView")).getText();
            System.out.println("Page Title is " + pageURL);
            Assert.assertEquals(pageURL, "Appium Pro");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
        Killing the driver and ending the session
     */

    @AfterSuite
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


