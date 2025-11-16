package com.bazaarstores.pages;

import com.bazaarstores.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class StoresPage extends BasePage {

    private final By storeRows = By.xpath("//table//tbody//tr");
    private final By storeNames = By.xpath("//table//tbody//tr//td[2]");

    public List<String> getStoreNames() {
        List<WebElement> storeElements = Driver.getDriver().findElements(
                By.xpath("//table//tbody//tr[td[1][normalize-space()!='']]/td[1]")
        );

        List<String> storeNames = new ArrayList<>();
        for (WebElement each : storeElements) {
            String name = each.getText().trim();
            if (!name.isEmpty()) {
                storeNames.add(name);
            }
        }

        System.out.println(" UI Store Names found: " + storeNames);
        return storeNames;
    }


    public void validateStoreDetails() {
        List<WebElement> rows = Driver.getDriver().findElements(storeRows);
        for (WebElement row : rows) {
            String id = row.findElement(By.xpath("./td[1]")).getText().trim();
            String name = row.findElement(By.xpath("./td[2]")).getText().trim();
            String location = row.findElement(By.xpath("./td[3]")).getText().trim();
            Assert.assertFalse( "Missing Store ID",id.isEmpty());
            Assert.assertFalse( "Missing Store Name",name.isEmpty());
            Assert.assertFalse( "Missing Store Location",location.isEmpty());
        }
    }
}