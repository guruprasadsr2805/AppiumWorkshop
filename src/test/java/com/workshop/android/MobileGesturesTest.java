package com.workshop.android;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class MobileGesturesTest {

    private AppiumDriverLocalService service;
    private AndroidDriver driver;
    private WebDriverWait wait;


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

    @BeforeMethod
    public void launchApp() {
        try {
            UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                    .autoGrantPermissions()
                    .setNewCommandTimeout(Duration.ofMillis(900000))
                    .setAdbExecTimeout(Duration.ofMillis(900000))
                    .setApp(System.getProperty("user.dir") + "/Apps/VodQA.apk");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), uiAutomator2Options);
            driver.setSetting(Setting.KEY_INJECTION_DELAY, 500);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to perform the double tap gesture
     */
    @Test
    public void performDoubleTapGesture() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Double Tap\")"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Double Tap Demo\")")));

            WebElement elementToDoubleTap = driver.findElement(AppiumBy.androidUIAutomator("textStartsWith(\"Double Tap Me\")"));

            ((JavascriptExecutor) driver).executeScript("mobile: doubleClickGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) elementToDoubleTap).getId()
            ));

            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id("android:id/alertTitle")));

            Assert.assertEquals(driver.findElement(AppiumBy.id("android:id/alertTitle")).getText(), "Double Tap");
            Assert.assertEquals(driver.findElement(AppiumBy.id("android:id/message")).getText(), "Double tap successful!");
            Assert.assertEquals(driver.findElement(AppiumBy.id("android:id/button1")).getText(), "OK");

            driver.findElement(AppiumBy.id("android:id/button1")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to perform the long press gesture
     */
    @Test
    public void performLongPressGesture() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Long Press\")"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Long Press Demo\")")));

            WebElement elementToLongPress = driver.findElement(AppiumBy.androidUIAutomator("text(\"Long Press Me\")"));

            ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) elementToLongPress).getId(), "duration", 2000
            ));

            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.id("android:id/alertTitle")));

            Assert.assertEquals(driver.findElement(AppiumBy.id("android:id/alertTitle")).getText(), "Long Pressed");
            Assert.assertEquals(driver.findElement(AppiumBy.id("android:id/message")).getText(), "you pressed me hard :P");
            Assert.assertEquals(driver.findElement(AppiumBy.id("android:id/button1")).getText(), "OK");

            driver.findElement(AppiumBy.id("android:id/button1")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to perform the tap gesture
     */
    @Test
    public void performTapGesture() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button")));

            WebElement elementToTap = driver.findElement(AppiumBy.className("android.widget.Button"));

            ((JavascriptExecutor) driver).executeScript("mobile: clickGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) elementToTap).getId()
            ));

            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("text(\"Samples List\")")));
            driver.findElement(AppiumBy.androidUIAutomator("text(\"Back\")")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to perform the drag gesture
     */
    @Test
    public void performDragGesture() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Drag & Drop\")"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Drag me!\")")));

            WebElement elementToBeDragged = driver.findElement(AppiumBy.androidUIAutomator("text(\"Drag me!\")"));
            WebElement elementWhereDropHappens = driver.findElement(AppiumBy.androidUIAutomator("text(\"Drop here.\")"));

            ((JavascriptExecutor) driver).executeScript("mobile: dragGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) elementToBeDragged).getId(),
                    "endX", elementWhereDropHappens.getLocation().getX() + (driver.manage().window().getSize().width/2),
                    "endY", elementWhereDropHappens.getLocation().getY() + 100,
                    "speed", 700
            ));

            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("success")));

            Assert.assertEquals(driver.findElement(AppiumBy.accessibilityId("success")).getText(), "Circle dropped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to perform the pinch open gesture (zoom in)
     */
    @Test
    public void performPinchOpenGesture() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Photo View\")"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Photo Screen\")")));

            WebElement elementToZoomIn = driver.findElement(AppiumBy.className("android.widget.ImageView"));

            // Java
            ((JavascriptExecutor) driver).executeScript("mobile: pinchOpenGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) elementToZoomIn).getId(),
                    "percent", 0.50,
                    "speed", 750
            ));

            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Test to perform the pinch close gesture (zoom out)
     */
    @Test
    public void performPinchCloseGesture() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.Button"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Photo View\")"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.androidUIAutomator("textStartsWith(\"Photo Screen\")")));

            WebElement elementToZoomOut = driver.findElement(AppiumBy.className("android.widget.ImageView"));

            // Java
            ((JavascriptExecutor) driver).executeScript("mobile: pinchCloseGesture", ImmutableMap.of(
                    "elementId", ((RemoteWebElement) elementToZoomOut).getId(),
                    "percent", 0.50,
                    "speed", 750
            ));
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
