package com.workshop.android;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;


/*
 * This code will be performing parallel execution of login operation
 * on two different devices. Settings needed for multiple devices are
 * specified in testng.xml file
 */
public class AndroidParallelTestsDemo {

    private final static String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private AndroidDriver driver;

    @BeforeTest(alwaysRun = true)
    @Parameters({"udid", "systemPort","device"})
    public void setup(String udid, String systemPort, String device) throws Exception {

        URL url = new URL(APPIUM_SERVER_URL);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:newCommandTimeout", 900000);
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:udid", udid);
        capabilities.setCapability("appium:systemPort", systemPort);
        capabilities.setCapability("appium:app", System.getProperty("user.dir") + "/Apps/VodQA.apk");
        capabilities.setCapability("appium:appWaitForLaunch", false);
        driver = new AndroidDriver(url, capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

    }

    @Test
    public void testAppTitle() {
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


    @AfterTest(alwaysRun = true)
    public void teardown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }



}