package org.selenium;

import java.time.Duration;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Validation E2E Tests")
public class ValidationE2ETest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    public void setupTest() {
        baseUrl = System.getProperty("ui.baseUrl", "http://localhost:5173");
        WebDriverManager.chromedriver().setup();
        boolean headless = Boolean.parseBoolean(System.getProperty("ui.headless", "false"));
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(baseUrl);
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Empty username shows validation")
    public void testEmptyUsername() {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordInput.sendKeys("123456");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        submitButton.click();

        WebElement usernameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username-error")));
        Assertions.assertEquals("Username không được để trống", usernameError.getText());
    }

    @Test
    @DisplayName("Empty password shows validation")
    public void testEmptyPassword() {
        driver.navigate().refresh();

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameInput.sendKeys("admin");

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        submitButton.click();

        WebElement passwordError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-error")));
        Assertions.assertEquals("Password không được để trống", passwordError.getText());
    }
}
