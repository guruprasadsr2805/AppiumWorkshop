package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class RealDeviceNativeAppTest {

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
            capabilities.setCapability("appium:app", System.getProperty("user.dir") + "/Apps/VodQA.apk");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.setSetting(Setting.KEY_INJECTION_DELAY, 500);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to navigate to the Native view and validate the presence of text
     */
    @Test
    public void runTest() {
        try {
            driver.findElement(AppiumBy.className("android.widget.Button")).click();
            Thread.sleep(5000);
            driver.findElement(AppiumBy.androidUIAutomator("textStartsWith(\"Native View\")")).click();
            Thread.sleep(2000);
            List<WebElement> viewElements = driver.findElements(AppiumBy.androidUIAutomator("description(\"textView\")"));
            Assert.assertEquals(viewElements.get(0).getText(),"Hello World, I'm View one ");
            Assert.assertEquals(viewElements.get(1).getText(), "Hello World, I'm View two ");
            Assert.assertEquals(viewElements.get(2).getText(), "Hello World, I'm View three ");
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
