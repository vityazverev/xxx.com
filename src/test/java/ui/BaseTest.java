package ui;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {
	protected WebDriver driver;
	protected Wait<WebDriver> wait;

	protected ChromeDriver startChromeDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new");
		options.setImplicitWaitTimeout(Duration.ofSeconds(10));
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		return (ChromeDriver) driver;
	}

	public void waitElement(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitElement(By locator, int seconds) {
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	@AfterEach
	public void quit() {
		if (driver != null) {
			driver.quit();
		}
	}

	protected void setResolution(int width, int height) {
		if (width == 0 & height == 0) driver.manage().window().maximize();
		else driver.manage().window().setSize(new Dimension(width, height));
	}

	protected void waitElementDisplayed(By modalDialogMessageBy) {
		WebElement element = driver.findElement(modalDialogMessageBy);
		wait.until(d -> element.isDisplayed());
	}

	protected void scrollDownToElement(By by) {
		WebElement element = driver.findElement(by);
		WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(element);
		new Actions(driver)
				.scrollToElement(element)
				.scrollFromOrigin(scrollOrigin, 0, driver.manage().window().getSize().getHeight() / 2)
				.perform();
	}

	protected void moveSliderRight(By sliderWebBy) {
		scrollDownToElement(sliderWebBy);
		driver.findElement(sliderWebBy).sendKeys(Keys.ARROW_RIGHT);
	}

	protected String getText(By selector) {
		return driver.findElement(selector).getText();
	}
}