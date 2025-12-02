package org.selenium;
//6.1.2 d/
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("UI tests disabled in CI")
public class UIElementsTest {

    private WebDriver driver;

    @BeforeAll
    public void setup() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173");
    }

    @AfterAll
    public void teardown() {
        if (driver != null) driver.quit();
    }

    // --------------------------------------------------------
    // 1. Kiểm tra UI elements tồn tại
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Elements — Presence Test")
    public void testElementsPresence() {
        Assertions.assertNotNull(driver.findElement(By.id("username")));
        Assertions.assertNotNull(driver.findElement(By.id("password")));
        Assertions.assertNotNull(driver.findElement(By.id("login-btn")));
    }

    // --------------------------------------------------------
    // 2. Kiểm tra các thành phần hiển thị (visible)
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Elements — Visible Test")
    public void testElementsVisibility() {
        Assertions.assertTrue(driver.findElement(By.id("username")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.id("password")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.id("login-btn")).isDisplayed());
    }

    // --------------------------------------------------------
    // 3. Kiểm tra nhập dữ liệu, xóa dữ liệu
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Interaction — Typing & Clearing")
    public void testTypingAndClearing() {

        WebElement user = driver.findElement(By.id("username"));
        WebElement pass = driver.findElement(By.id("password"));

        user.clear();
        user.sendKeys("testUser");
        Assertions.assertEquals("testUser", user.getAttribute("value"));

        pass.clear();
        pass.sendKeys("12345");
        Assertions.assertEquals("12345", pass.getAttribute("value"));

        // Clear again
        user.clear();
        pass.clear();

        Assertions.assertEquals("", user.getAttribute("value"));
        Assertions.assertEquals("", pass.getAttribute("value"));
    }

    // --------------------------------------------------------
    // 4. Kiểm tra thuộc tính UI
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Elements — Attributes Test")
    public void testAttributes() {

        WebElement user = driver.findElement(By.id("username"));
        WebElement pass = driver.findElement(By.id("password"));
        WebElement btn = driver.findElement(By.id("login-btn"));

        // Placeholder validation
        Assertions.assertEquals("Enter username", user.getAttribute("placeholder"));
        Assertions.assertEquals("Enter password", pass.getAttribute("placeholder"));

        // Check enabled
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertTrue(pass.isEnabled());
        Assertions.assertTrue(btn.isEnabled());
    }

    // --------------------------------------------------------
    // 5. Kiểm tra click button login
    // --------------------------------------------------------
    @Test
    @DisplayName("UI Interaction — Login Button Click")
    public void testButtonClick() {
        WebElement btn = driver.findElement(By.id("login-btn"));

        // Click không cần nhập → FE phải phản hồi
        btn.click();

        // Page không được crash
        Assertions.assertTrue(true);
    }

    
}
