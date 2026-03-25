package tests;


import org.testng.Assert;
import org.testng.annotations.Test;

import Base.BaseTest;
import Pages.DashboardPage;
import Pages.LoginPage;
import utils.TestDataProvider;

/**
 * LoginTest - Tests for login functionality
 * Tests: valid login, invalid login, locked user, empty fields
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");

        DashboardPage dashboardPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard should be loaded after login");
        Assert.assertEquals(dashboardPage.getPageTitle(), "Products", "Page title should be 'Products'");
        log.info("Valid login test PASSED");
    }

    @Test(priority = 2, description = "Verify login fails with incorrect password")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginExpectingFailure("standard_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username and password do not match"),
            "Error message should mention credential mismatch"
        );
        log.info("Invalid password test PASSED");
    }

    @Test(priority = 3, description = "Verify locked out user cannot login")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginExpectingFailure("locked_out_user", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error should be shown for locked user");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("locked out"),
            "Error message should mention locked out"
        );
    }

    @Test(priority = 4, description = "Verify empty username shows validation error")
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginExpectingFailure("", "secret_sauce");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Validation error should be displayed");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username is required"),
            "Error should say username is required"
        );
    }

    @Test(priority = 5, description = "Verify empty password shows validation error")
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginExpectingFailure("standard_user", "");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Validation error should be displayed");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Password is required"),
            "Error should say password is required"
        );
    }

    @Test(priority = 6, description = "Data-driven login test",
          dataProvider = "loginData", dataProviderClass = TestDataProvider.class)
    public void testLoginDataDriven(String username, String password,
                                    boolean shouldPass, String expectedText) {
        LoginPage loginPage = new LoginPage(driver);

        if (shouldPass) {
            DashboardPage dashboardPage = loginPage.login(username, password);
            Assert.assertEquals(dashboardPage.getPageTitle(), expectedText,
                "Dashboard title mismatch for user: " + username);
        } else {
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.getErrorMessage().contains(expectedText),
                "Error message mismatch for user: " + username);
        }
    }

    
    @Test(priority = 7, description = "Verify logout functionality")
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Should be on dashboard");

        LoginPage loginPageAfterLogout = dashboardPage.logout();
        Assert.assertTrue(loginPageAfterLogout.isLoginPageDisplayed(),
            "Should return to login page after logout");
    }
}
