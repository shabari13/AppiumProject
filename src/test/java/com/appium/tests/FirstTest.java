package com.appium.tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class FirstTest {

    private AndroidDriver driver;
    private WebDriverWait wait;
    String testEmail;
    String testPassword;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        String email = System.getenv("KINDLE_EMAIL");
        System.out.print("fetced email from Jenkins");
        String password = System.getenv("KINDLE_PASSWORD");
        if (email == null || password == null) {
            // Fallback for local testing
            email = "shabars+201@amazon.com";
            password = "labone2six";
        }
        this.testEmail = email;
        this.testPassword = password;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("automationName", "UiAutomator2");

        // Kindle app
        caps.setCapability("appPackage", "com.amazon.kindle");
        caps.setCapability("appActivity", "com.amazon.kindle.UpgradePage");
        caps.setCapability("noReset", false); 
        caps.setCapability("fullReset", false); 

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Initialize explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void testKindleLogin() throws InterruptedException {
        System.out.println("Kindle app launched successfully!");

        // Step 1: Handle notification permission popup - Click "Allow"
        try {
            WebElement allowButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button")
            ));
            allowButton.click();
            System.out.println("Clicked on Allow notification button");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Allow button not found or already handled");
        }

        // Step 2: Click on Library tab
        try {
            WebElement libraryTab = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.amazon.kindle:id/library_tab")
            ));
            libraryTab.click();
            System.out.println("Clicked on Library tab");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Library tab not found: " + e.getMessage());
        }

        // Step 3: Click on Sign In button
        try {
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.amazon.kindle:id/empty_library_sign_in")
            ));
            signInButton.click();
            System.out.println("Clicked on Sign In button");
            Thread.sleep(3000); // Wait for login page to load
        } catch (Exception e) {
            System.out.println("Sign In button not found: " + e.getMessage());
        }

        // Step 4: Enter email/mobile number in the EditText field
        try {
            // Find the email input field using XPath with class and hint
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.EditText[@hint='Enter mobile number or email']")
            ));
            emailField.click(); // Click to focus
            emailField.sendKeys(testEmail);
            System.out.println("Entered email address");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Email field not found: " + e.getMessage());
            throw e;
        }

        // Step 5: Click Continue button
        try {
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@text='Continue']")
            ));
            continueButton.click();
            System.out.println("Clicked on Continue button");
            Thread.sleep(3000); // Wait for next page
        } catch (Exception e) {
            System.out.println("Continue button not found: " + e.getMessage());
            throw e;
        }

        try {
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.EditText[@hint='Amazon password']")
            ));
            passwordField.click();
            passwordField.sendKeys(testPassword);
            System.out.println("Entered password");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Password field not found: " + e.getMessage());
            throw e;
        }

        // Step 7: Click Sign In button (final submit)
        try {
            WebElement signInSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.xpath("//android.widget.Button[@text='Sign in']")
            ));
            signInSubmitButton.click();
            System.out.println("Clicked on Sign In submit button");
            Thread.sleep(15000); // Wait for login to complete and app to load
        } catch (Exception e) {
            System.out.println("Sign In submit button not found: " + e.getMessage());
            throw e;
        }

        System.out.println("Login completed successfully!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}