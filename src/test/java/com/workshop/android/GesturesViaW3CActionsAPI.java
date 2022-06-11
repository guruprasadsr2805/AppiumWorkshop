package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.time.Duration;

import static java.time.Duration.ofMillis;
import static java.util.Collections.singletonList;

public class GesturesViaW3CActionsAPI {
    private AppiumDriverLocalService service;
    private AndroidDriver driver;
    private WebDriverWait wait;


    @BeforeSuite
    public void setupAppiumServer() {
        try {
            service = new AppiumServiceBuilder()
                    .usingPort(4723)
                    .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
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
            UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                    .autoGrantPermissions()
                    .setApp(System.getProperty("user.dir") + "/Apps/VodQA.apk");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), uiAutomator2Options);
            driver.setSetting(Setting.KEY_INJECTION_DELAY, 500);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void horizontalSwipingTest() {
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("slider1"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("slider")));

        Point source = driver.findElement(AppiumBy.accessibilityId("slider")).getLocation();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        sequence.addAction(finger.createPointerMove(ofMillis(0),
                PointerInput.Origin.viewport(), source.x, source.y));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
        sequence.addAction(new Pause(finger, ofMillis(600)));
        sequence.addAction(finger.createPointerMove(ofMillis(600),
                PointerInput.Origin.viewport(), source.x + 400, source.y));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
        driver.perform(singletonList(sequence));
    }

    @Test
    public void verticalSwipeTest() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("verticalSwipe"))).click();
        Point source = driver.findElement(AppiumBy.className("android.widget.ScrollView")).getLocation();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        sequence.addAction(finger.createPointerMove(ofMillis(0),
                PointerInput.Origin.viewport(),
                source.x / 4, source.y/2 + 400));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
        sequence.addAction(finger.createPointerMove(ofMillis(600),
                PointerInput.Origin.viewport(), source.getX() / 2, source.y / 2));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));
        driver.perform(singletonList(sequence));
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
