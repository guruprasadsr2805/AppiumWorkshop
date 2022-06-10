package com.workshop.android;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class DeepLinkDemo {
    private String APP_ANDROID = "https://github.com/cloudgrey-io/the-app/releases/download/v1.2.1/TheApp-v1.2.1.apk";
    private String AUTH_USER = "alice";
    private String AUTH_PASS = "mypassword";
    private AppiumDriverLocalService service;
    private AndroidDriver driver;

    @BeforeSuite
    public void setupAppiumServer() {
        try {
            service = new AppiumServiceBuilder()
                    .usingPort(4723)
                    .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                    .withLogFile(new File("./Appium_Server_Log.txt"))
                    .build();
            service.start();

            if (service == null || !service.isRunning()) {
                throw new RuntimeException("An appium server node is not started!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @BeforeTest
    public void setup() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appium:newCommandTimeout", 900000);
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            capabilities.setCapability("appium:autoGrantPermissions", true);
            capabilities.setCapability("appium:app", System.getProperty("user.dir") + "/Apps/TheApp-v1.10.0.apk");
            capabilities.setCapability("appium:appWaitForLaunch", false);
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeepLinkForDirectNavAndroid () throws IOException {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            driver.get("theapp://login/" + AUTH_USER + "/" + AUTH_PASS);
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(getLoggedInBy(AUTH_USER)));
            Assert.assertEquals(driver.findElement(getLoggedInBy(AUTH_USER)).getText(),
                    "You are logged in as " + AUTH_USER);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public By getLoggedInBy(String username) {
        return By.xpath("//android.widget.TextView[@text=\"You are logged in as " + username + "\"]");
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
