package org.example.seleniumtests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class InventoryPAge {
    private final WebDriver driver;
    private final By inventoryContainer = By.id("inventory_container");
    private final By inventoryItems = By.className("inventory_items");
    private final By itemPrices =By.className("inventory_item_price");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    private final By addBackpackToCart = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartBange = By.className("shopping_cart_badge");
    private final By openMenu = By.id("react-burger-menu-btn");
    private final By logout = By.id("logout_sidebar_link");

    public InventoryPAge(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isInventoryVisible(){
        return driver.findElement(inventoryContainer).isDisplayed();
    }

    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    public void addBackpackToCart(){
//        driver.findElement(addBackpackToCart).click();
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(addBackpackToCart))
                .click();
    }

    public void goToCart(){
        driver.findElement(cartIcon).click();
    }

    public int getInventoryItemCount(){
        return driver.findElements(inventoryItems).size();
    }

    public void selectSortOption(String value){
        Select dropdown = new Select(driver.findElement(sortDropdown));
        dropdown.selectByValue(value);
    }

    public List<Double> getItemPrices(){
        return driver.findElements(itemPrices).stream()
                .map(WebElement::getText)
                .map(text -> text.replace("$", ""))
                .map(Double :: parseDouble)
                .collect(Collectors.toList());
    }

//    ------------------------------------------------------------

    public boolean isSortedByPriceAscending(){
        List<Double> prices = getItemPrices();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);
        return prices.equals(sorted);
    }

    public  boolean isSortedByPriceDescending(){
        List<Double> prices = getItemPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Collections.reverseOrder());
        return prices.equals(sorted);
    }

    public  List<String> getItemNames(){
        List<WebElement> items = driver.findElements(By.className("inventory_item_name"));
        return  items.stream()
                .map(WebElement :: getText)
                .collect(Collectors.toList());
    }
    public void sortByNameAZ(){
        Select dropdown = new Select(driver.findElement(sortDropdown));
        dropdown.selectByVisibleText("Name (A to Z)");
    }

    public String getCartBangeCount(){
        try {
            return driver.findElement(cartBange).getText();
        }catch (NoSuchElementException e){
            return "0";
        }
    }

    public void openMenu(){
        driver.findElement(openMenu).click();
    }

    public void logout(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(logout)).click();
    }

    public void addToCart() {
        By addToCartBtn = By.id("add-to-cart-sauce-labs-backpack");
        driver.findElement(addToCartBtn).click();
    }


}
