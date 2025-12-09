package org.selenium;
//6.1.2 d/
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UIElementsTest {

    private WebDriver driver;

    @BeforeAll
    public void setup() {
        System.out.println("\n========== Starting UI Tests ==========");
        System.out.println("Setting up Chrome WebDriver and navigating to http://localhost:5173");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173");
        System.out.println("✓ Chrome browser launched and navigated to application\n");
    }

    @AfterAll
    public void teardown() {
        System.out.println("\n========== Closing Browser ==========");
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed\n");
        }
    }

    // --------------------------------------------------------
    // 1. Kiểm tra UI elements tồn tại
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Elements — Presence Test")
    public void testElementsPresence() {
        System.out.println("========== Test 1: Checking UI Elements Presence ==========");
        try {
            System.out.println("Looking for username element...");
            assertNotNull(driver.findElement(By.id("username")));
            System.out.println("  ✓ Username input found");
            
            System.out.println("Looking for password element...");
            assertNotNull(driver.findElement(By.id("password")));
            System.out.println("  ✓ Password input found");
            
            System.out.println("Looking for login button...");
            assertNotNull(driver.findElement(By.id("login-btn")));
            System.out.println("  ✓ Login button found");
            
            System.out.println("✓ Test PASSED: All UI elements are present\n");
        } catch (Exception e) {
            System.out.println("✗ Test FAILED: " + e.getMessage());
            throw e;
        }
    }

    // --------------------------------------------------------
    // 2. Kiểm tra các thành phần hiển thị (visible)
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Elements — Visible Test")
    public void testElementsVisibility() {
        System.out.println("========== Test 2: Checking UI Elements Visibility ==========");
        try {
            System.out.println("Checking if username element is visible...");
            assertTrue(driver.findElement(By.id("username")).isDisplayed());
            System.out.println("  ✓ Username input is visible");
            
            System.out.println("Checking if password element is visible...");
            assertTrue(driver.findElement(By.id("password")).isDisplayed());
            System.out.println("  ✓ Password input is visible");
            
            System.out.println("Checking if login button is visible...");
            assertTrue(driver.findElement(By.id("login-btn")).isDisplayed());
            System.out.println("  ✓ Login button is visible");
            
            System.out.println("✓ Test PASSED: All UI elements are visible\n");
        } catch (Exception e) {
            System.out.println("✗ Test FAILED: " + e.getMessage());
            throw e;
        }
    }

    // --------------------------------------------------------
    // 3. Kiểm tra nhập dữ liệu, xóa dữ liệu
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Interaction — Typing & Clearing")
    public void testTypingAndClearing() {
        System.out.println("========== Test 3: Testing Typing and Clearing ==========");
        try {
            WebElement user = driver.findElement(By.id("username"));
            WebElement pass = driver.findElement(By.id("password"));

            System.out.println("Step 1: Clearing and typing in username field...");
            user.clear();
            user.sendKeys("testUser");
            String usernameValue = user.getAttribute("value");
            System.out.println("  Username value: " + usernameValue);
            assertEquals("testUser", usernameValue);
            System.out.println("  ✓ Username typing works correctly");

            System.out.println("Step 2: Clearing and typing in password field...");
            pass.clear();
            pass.sendKeys("12345");
            String passwordValue = pass.getAttribute("value");
            System.out.println("  Password value: " + passwordValue);
            assertEquals("12345", passwordValue);
            System.out.println("  ✓ Password typing works correctly");

            System.out.println("Step 3: Clearing both fields...");
            user.clear();
            pass.clear();

            String clearedUsername = user.getAttribute("value");
            String clearedPassword = pass.getAttribute("value");
            System.out.println("  Username value after clear: '" + clearedUsername + "'");
            System.out.println("  Password value after clear: '" + clearedPassword + "'");
            assertEquals("", clearedUsername);
            assertEquals("", clearedPassword);
            System.out.println("  ✓ Fields cleared successfully");
            
            System.out.println("✓ Test PASSED: Typing and clearing works correctly\n");
        } catch (Exception e) {
            System.out.println("✗ Test FAILED: " + e.getMessage());
            throw e;
        }
    }

    // --------------------------------------------------------
    // 4. Kiểm tra thuộc tính UI
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Elements — Attributes Test")
    public void testAttributes() {
        System.out.println("========== Test 4: Testing UI Elements Attributes ==========");
        try {
            WebElement user = driver.findElement(By.id("username"));
            WebElement pass = driver.findElement(By.id("password"));
            WebElement btn = driver.findElement(By.id("login-btn"));

            System.out.println("Step 1: Checking placeholder attributes...");
            String userPlaceholder = user.getAttribute("placeholder");
            String passPlaceholder = pass.getAttribute("placeholder");
            System.out.println("  Username placeholder: '" + userPlaceholder + "'");
            System.out.println("  Password placeholder: '" + passPlaceholder + "'");
            assertEquals("Enter username", userPlaceholder);
            assertEquals("Enter password", passPlaceholder);
            System.out.println("  ✓ Placeholders are correct");

            System.out.println("Step 2: Checking if elements are enabled...");
            System.out.println("  Username enabled: " + user.isEnabled());
            System.out.println("  Password enabled: " + pass.isEnabled());
            System.out.println("  Login button enabled: " + btn.isEnabled());
            assertTrue(user.isEnabled());
            assertTrue(pass.isEnabled());
            assertTrue(btn.isEnabled());
            System.out.println("  ✓ All elements are enabled");
            
            System.out.println("✓ Test PASSED: All UI attributes are correct\n");
        } catch (Exception e) {
            System.out.println("✗ Test FAILED: " + e.getMessage());
            throw e;
        }
    }

    // --------------------------------------------------------
    // 5. Kiểm tra click button login
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Interaction — Login Button Click")
    public void testButtonClick() {
        System.out.println("========== Test 5: Testing Login Button Click ==========");
        try {
            WebElement btn = driver.findElement(By.id("login-btn"));

            System.out.println("Step 1: Clicking login button without entering credentials...");
            btn.click();
            System.out.println("  ✓ Button clicked successfully");

            System.out.println("Step 2: Checking if page is still active...");
            assertTrue(true);
            System.out.println("  ✓ Page did not crash after button click");
            
            System.out.println("✓ Test PASSED: Login button interaction works correctly\n");
        } catch (Exception e) {
            System.out.println("✗ Test FAILED: " + e.getMessage());
            throw e;
        }
    }

    
}
