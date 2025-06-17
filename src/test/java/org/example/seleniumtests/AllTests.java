package org.example.seleniumtests;

import org.example.seleniumtests.page.CartPage;
import org.example.seleniumtests.page.CheckoutPage;
import org.example.seleniumtests.page.InventoryPAge;
import org.example.seleniumtests.page.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllTests extends BaseTest{

/*
             --------------- Тесты по Авторизации------------------
                  (вход с коректными и не коректными данными)
*/
    @Test
    public void testSuccessfulLogin(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventoryPage = new InventoryPAge(driver);
        Assert.assertTrue(inventoryPage.isInventoryVisible());
        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("inventory"));

    }

    @Test
    public  void testLoginWithWrongPassword(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "wrong_password");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Epic sadface: Username and password do not match any user in this service"));
    }

    @Test
    public void testLoginWithEmptyFields(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username is required"));
    }

    @Test
    public void testLogout(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.openMenu();
        inventory.logout();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        Assert.assertTrue(loginBtn.isDisplayed(), "User is not on login page after logout");

    }

/*
             --------------- Тесты с карточками товаров------------------
                   (добавление и удаление товаров из корзины)
*/

    @Test
    public void testAddToCart() throws InterruptedException{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.addBackpackToCart();
        inventory.goToCart();

        Thread.sleep(1000);

        CartPage cart = new CartPage(driver);
        Assert.assertTrue(cart.isItemInCart(),"Item should be present in the cart");

        cart.removeItem();
        Assert.assertFalse(cart.isItemInCart(),"Item should not be present in the cart");
    }

//    Сортирование товаров по ценам и алфавиту


    @Test
    public void testSortByPriceLowToHigh(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.selectSortOption("lohi");

        Assert.assertTrue(inventory.isSortedByPriceAscending(),"Items are not sorted by price from low to high");
    }

    @Test
    public void testSortByPriceHighToLow(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.selectSortOption("hilo");

        Assert.assertTrue(inventory.isSortedByPriceDescending(), "Items are not sorted by price from high to low");
    }

    @Test
    public void testSortByAlphabetically(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.sortByNameAZ();

        List<String> item = inventory.getItemNames();
        List<String> sorted = new ArrayList<>(item);
        Collections.sort(sorted);

        Assert.assertEquals(item,sorted,"Items are not sorted alphabetically");
    }

    /*
             --------------- Тесты по оформлению заказа------------------
                  (переход к оплате,финализация заказа и тд...)
*/

    @Test
    public void testContinueShoppingButton(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.addBackpackToCart();
        inventory.goToCart();

        CartPage cart = new CartPage(driver);
        cart.clickContinueShopping();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory"), "Should navigate back to inventory page");
    }

    @Test
    public void testCheckoutButtonDisabledOnEmptyCart(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.goToCart();

        CartPage cart = new CartPage(driver);
        Assert.assertTrue(cart.isCheckoutButtonEnabled(),"Checkout button should be enabled even on empty cart");
    }

    @Test
    public void testCartBangeAfterAdd(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.addBackpackToCart();

        String bange = inventory.getCartBangeCount();
        Assert.assertEquals(bange, "1", "Cart bange should show 1 item");
    }

    @Test
    public void testFulChekoutFlow () throws InterruptedException{
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "secret_sauce");

        InventoryPAge inventory = new InventoryPAge(driver);
        inventory.addToCart();
        inventory.goToCart();

        CartPage cart = new CartPage(driver);
        cart.goCheckout();

        Thread.sleep(1000);

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillCheckoutForm("TestName", "TestLastName","22-333");
        checkoutPage.finishCheckout();



        WebElement config = new WebDriverWait(driver,Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout_complete_container")));
        Assert.assertTrue(config.getText().contains("Thank you for your order!"));
    }
}
