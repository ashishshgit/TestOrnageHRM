package com.demoproject.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPage() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verfiyVaildLoginPage(String username, String password)

	{
		ExtentManager.startTest("Valid Login Test"); // --This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");

		loginpage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin tab should be visable afterlogin");
		ExtentManager.logStep("Validation Successful");
		homepage.logout();
		ExtentManager.logStep("Logged out Successfully!");
		staticWait(2);
	}

	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void verfiyInVaildLoginPage(String username, String password)

	{
		ExtentManager.startTest("In-valid Login Test!"); //This has been implemented in TestListener
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		
		loginpage.login(username, password);
		String expected = "Invalid credentials1";
		Assert.assertTrue(loginpage.verfiyError(expected), "Test Failed - Invaild error message");
		ExtentManager.logStep("Validation Successful");
	}
}
