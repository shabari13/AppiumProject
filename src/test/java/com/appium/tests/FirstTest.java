package com.appium.tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class FirstTest {

    private AndroidDriver driver;
    private WebDriverWait wait;
    String testEmail;
    String testPassword;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        String email = System.getenv("KINDLE_EMAIL");
        System.out.println("Fetched email from Jenkins");
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
        caps.setCapability("appium:deviceName", "emulator-5554");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:appPackage", "com.amazon.kindle");
        caps.setCapability("appium:appActivity", "com.amazon.kindle.UpgradePage");
        caps.setCapability("appium:noReset", false); 
        caps.setCapability("appium:fullReset", false); 

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
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Sign In button not found: " + e.getMessage());
        }

        // Step 4: Enter email/mobile number
        try {
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.EditText[@hint='Enter mobile number or email']")
            ));
            emailField.click();
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
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Continue button not found: " + e.getMessage());
            throw e;
        }

        // Step 6: Enter password
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
            Thread.sleep(5000); // Wait for login to complete
        } catch (Exception e) {
            System.out.println("Sign In submit button not found: " + e.getMessage());
            throw e;
        }

        System.out.println("Login completed successfully!");

        // Step 8: Click on Search box
        try {
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.id("com.amazon.kindle:id/search_box")
            ));
            searchBox.click();
            System.out.println("Clicked on Search box");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Search box not found: " + e.getMessage());
            throw e;
        }

        // Step 9: Enter search text "Appium Recipes"
        try {
            // After clicking search box, the active EditText should be focused
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.className("android.widget.EditText")
            ));
            searchInput.sendKeys("Appium Recipes");
            System.out.println("Entered search text: Appium Recipes");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Search input field not found: " + e.getMessage());
            throw e;
        }

        // Step 10: Press Enter to search
        try {
            driver.pressKey(new KeyEvent(AndroidKey.ENTER));
            System.out.println("Pressed Enter key to search");
            Thread.sleep(3000); // Wait for search results
        } catch (Exception e) {
            System.out.println("Failed to press Enter: " + e.getMessage());
        }

        // Step 11: Validate search results - Check for cover images
        try {
            List<WebElement> searchResults = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                AppiumBy.id("com.amazon.kindle:id/cover_image")
            ));
            
            // Assert that search results are present
            Assert.assertTrue(searchResults.size() > 0, "No search results found!");
            System.out.println("Search results validated successfully! Found " + searchResults.size() + " result(s)");
            
            // Additional validation - check if at least one result is displayed
            boolean isResultDisplayed = searchResults.get(0).isDisplayed();
            Assert.assertTrue(isResultDisplayed, "Search result is not displayed!");
            System.out.println("First search result is visible on screen");
            
        } catch (Exception e) {
            System.out.println("Search result validation failed: " + e.getMessage());
            Assert.fail("Search results not found or not displayed");
        }

        System.out.println("Search test completed successfully!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}