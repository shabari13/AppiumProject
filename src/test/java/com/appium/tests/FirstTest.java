package com.appium.tests;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class FirstTest {
    
    private AndroidDriver driver;
    
    @BeforeClass
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("automationName", "UiAutomator2");
        
        // Test Calculator app (pre-installed on emulator)
        caps.setCapability("appPackage", "com.amazon.kindle");
        caps.setCapability("appActivity", "com.amazon.kindle.UpgradePage");
        
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @Test
    public void testCalculator() throws InterruptedException {
        System.out.println("Kindle app launched successfully!");
        Thread.sleep(3000); // Just to see it running
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}