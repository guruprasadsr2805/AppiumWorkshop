package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;

public class WaitsWithAppiumPlugin {

    /*
    Before running this test, make sure that we install the appium-wait-plugin.
    Please run the below command to install the plugin
    appium plugin install --source=npm appium-wait-plugin
    https://github.com/AppiumTestDistribution/appium-wait-plugin
     */
    private String AUTH_USER = "alice";
    private String AUTH_PASS = "mypassword";

    private By loginScreen = AppiumBy.accessibilityId("Login Screen");
    private By loginBtn = AppiumBy.accessibilityId("loginBtn");
    private By username = AppiumBy.accessibilityId("username");
    private By password = AppiumBy.accessibilityId("password");

    private AppiumDriverLocalService service;
    private AndroidDriver driver;
    private WebDriverWait wait;

    private By getLoggedInBy(String username) {
        return By.xpath("//android.widget.TextView[@text=\"You are logged in as " + username + "\"]");
    }

    @BeforeClass
    public void setup() {
        try {

            service = new AppiumServiceBuilder()
                    .usingPort(4723)
                    .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                    .withArgument(GeneralServerFlag.USE_PLUGINS, "element-wait")
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

    @BeforeMethod
    public void launchApp() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appium:newCommandTimeout", 900000);
            capabilities.setCapability("appium:adbExecTimeout", 70000);
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            capabilities.setCapability("appium:autoGrantPermissions", true);
            capabilities.setCapability("appium:app", "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk");
            driver = new AndroidDriver(service.getUrl(), capabilities);
            driver.setSetting(Setting.KEY_INJECTION_DELAY, 500);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (Exception e) {

        }
    }

    @AfterClass
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

    @Test
    public void testLogin_PluginWait() {
        driver.findElement(loginScreen).click();
        driver.findElement(username).sendKeys(AUTH_USER);
        driver.findElement(password).sendKeys(AUTH_PASS);
        driver.findElement(loginBtn).click();
        driver.findElement(getLoggedInBy(AUTH_USER));
    }
}
