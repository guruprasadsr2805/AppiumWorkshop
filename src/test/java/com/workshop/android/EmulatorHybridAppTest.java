package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;


public class EmulatorHybridAppTest {
    public AndroidDriver driver;

    /*
        Setting up desired capabilities to launch the VodQA app on the real device / emulator
    */

    @BeforeSuite
    public void setup() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appium:newCommandTimeout", 900000);
            capabilities.setCapability("appium:adbExecTimeout", 70000);
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            capabilities.setCapability("appium:autoGrantPermissions", true);
            capabilities.setCapability("appium:app", System.getProperty("user.dir") + "/Apps/TheApp-v1.10.0.apk");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to navigate to the Native view and validate the page title
     */
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
