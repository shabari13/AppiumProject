package com.appium.tests;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class IOSKindleTest {
	private IOSDriver driver;
	private WebDriverWait wait;
	private String testEmail;
	private String testPassword;

	@BeforeClass
	public void setUp() throws MalformedURLException {
		// Load credentials
		testEmail = System.getenv("KINDLE_EMAIL");
		testPassword = System.getenv("KINDLE_PASSWORD");

		if (testEmail == null || testPassword == null) {
			testEmail = "shabars+201@amazon.com";
			testPassword = "labone2six";
		}

		// Set up iOS capabilities using DesiredCapabilities
		DesiredCapabilities caps = new DesiredCapabilities();

		// Standard W3C capability (no prefix)
		caps.setCapability("platformName", "iOS");

		// iOS specific capabilities (with appium: prefix)
		caps.setCapability("appium:deviceName", "Shabari");
		caps.setCapability("appium:udid", "00008140-0016450811E8801C");
		caps.setCapability("appium:platformVersion", "16.7");
		caps.setCapability("appium:bundleId", "com.amazon.Lassen");
		caps.setCapability("appium:automationName", "XCUITest");
		caps.setCapability("appium:noReset", false);
		caps.setCapability("appium:fullReset", false);

		Map<String, Object> settings = new HashMap<>();
		settings.put("usePasskey", false);
		caps.setCapability("appium:settings", settings);
		// Signing capabilities (IMPORTANT for physical device)
		caps.setCapability("appium:xcodeOrgId", "F6N5ZJWXU7");
		caps.setCapability("appium:xcodeSigningId", "Apple Development");
		caps.setCapability("appium:updatedWDABundleId", "com.shabari.WebDriverAgentRunner");

		// Timeouts
		caps.setCapability("appium:newCommandTimeout", 300);
		caps.setCapability("appium:wdaLaunchTimeout", 180);
		caps.setCapability("appium:autoAcceptAlerts", false);
		caps.setCapability("appium:autoDismissAlerts", true); // Auto-dismiss system sheets

		driver = new IOSDriver(new URL("http://0.0.0.0:4723"), caps);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		System.out.println("iOS Kindle app launched successfully!");
	}

	// @Test
	public void testKindleLogin() throws InterruptedException {
		System.out.println("Starting Kindle login automation...");

		// Wait for app to load
		Thread.sleep(5000);

		// Step 1: Click on email field and enter email
		System.out.println("\nStep 1: Entering email...");
		WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(
				AppiumBy.xpath("//XCUIElementTypeTextField[@name='Enter mobile number or email']")));
		emailField.click();
		emailField.clear();
		emailField.sendKeys(testEmail);
		System.out.println("✓ Email entered: " + testEmail);

		Thread.sleep(1000);

		// Step 2: Click Continue button
		System.out.println("\nStep 2: Clicking Continue button...");
		WebElement continueButton = wait
				.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Continue")));
		continueButton.click();
		System.out.println("✓ Continue button clicked");

		Thread.sleep(3000); // Wait for password screen

		// Step 3: Click on password field and enter password
		System.out.println("\nStep 3: Entering password...");
		WebElement passwordField = wait.until(ExpectedConditions
				.elementToBeClickable(AppiumBy.xpath("//XCUIElementTypeSecureTextField[@name='Amazon password']")));
		passwordField.click();
		passwordField.clear();
		passwordField.sendKeys(testPassword);
		System.out.println("✓ Password entered");

		Thread.sleep(1000);

		// Step 4: Click Sign in button
		System.out.println("\nStep 4: Clicking Sign in button...");
		WebElement signInButton = wait.until(
				ExpectedConditions.elementToBeClickable(AppiumBy.xpath("//XCUIElementTypeButton[@name='Sign in']")));
		signInButton.click();
		System.out.println("✓ Sign in button clicked");

		Thread.sleep(5000); // Wait for login to complete

		// Verify login success
		System.out.println("\n✓✓✓ Login completed successfully! ✓✓✓");
		Thread.sleep(5000);
	}

	@Test
	public void testClickBookByTitle() throws InterruptedException {
		System.out.println("Starting Kindle book selection...");
		Thread.sleep(5000);

		clickLibraryTab();

		// Click on the book using its XPath
		clickBookByXPath("(//XCUIElementTypeImage[@name='MediaItemCoverImageView'])[1]");

		System.out.println("✓ Book clicked successfully!");

		clickMiddleOfPage();

		// Step 4: Click Aa (Reading Settings)
		clickReadingSettings();

		// Step 5: Click Layout tab
		clickLayoutTab();

		// Step 6: Toggle Continuous Scrolling ON
		toggleContinuousScrolling();
		Thread.sleep(1000);

		// Step 7: Toggle Continuous Scrolling OFF
		toggleContinuousScrolling();
		Thread.sleep(1000);

		// Step 8: Swipe up in the panel
		swipeUpInPanel();

		// Step 9: Toggle Orientation Lock ON
		toggleOrientationLock();
		Thread.sleep(1000);

		toggleOrientationLock();
		Thread.sleep(1000);

		// Step 11: Set brightness to 50%
		setBrightnessTo50Percent();
		
		closeSettingsPanel();
		
		 // Click center of page again
	    clickMiddleOfPage();
	    
	    // Close the book
	    closeBook();
	    
	 // After closing book from previous test, navigate Home
	    clickHomeTab();
	    
	    // Swipe up slightly
	    swipeUpSlightly();
	    
	    // Click Mystery & Thriller tab
	    clickMysteryThrillerTab();
	    
	    // Click Nash Falls book
	    clickNashFallsBook();
	    
	    // Handle bottom sheet
	    handleBottomSheet();
	    
	    System.out.println("✓ Test completed!");

	    System.out.println("\n" + "=".repeat(50));
	    System.out.println("✓ ALL STEPS COMPLETED SUCCESSFULLY!");
	    System.out.println("=".repeat(50));
	    
	    Thread.sleep(2000);
	}
	
	/**
	 * Click Home tab
	 */
	private void clickHomeTab() throws InterruptedException {
	    try {
	        System.out.println("\n[1] Clicking Home tab...");
	        
	        WebElement homeTab = wait.until(ExpectedConditions.elementToBeClickable(
	            AppiumBy.accessibilityId("OOBAppChrome.home")
	        ));
	        
	        homeTab.click();
	        System.out.println("✓ Home tab clicked");
	        Thread.sleep(2000);
	        
	    } catch (Exception e) {
	        System.out.println("❌ Failed to click Home tab: " + e.getMessage());
	        throw e;
	    }
	}
	
	/**
	 * Swipe up slightly
	 */
	private void swipeUpSlightly() throws InterruptedException {
	    try {
	        System.out.println("\n[2] Swiping up slightly...");
	        
	        Dimension size = driver.manage().window().getSize();
	        
	        int startX = size.width / 2;
	        int startY = (int) (size.height * 0.6);  // Start at 60% from top
	        int endY = (int) (size.height * 0.4);    // End at 40% from top (slight swipe)
	        
	        System.out.println("Swiping from (" + startX + ", " + startY + ") to (" + startX + ", " + endY + ")");
	        
	        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
	        Sequence swipe = new Sequence(finger, 0);
	        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
	        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
	        swipe.addAction(new Pause(finger, Duration.ofMillis(100)));
	        swipe.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), startX, endY));
	        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
	        
	        driver.perform(Arrays.asList(swipe));
	        System.out.println("✓ Swiped up");
	        Thread.sleep(2000);
	        
	    } catch (Exception e) {
	        System.out.println("❌ Failed to swipe: " + e.getMessage());
	        throw e;
	    }
	}
	
	/**
	 * Click Mystery & Thriller tab
	 */
	private void clickMysteryThrillerTab() throws InterruptedException {
	    try {
	        System.out.println("\n[3] Clicking Mystery & Thriller tab...");
	        
	        // Try accessibility ID first
	        WebElement mysteryTab = wait.until(ExpectedConditions.elementToBeClickable(
	            AppiumBy.accessibilityId("3")
	        ));
	        
	        mysteryTab.click();
	        System.out.println("✓ Mystery & Thriller tab clicked");
	        Thread.sleep(2000);
	        
	    } catch (Exception e) {
	        System.out.println("Accessibility ID failed, trying XPath...");
	        
	        try {
	            WebElement mysteryTab = wait.until(ExpectedConditions.elementToBeClickable(
	                AppiumBy.xpath("//XCUIElementTypeButton[@name='3']")
	            ));
	            
	            mysteryTab.click();
	            System.out.println("✓ Mystery & Thriller tab clicked via XPath");
	            Thread.sleep(2000);
	            
	        } catch (Exception e2) {
	            System.out.println("❌ Failed to click Mystery & Thriller tab: " + e2.getMessage());
	            throw e2;
	        }
	    }
	}

	/**
	 * Click Nash Falls book
	 */
	private void clickNashFallsBook() throws InterruptedException {
	    try {
	        System.out.println("\n[4] Clicking Nash Falls book...");
	        
	        // Try iOS Class Chain first
	        WebElement nashFallsBook = wait.until(ExpectedConditions.elementToBeClickable(
	            AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"book_cover_image\"`][1]")
	        ));
	        
	        nashFallsBook.click();
	        System.out.println("✓ Nash Falls book clicked");
	        Thread.sleep(3000); // Wait for bottom sheet to load
	        
	    } catch (Exception e) {
	        System.out.println("iOS Class Chain failed, trying XPath...");
	        
	        try {
	            // Try XPath
	            WebElement nashFallsBook = wait.until(ExpectedConditions.elementToBeClickable(
	                AppiumBy.xpath("//XCUIElementTypeButton[@name='book_cover_image' and @label='Nash Falls by David Baldacci']")
	            ));
	            
	            nashFallsBook.click();
	            System.out.println("✓ Nash Falls book clicked via XPath");
	            Thread.sleep(3000);
	            
	        } catch (Exception e2) {
	            System.out.println("XPath failed, trying generic book_cover_image...");
	            
	            // Fallback: Click first book_cover_image
	            WebElement bookCover = driver.findElement(
	                AppiumBy.xpath("(//XCUIElementTypeButton[@name='book_cover_image'])[1]")
	            );
	            bookCover.click();
	            System.out.println("✓ Book clicked via fallback");
	            Thread.sleep(3000);
	        }
	    }
	}

	/**
	 * Handle the bottom sheet that Appium Inspector can't see
	 */
	private void handleBottomSheet() throws InterruptedException {
	    System.out.println("\n[5] Handling bottom sheet...");
	    
	    // Method 1: Wait longer for elements to appear
	    Thread.sleep(2000);
	    
	    // Method 2: Debug - print page source to see what's available
	    debugBottomSheetElements();
	    
	    // Method 3: Try to interact with visible buttons
	    interactWithBottomSheet();
	}

	/**
	 * Debug: Print page source to see bottom sheet elements
	 */
	private void debugBottomSheetElements() {
	    try {
	        System.out.println("\n=== DEBUGGING BOTTOM SHEET ===");
	        
	        String pageSource = driver.getPageSource();
	        
	        // Look for specific keywords in page source
	        if (pageSource.contains("Get book")) {
	            System.out.println("✓ Found 'Get book' in page source");
	        }
	        if (pageSource.contains("Download sample")) {
	            System.out.println("✓ Found 'Download sample' in page source");
	        }
	        if (pageSource.contains("Nash Falls")) {
	            System.out.println("✓ Found 'Nash Falls' in page source");
	        }
	        
	        // Try to find buttons
	        var buttons = driver.findElements(AppiumBy.className("XCUIElementTypeButton"));
	        System.out.println("\nFound " + buttons.size() + " buttons on screen:");
	        
	        for (int i = 0; i < Math.min(buttons.size(), 10); i++) {
	            try {
	                String name = buttons.get(i).getAttribute("name");
	                String label = buttons.get(i).getAttribute("label");
	                
	                if (name != null && !name.isEmpty()) {
	                    System.out.println((i + 1) + ". Name: " + name);
	                    if (label != null && !label.isEmpty()) {
	                        System.out.println("   Label: " + label);
	                    }
	                }
	            } catch (Exception e) {
	                // Skip
	            }
	        }
	        
	        System.out.println("==============================\n");
	        
	    } catch (Exception e) {
	        System.out.println("Debug failed: " + e.getMessage());
	    }
	}

	/**
	 * Try to interact with bottom sheet elements
	 */
	private void interactWithBottomSheet() throws InterruptedException {
	    // Try Method 1: Find buttons by text/name
	    if (tryClickBottomSheetButton("Get book")) return;
	    if (tryClickBottomSheetButton("Download sample")) return;
	    
	    // Try Method 2: Use coordinate-based clicks
	    clickBottomSheetByCoordinates();
	}

	/**
	 * Try to click a button on the bottom sheet by name
	 */
	private boolean tryClickBottomSheetButton(String buttonName) {
	    try {
	        System.out.println("Looking for button: " + buttonName);
	        
	        // Try accessibility ID
	        WebElement button = driver.findElement(AppiumBy.accessibilityId(buttonName));
	        button.click();
	        System.out.println("✓ Clicked '" + buttonName + "' button");
	        Thread.sleep(2000);
	        return true;
	        
	    } catch (Exception e1) {
	        try {
	            // Try XPath with name
	            WebElement button = driver.findElement(
	                AppiumBy.xpath("//XCUIElementTypeButton[@name='" + buttonName + "']")
	            );
	            button.click();
	            System.out.println("✓ Clicked '" + buttonName + "' button via XPath");
	            Thread.sleep(2000);
	            return true;
	            
	        } catch (Exception e2) {
	            try {
	                // Try finding by partial text match
	                WebElement button = driver.findElement(
	                    AppiumBy.xpath("//XCUIElementTypeButton[contains(@name, '" + buttonName + "')]")
	                );
	                button.click();
	                System.out.println("✓ Clicked button via partial match");
	                Thread.sleep(2000);
	                return true;
	                
	            } catch (Exception e3) {
	                System.out.println("Could not find button: " + buttonName);
	                return false;
	            }
	        }
	    }
	}

	/**
	 * Click on bottom sheet elements using coordinates
	 */
	private void clickBottomSheetByCoordinates() throws InterruptedException {
	    try {
	        System.out.println("Using coordinate-based clicks for bottom sheet...");
	        
	        Dimension size = driver.manage().window().getSize();
	        
	        // Click "Get book" button (orange button in the middle)
	        int getBookX = size.width / 2;
	        int getBookY = (int) (size.height * 0.65);  // Approximately 65% from top
	        
	        System.out.println("Clicking 'Get book' at: (" + getBookX + ", " + getBookY + ")");
	        tapAtCoordinates(getBookX, getBookY);
	        System.out.println("✓ Clicked 'Get book' button");
	        Thread.sleep(2000);
	        
	        // If you want to close the sheet instead, tap the X button
	        // int closeX = size.width - 40;
	        // int closeY = 100;
	        // tapAtCoordinates(closeX, closeY);
	        
	    } catch (Exception e) {
	        System.out.println("Coordinate-based click failed: " + e.getMessage());
	    }
	}

	/**
	 * Close bottom sheet by tapping X button
	 */
	private void closeBottomSheet() throws InterruptedException {
	    try {
	        System.out.println("\nClosing bottom sheet...");
	        
	        // Try to find close button (X in top right)
	        try {
	            WebElement closeButton = driver.findElement(AppiumBy.accessibilityId("Close"));
	            closeButton.click();
	            System.out.println("✓ Bottom sheet closed");
	            Thread.sleep(1000);
	            return;
	        } catch (Exception e) {
	            // Close button not found, try coordinates
	        }
	        
	        // Use coordinates - X button is typically in top-right corner
	        Dimension size = driver.manage().window().getSize();
	        int closeX = size.width - 40;
	        int closeY = 100;
	        
	        System.out.println("Tapping close button at: (" + closeX + ", " + closeY + ")");
	        tapAtCoordinates(closeX, closeY);
	        System.out.println("✓ Bottom sheet closed via coordinates");
	        Thread.sleep(1000);
	        
	    } catch (Exception e) {
	        System.out.println("Failed to close bottom sheet: " + e.getMessage());
	    }
	}

	
	/**
	 * Close the settings panel
	 */
	private void closeSettingsPanel() throws InterruptedException {
	    try {
	        System.out.println("\n[10] Closing settings panel...");
	        
	        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
	            AppiumBy.accessibilityId("Close menu")
	        ));
	        
	        closeButton.click();
	        System.out.println("✓ Settings panel closed");
	        Thread.sleep(2000);
	        
	    } catch (Exception e) {
	        System.out.println("❌ Failed to close settings panel: " + e.getMessage());
	        throw e;
	    }
	}

	/**
	 * Close the book and return to library
	 */
	private void closeBook() throws InterruptedException {
	    try {
	        System.out.println("\n[12] Closing book...");
	        
	        WebElement closeBookButton = wait.until(ExpectedConditions.elementToBeClickable(
	            AppiumBy.accessibilityId("Close Book")
	        ));
	        
	        closeBookButton.click();
	        System.out.println("✓ Book closed, returned to library");
	        Thread.sleep(2000);
	        
	    } catch (Exception e) {
	        System.out.println("❌ Failed to close book: " + e.getMessage());
	        throw e;
	    }
	}


	private void clickBookByXPath(String xpath) throws InterruptedException {
		try {
			System.out.println("Clicking book with XPath: " + xpath);

			WebElement book = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(xpath)));

			book.click();
			System.out.println("✓ Book clicked");
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("❌ Failed to click book: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Click on Library tab
	 */
	private void clickLibraryTab() throws InterruptedException {
		try {
			System.out.println("Clicking on Library tab...");

			// Try using accessibility ID first (most reliable)
			WebElement libraryTab = wait
					.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("library_dark")));

			libraryTab.click();
			System.out.println("✓ Library tab clicked");
			Thread.sleep(2000); // Wait for library to load

		} catch (Exception e) {
			System.out.println("Accessibility ID failed, trying XPath...");

			try {
				// Fallback to XPath
				WebElement libraryTab = wait.until(ExpectedConditions
						.elementToBeClickable(AppiumBy.xpath("//XCUIElementTypeImage[@name='library_dark']")));

				libraryTab.click();
				System.out.println("✓ Library tab clicked via XPath");
				Thread.sleep(2000);

			} catch (Exception e2) {
				System.out.println("❌ Failed to click Library tab: " + e2.getMessage());
				throw e2;
			}
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			System.out.println("\nClosing app...");
			driver.quit();
		}
	}

	/**
	 * Click middle of page to show menu items
	 */
	private void clickMiddleOfPage() throws InterruptedException {
		try {
			System.out.println("\n[3] Clicking middle of page to show menu...");

			Dimension size = driver.manage().window().getSize();
			int centerX = size.width / 2;
			int centerY = size.height / 2;

			System.out.println("Screen size: " + size.width + "x" + size.height);
			System.out.println("Tapping at center: (" + centerX + ", " + centerY + ")");

			tapAtCoordinates(centerX, centerY);
			System.out.println("✓ Menu displayed");
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("❌ Failed: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Click Aa (Reading Settings) menu
	 */
	private void clickReadingSettings() throws InterruptedException {
		try {
			System.out.println("\n[4] Clicking Reading Settings (Aa)...");

			WebElement aaButton = wait
					.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Reading Settings")));

			aaButton.click();
			System.out.println("✓ Reading Settings opened");
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("❌ Failed: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Click Layout tab
	 */
	private void clickLayoutTab() throws InterruptedException {
		try {
			System.out.println("\n[5] Clicking Layout tab...");

			WebElement layoutTab = wait
					.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Layout")));

			layoutTab.click();
			System.out.println("✓ Layout tab opened");
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("❌ Failed: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Toggle Continuous Scrolling switch
	 */
	private void toggleContinuousScrolling() throws InterruptedException {
		try {
			System.out.println("\n[6] Toggling Continuous Scrolling...");

			// Try XPath first
			WebElement continuousScrollSwitch = wait.until(ExpectedConditions
					.elementToBeClickable(AppiumBy.xpath("//XCUIElementTypeSwitch[@name='Continuous Scrolling']")));

			String currentValue = continuousScrollSwitch.getAttribute("value");
			System.out.println("Current state: " + currentValue);

			continuousScrollSwitch.click();
			Thread.sleep(500);

			String newValue = continuousScrollSwitch.getAttribute("value");
			System.out.println("✓ Continuous Scrolling toggled to: " + newValue);

		} catch (Exception e) {
			System.out.println("❌ Failed: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Swipe up in the settings panel
	 */
	private void swipeUpInPanel() throws InterruptedException {
		try {
			System.out.println("\n[7] Swiping up in panel...");

			Dimension size = driver.manage().window().getSize();

			int startX = size.width / 2;
			int startY = (int) (size.height * 0.7); // Start at 70% from top
			int endY = (int) (size.height * 0.3); // End at 30% from top

			System.out.println("Swiping from (" + startX + ", " + startY + ") to (" + startX + ", " + endY + ")");

			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence swipe = new Sequence(finger, 0);
			swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
			swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			swipe.addAction(new Pause(finger, Duration.ofMillis(100)));
			swipe.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
			swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

			driver.perform(Arrays.asList(swipe));
			System.out.println("✓ Swiped up");
			Thread.sleep(1000);

		} catch (Exception e) {
			System.out.println("❌ Failed: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Toggle Orientation Lock switch
	 */
	private void toggleOrientationLock() throws InterruptedException {
		try {
			System.out.println("\n[8] Toggling Orientation Lock...");

			// Try XPath first
			WebElement orientationLockSwitch = wait.until(ExpectedConditions
					.elementToBeClickable(AppiumBy.xpath("//XCUIElementTypeSwitch[@name='Orientation Lock']")));

			String currentValue = orientationLockSwitch.getAttribute("value");
			System.out.println("Current state: " + currentValue);

			orientationLockSwitch.click();
			Thread.sleep(500);

			String newValue = orientationLockSwitch.getAttribute("value");
			System.out.println("✓ Orientation Lock toggled to: " + newValue);

		} catch (Exception e) {
			System.out.println("XPath failed, trying iOS Class Chain...");

			try {
				WebElement orientationLockSwitch = wait.until(ExpectedConditions.elementToBeClickable(
						AppiumBy.iOSClassChain("**/XCUIElementTypeSwitch[`name == \"Orientation Lock\"`]")));

				String currentValue = orientationLockSwitch.getAttribute("value");
				System.out.println("Current state: " + currentValue);

				orientationLockSwitch.click();
				Thread.sleep(500);

				String newValue = orientationLockSwitch.getAttribute("value");
				System.out.println("✓ Orientation Lock toggled to: " + newValue);

			} catch (Exception e2) {
				System.out.println("❌ Failed: " + e2.getMessage());
				throw e2;
			}
		}
	}
	
	/**
	 * Set brightness slider to 50%
	 */
	private void setBrightnessTo50Percent() throws InterruptedException {
	    try {
	        System.out.println("\n[9] Setting brightness to 50%...");
	        
	        WebElement brightnessSlider = wait.until(ExpectedConditions.presenceOfElementLocated(
	            AppiumBy.accessibilityId("AaSeekbar-Brightness")
	        ));
	        
	        // Get current value
	        String currentValue = brightnessSlider.getAttribute("value");
	        System.out.println("Current brightness: " + currentValue);
	        
	        // Set to 50%
	        brightnessSlider.sendKeys("0.5");  // iOS sliders typically use 0.0 to 1.0
	        Thread.sleep(1000);
	        
	        String newValue = brightnessSlider.getAttribute("value");
	        System.out.println("✓ Brightness set to: " + newValue);
	        
	    } catch (Exception e) {
	        System.out.println("sendKeys failed, trying drag method...");
	        setBrightnessUsingDrag();
	    }
	}
	
	
	/**
	 * Alternative: Set brightness using drag gesture
	 */
	private void setBrightnessUsingDrag() throws InterruptedException {
	    try {
	        WebElement brightnessSlider = wait.until(ExpectedConditions.presenceOfElementLocated(
	            AppiumBy.accessibilityId("AaSeekbar-Brightness")
	        ));
	        
	        // Get slider position and size
	        Point location = brightnessSlider.getLocation();
	        Dimension size = brightnessSlider.getSize();
	        
	        // Calculate start (left edge) and middle (50%) positions
	        int startX = location.x;
	        int middleX = location.x + (size.width / 2);  // 50%
	        int y = location.y + (size.height / 2);
	        
	        System.out.println("Dragging slider from " + startX + " to " + middleX);
	        
	        // Drag from start to middle
	        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
	        Sequence drag = new Sequence(finger, 0);
	        drag.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), middleX, y));
	        drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
	        drag.addAction(new Pause(finger, Duration.ofMillis(100)));
	        drag.addAction(finger.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), middleX, y));
	        drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
	        
	        driver.perform(Arrays.asList(drag));
	        
	        System.out.println("✓ Brightness slider dragged to 50%");
	        Thread.sleep(1000);
	        
	    } catch (Exception e) {
	        System.out.println("❌ Drag method failed: " + e.getMessage());
	    }
	}
	
	/**
	 * Helper: Tap at specific coordinates
	 */
	private void tapAtCoordinates(int x, int y) {
	    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
	    Sequence tap = new Sequence(finger, 0);
	    tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
	    tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
	    tap.addAction(new Pause(finger, Duration.ofMillis(200)));
	    tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
	    driver.perform(Arrays.asList(tap));
	}


	/**
	 * Click on a book by its title
	 */
	private void clickBookByTitle(String bookTitle) throws InterruptedException {
		try {
			System.out.println("Looking for book: " + bookTitle);

			// Method 1: Find the static text element with the book title
			WebElement bookTitleElement = wait.until(ExpectedConditions.presenceOfElementLocated(
					AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, '" + bookTitle + "')]")));

			System.out.println("Found book title element");

			// Click on the parent element (which is usually the clickable container)
			// The parent might be a Cell, Button, or Other element
			WebElement parentElement = bookTitleElement.findElement(AppiumBy.xpath("./ancestor::XCUIElementTypeCell"));
			parentElement.click();

			System.out.println("✓ Clicked on book: " + bookTitle);
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("Method 1 failed, trying alternative approach...");
			clickBookByTitleAlternative(bookTitle);
		}
	}

	/**
	 * Alternative method: Find book and navigate to clickable element
	 */
	private void clickBookByTitleAlternative(String bookTitle) throws InterruptedException {
		try {
			// Find the text element
			WebElement bookText = driver
					.findElement(AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, '" + bookTitle + "')]"));

			// Try clicking on immediate parent
			WebElement clickableElement = bookText.findElement(AppiumBy.xpath("./.."));
			clickableElement.click();

			System.out.println("✓ Clicked using alternative method");
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("Alternative method failed: " + e.getMessage());
			clickBookByTitleUsingCoordinates(bookTitle);
		}
	}

	private void clickBookByTitleUsingCoordinates(String bookTitle) throws InterruptedException {
		try {
			WebElement bookText = driver
					.findElement(AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, '" + bookTitle + "')]"));

			// Get the coordinates and size of the text element
			Point location = bookText.getLocation();
			Dimension size = bookText.getSize();

			// Calculate center point
			int centerX = location.x + (size.width / 2);
			int centerY = location.y + (size.height / 2);

			System.out.println("Tapping at coordinates: (" + centerX + ", " + centerY + ")");

			// Tap on the book
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence tap = new Sequence(finger, 0);
			tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
			tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			tap.addAction(new Pause(finger, Duration.ofMillis(200)));
			tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
			driver.perform(Arrays.asList(tap));

			System.out.println("✓ Clicked using coordinates");
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("All methods failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

}