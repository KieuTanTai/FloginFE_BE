package org.selenium;

import java.time.Duration;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTest {

    private WebDriver driver;
    private WebDriverWait wait;

   @BeforeAll
    public void setupTest() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173");  // URL form login FE của bạn
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testEmptyUsername() {
        // Điền password, bỏ trống username
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordInput.sendKeys("123456");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn"))); // chỉnh id button
        submitButton.click();

        // Kiểm tra validation message
        WebElement usernameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-error")));
        Assertions.assertEquals("Username không được để trống", usernameError.getText());
    }

    @Test
    public void testEmptyPassword() {
        // Điền username, bỏ trống password
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameInput.sendKeys("admin");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn"))); // chỉnh id button
        submitButton.click();

        // Kiểm tra validation message
        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-error")));
        Assertions.assertEquals("Password không được để trống", passwordError.getText());
    }
    // 6.1.2 c/---------------- SUCCESS FLOW ----------------
    @Test
    @DisplayName("Login Success Flow")
    public void testLoginSuccess() throws InterruptedException {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("admin123");

        driver.findElement(By.id("login-btn")).click();

        Thread.sleep(1000);

        // Giả sử sau khi login thành công, URL đổi hoặc xuất hiện thông báo
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("dashboard")
                        || driver.getPageSource().contains("Login success"),
                "Login success flow failed!"
        );
    }

    // ---------------- ERROR FLOW ----------------
    @Test
    @DisplayName("Login Error Flow (missing fields)")
    public void testLoginError() throws InterruptedException {

        driver.navigate().refresh();

        // Không nhập gì → bấm Login
        driver.findElement(By.id("login-btn")).click();

        Thread.sleep(500);

        // FE của bạn phải hiển thị message lỗi kiểu:
        // "Username is required", "Password is required"
        boolean hasError =
                driver.getPageSource().contains("required")
                        || driver.getPageSource().toLowerCase().contains("error");

        Assertions.assertTrue(hasError, "Expected validation error message not shown!");
    }
}
