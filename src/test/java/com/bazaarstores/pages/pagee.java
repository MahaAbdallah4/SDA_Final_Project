package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.WebDriver;

public class pagee {
    public static void main(String[] args) {
        WebDriver driver = Driver.getDriver();
        driver.get("https://bazaarstores.com");
        System.out.println(driver.getTitle());
        Driver.quitDriver();
    }

}
