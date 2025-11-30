
package org.selenium;

import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
// có hiện lên ui , xong tự động điền để qua login thành công , nhưng lại không hiện lên thông báo thành công khi check có component ở login :((((
public class LoginTest {
    public static void main(String[] args) {

        // 1. Tự động download và setup ChromeDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://localhost:5173");
            System.out.println("Trang login đã mở.");

            // 2. Nhập username và password
            driver.findElement(By.id("username")).sendKeys("admin");
            driver.findElement(By.id("password")).sendKeys("admin123");
            System.out.println("Đã nhập username và password.");

            // 3. Bấm nút login
            driver.findElement(By.id("login-btn")).click();
            System.out.println("Đã bấm nút login.");

            // 4. Chờ dashboard load và xuất hiện image
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement image = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("image")));

            if (image.isDisplayed()) {
                System.out.println("Login successful! Dashboard image xuất hiện.");
            } else {
                System.out.println("Login failed: image không xuất hiện.");
            }

            // 5. Giữ browser mở để bạn kiểm tra, chờ Enter
            System.out.println("Nhấn Enter để đóng browser...");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("Browser đã đóng.");
        }
    }
}
