package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class UiAutomator2OptionsStartingAppiumServerViaCodeNativeAppTest {

    private AppiumDriverLocalService service;
    public AndroidDriver driver;

    /*
        Setting up UiAutomator2Options capabilities to launch the VodQA app on the real device / emulator
    */

    @BeforeSuite
    public void setup() {
        try {
            service = new AppiumServiceBuilder()
                    .usingPort(4723)
                    .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                    .build();
            service.start();

            if (service == null || !service.isRunning()) {
                throw new RuntimeException("An appium server node is not started!");
            } else {
                UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                        .autoGrantPermissions()
                        .setNewCommandTimeout(Duration.ofMillis(20000))
                        .setAdbExecTimeout(Duration.ofMillis(20000))
                        .setApp(System.getProperty("user.dir") + "/Apps/VodQA.apk");
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), uiAutomator2Options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            }
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
            Assert.assertEquals(viewElements.get(0).getText(), "Hello World, I'm View one ");
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
            if (driver != null) {
                driver.quit();
            }
            if (service != null) {
                service.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
