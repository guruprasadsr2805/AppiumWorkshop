package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;

public class RealDeviceWebAppTest {

    public AndroidDriver driver;

    /*
        Setting up desired capabilities to launch the Chrome browser on the real device / emulator
    */

    @BeforeSuite
    public void setup() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appium:newCommandTimeout", 900000);
            capabilities.setCapability("appium:adbExecTimeout", 70000);
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            capabilities.setCapability("appium:autoGrantPermissions", true);
            capabilities.setCapability("browserName", "Chrome");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.setSetting(Setting.KEY_INJECTION_DELAY, 500);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to navigate to a website and verify the error message for wrong login
     */
    @Test
    public void runTest() {
        try {
            Thread.sleep(2000);
            driver.get("https://www.saucedemo.com/");

            Thread.sleep(2000);
            driver.findElement(By.xpath("//input[@name=\"user-name\"]")).sendKeys("random");
            driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys("1234");
            driver.findElement(By.xpath("//input[@name=\"login-button\"]")).click();
            Thread.sleep(3000);
            String errorText = driver.findElement(By.cssSelector("h3")).getText();
            Assert.assertEquals(errorText, "Epic sadface: Username and password do not match any user in this service");
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
