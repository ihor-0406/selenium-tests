package org.example.seleniumtests.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class CartPage {

    private WebDriver driver;

    private final By cartItem = By.className("inventory_item_name");
    private final By removeButton = By.id("remove-sauce-labs-backpack");
    private final By continueButton = By.name("continue-shopping");
    private final By checkoutButton = By.id("checkout");


    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isItemInCart(){
        return driver.findElements(cartItem).size() > 0;

    }

    public void removeItem() {
        driver.findElement(removeButton).click();
    }


    public void clickContinueShopping(){
        driver.findElement(continueButton).click();
    }

    public int getCartItemCount(){
        return driver.findElements(cartItem).size();
    }

    public boolean isCheckoutButtonEnabled(){
        WebElement button = driver.findElement(By.className("shopping_cart_link"));
        return button.isEnabled();
    }

    public void goCheckout(){
        driver.findElement(checkoutButton).click();
    }
}
