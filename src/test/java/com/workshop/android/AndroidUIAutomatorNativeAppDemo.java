package com.workshop.android;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AndroidUIAutomatorNativeAppDemo {

    /*
    This test class is going to show the test case of VodQA app, Native View tests.
    */

    private AndroidDriver driver;

    @BeforeSuite
    public void setUp(){
        try{
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("appium:platformVersion", "11");
            capabilities.setCapability("appium:deviceName", "Pixel2_API30_AndroidR11");
            capabilities.setCapability("appium:avd","Pixel2_API30_AndroidR11");
            capabilities.setCapability("appium:newCommandTimeout", 900000);
            capabilities.setCapability("appium:adbExecTimeout", 70000);
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            capabilities.setCapability("appium:autoGrantPermissions", true);
            capabilities.setCapability("appium:app", System.getProperty("user.dir") + "/Apps/VodQA.apk");
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            Thread.sleep(2000);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void locateByText(){
        try{
            driver.findElement(AppiumBy.className("android.widget.Button")).click();
            Thread.sleep(5000);
            driver.findElement(AppiumBy.androidUIAutomator("textStartsWith(\"Native View\")")).click();
            Thread.sleep(2000);
            List<WebElement> viewElements = driver.findElements(AppiumBy.androidUIAutomator("description(\"textView\")"));
            assertThat(viewElements.get(0).getText(), is("Hello World, I'm View one"));
            assertThat(viewElements.get(1).getText(), is("Hello World, I'm View two"));
            assertThat(viewElements.get(2).getText(), is("Hello World, I'm View three"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @AfterSuite
    public void tearDown(){
        try {
            driver.quit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
