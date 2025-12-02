package org.example.tests;

import org.example.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("UI tests disabled in CI")
public class LoginFlowTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeAll
    public void setupDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // chạy nền
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:5173"); // URL frontend của bạn
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void testSuccessfulLogin() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("admin123");
        loginPage.clickLogin();

        // Chờ vài giây nếu cần hoặc assert dashboard element
        // Ví dụ: assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    public void testFailedLogin() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("wrongpassword");
        loginPage.clickLogin();

        String error = loginPage.getErrorMessage();
        assertNotNull(error);
        assertTrue(error.contains("Đăng nhập thất bại") || error.contains("Vui lòng nhập"));
    }
}
