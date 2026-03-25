package utils;

import org.testng.annotations.DataProvider;

/**
 * TestDataProvider - Centralized TestNG DataProviders
 */
public class TestDataProvider {

    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        return new Object[][] {
            {"standard_user",    "secret_sauce", true,  "Products"},
            {"locked_out_user",  "secret_sauce", false, "Epic sadface: Sorry, this user has been locked out."},
            {"problem_user",     "secret_sauce", true,  "Products"},
            {"standard_user",    "wrongpass",    false, "Epic sadface: Username and password do not match any user in this service"},
            {"",                 "secret_sauce", false, "Epic sadface: Username is required"},
        };
    }

    @DataProvider(name = "checkoutData")
    public static Object[][] checkoutData() {
        return new Object[][] {
            {"John",  "Doe",    "12345"},
            {"Jane",  "Smith",  "67890"},
            {"Alice", "Wonder", "54321"},
        };
    }

    @DataProvider(name = "sortOptions")
    public static Object[][] sortOptions() {
        return new Object[][] {
            {"Name (A to Z)"},
            {"Name (Z to A)"},
            {"Price (low to high)"},
            {"Price (high to low)"},
        };
    }
}
