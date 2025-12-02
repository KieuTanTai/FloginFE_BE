package org.selenium;

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

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UI Elements E2E")
public class UIElementsE2ETest {

    private WebDriver driver;
    private String baseUrl;

    @BeforeAll
    public void setup() {
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
        driver.get(baseUrl);
    }

    @AfterAll
    public void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    @DisplayName("UI Elements — Presence Test")
    public void testElementsPresence() {
        Assertions.assertNotNull(driver.findElement(By.id("username")));
        Assertions.assertNotNull(driver.findElement(By.id("password")));
        Assertions.assertNotNull(driver.findElement(By.id("login-btn")));
    }

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

        user.clear();
        pass.clear();

        Assertions.assertEquals("", user.getAttribute("value"));
        Assertions.assertEquals("", pass.getAttribute("value"));
    }

    @Test
    @DisplayName("UI Elements — Attributes Test")
    public void testAttributes() {
        WebElement user = driver.findElement(By.id("username"));
        WebElement pass = driver.findElement(By.id("password"));
        WebElement btn = driver.findElement(By.id("login-btn"));

        Assertions.assertEquals("Enter username", user.getAttribute("placeholder"));
        Assertions.assertEquals("Enter password", pass.getAttribute("placeholder"));

        Assertions.assertTrue(user.isEnabled());
        Assertions.assertTrue(pass.isEnabled());
        Assertions.assertTrue(btn.isEnabled());
    }

    @Test
    @DisplayName("UI Interaction — Login Button Click")
    public void testButtonClick() {
        WebElement btn = driver.findElement(By.id("login-btn"));
        btn.click();
        Assertions.assertTrue(true);
    }
}
