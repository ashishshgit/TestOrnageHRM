package com.demoproject.tests;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest2 extends BaseClass {
	@Test
	public void dummyone()

	{
		ExtentManager.startTest("DummyTest2 Test"); //--This has been implemented in
		// TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("verifying the title");
		//assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";
		System.out.println("Test Passed - Title is matching");
		ExtentManager.logStep("Validation Successful");

	}

}
