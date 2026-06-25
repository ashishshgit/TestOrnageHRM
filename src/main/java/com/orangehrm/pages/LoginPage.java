package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;

	// Define locators using by class

	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//Button[text()=' Login ']");
	private By errorMsg = By.xpath("//p[text()='invalid credentials']");

	/*
	 * public LoginPage(WebDriver driver)
	 * 
	 * { this.actionDriver = new ActionDriver(driver); }
	 */
	
	
	public LoginPage(WebDriver driver)
	{
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	// Method to perform login

	public void login(String UserName, String Password) {
		actionDriver.enterText(userNameField, "admin");
		actionDriver.enterText(passwordField, "admin123");
		actionDriver.click(loginButton);

	}

	// Method to check if error message is disaplayed

	public boolean ismessageDisaplyed() {
		return actionDriver.isDisplayed(errorMsg);
	}
    //Method to get the error message
	public String getErrorMessageText()
	
	{
		return actionDriver.getText(errorMsg);
	}
	
    public boolean verfiyError(String expected)
	
	{
    	return actionDriver.compareValue(errorMsg, expected);
	}
	
}
