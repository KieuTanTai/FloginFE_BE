package org.example.tests;
// file này setup POM 
import org.example.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

@Disabled("UI tests disabled in CI")
public class LoginUITest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:5173"); // URL frontend của bạn
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSuccessfulLogin() {
        loginPage.enterUsername("admin");
        loginPage.enterPassword("admin123");
        loginPage.clickLogin();

        // Kiểm tra page title sau login, ví dụ Home page
        assertEquals("Home", loginPage.getPageTitle());
    }
}
