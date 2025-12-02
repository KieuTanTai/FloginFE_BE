package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;

    // A few flexible selectors for a welcome/title element
    private final By welcomeSelector = By.cssSelector(".welcome, #welcome, h1");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getWelcomeText() {
        try {
            return driver.findElement(welcomeSelector).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
