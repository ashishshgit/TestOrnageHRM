package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		System.out.println("Setting Up the Webdriver for:" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("Webdriver initizlized and browser maximize");
		logger.trace("Trace Message");
		logger.error("Error Message");
		logger.debug("Debug Message");
		logger.fatal("Fetal Message");
		logger.warn("warn Message");

		// initialize the actionDriver only once
		/*
		 * if (actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * System.out.println("ActionDriver instance is created"+Thread.currentThread().
		 * getId());
		 * 
		 * }
		 */

		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initliazed for thread:" + Thread.currentThread().getId());

	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/Config.properties");

		prop.load(fis);
		logger.info("Property File Loaded");

		// Start the Extent Report
		// ExtentManager.getReporter(); //--This has been implemented in TestListener
	}

	private synchronized void launchBrowser() {
		// Initialize the WebDriver based on browser defined in config.properties

		String browser = System.getProperty("BROWSER","chrome");
		
		System.out.println(">>> BROWSER property: [" + System.getProperty("BROWSER") + "]");


		if (browser.equalsIgnoreCase("chrome")) {

			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless"); // Run Chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
			// options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

			// driver = new ChromeDriver();
			driver.set(new ChromeDriver(options));// new chnages as per thread
			logger.info("Chrome driver instance ids created");
		} else if (browser.equalsIgnoreCase("firefox")) {

			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

			driver.set(new FirefoxDriver(options));
			logger.info("Firefox driver instance ids created");

		} else if (browser.equalsIgnoreCase("edge")) {

			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			driver.set(new EdgeDriver(options));
			logger.info("Edge driver instance ids created");

		} else {
			throw new IllegalArgumentException("Browser Not Supported :" + browser);

		}
	}

	private void configureBrowser() {
		// Implicit wait
		int implicatiWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicatiWait));

		// Maximize the browser window
		driver.get().manage().window().maximize();

		// Navigate to URL

		try {
			driver.get().get(prop.getProperty("url_base"));
		} catch (Exception e) {

			System.out.println("Failed to Navigate to the url" + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to Quit the browser:" + e.getMessage());
			}
		}
		logger.info("WebDriver Instance is closed.");
		driver.remove();
		actionDriver.remove();
		// driver = null;
		// actionDriver = null;
	}

	// Get the prop value
	public static Properties getProp() {
		return prop;
	}

	/*
	 * //Driver getter method public static WebDriver getDriver() { return driver; }
	 */

	public static WebDriver getDriver()

	{
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}

	public static ActionDriver getActionDriver()

	{
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}

	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static method for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	@AfterSuite
	public void tearDownSuite() {
		ExtentManager.flushReport(); // flush once after all tests
	}
}
