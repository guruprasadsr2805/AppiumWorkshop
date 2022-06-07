package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class AndroidUIAutomatorHybridAppDemo {

    /*
     * This code will be performing the following using Appium:
     * 1. Will open an hybrid android App (having both native as well as webview screeens)
     * 2. Perform few operations in the native screen
     * 3. Change the context to web view and perform operation in web view
     */
    public static void main(String[] args) throws MalformedURLException, InterruptedException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:platformVersion", "11");
        capabilities.setCapability("appium:deviceName", "Pixel2_API30_AndroidR11");
        capabilities.setCapability("appium:avd","Pixel2_API30_AndroidR11");
        capabilities.setCapability("appium:newCommandTimeout", 900000);
        capabilities.setCapability("appium:adbExecTimeout", 70000);
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:app", "https://github.com/cloudgrey-io/the-app/releases/download/v1.10.0/TheApp-v1.10.0.apk");
        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

        driver.findElement(AppiumBy.accessibilityId("Webview Demo")).click();
        driver.findElement(AppiumBy.accessibilityId("urlInput")).sendKeys("https://appiumpro.com");
        driver.findElement(AppiumBy.accessibilityId("navigateBtn")).click();

        int counter = 5;
        String webViewContext = "";

        /*
         * Waiting for web view context to be active and available to interact
         */
        while(counter>0)
        {
            Set<String> contextNames = driver.getContextHandles();
            for (String contextName : contextNames)
            {
                System.out.println("Context name = " + contextName);
                Thread.sleep(2000);
                if(!contextName.equals("NATIVE_APP"))
                {
                    webViewContext = contextName;
                    Thread.sleep(2000);
                    break;

                }
            }
            if(webViewContext.isEmpty())
            {
                Thread.sleep(2000);
                counter --;
            }
            else
                break;
        }
        /*
         * Setting driver context as web view
         * before interacting with any element in web view screen
         */
        driver.context(webViewContext);
        System.out.println("Current driver context is " + driver.getContext());
        System.out.println("Webview Title is "+ driver.getTitle());

        /*
         * Setting driver context back as NATIVE_APP
         */
        driver.context("NATIVE_APP");
        driver.quit();


    }
}
