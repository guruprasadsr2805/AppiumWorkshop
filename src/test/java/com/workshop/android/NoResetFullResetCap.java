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
import java.util.Map;

public class NoResetFullResetCap {
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
            capabilities.setCapability("appium:noReset", false);
            capabilities.setCapability("appium:fullReset", true);
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
            driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc=\"" +
                    "Clipboard Demo\"]/android.widget.TextView[1]")).click();
            Thread.sleep(5000);
            WebElement clipBoardText = driver.findElements(By.className("android.widget.TextView")).get(1);
            System.out.println("Default Clipboard text is " + clipBoardText.getText());
            driver.findElement(By.xpath("//android.widget.EditText" +
                    "[@content-desc=\"messageInput\"]")).sendKeys("new clipboard text");
            driver.findElement((By.xpath("//android.view.ViewGroup" +
                    "[@content-desc=\"setClipboardText\"]/android.widget.TextView"))).click();
            driver.findElement(By.xpath("//android.view.ViewGroup" +
                    "[@content-desc=\"refreshClipboardText\"]/android.widget.TextView")).click();
            Thread.sleep(3000);
            clipBoardText = driver.findElements(By.className("android.widget.TextView")).get(1);
            System.out.println("new Clipboard text is " + clipBoardText.getText());
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "/failureScreenshots/screenshot.png"));
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

