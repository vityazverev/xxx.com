package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestingTaskUI extends BaseTest {
	private static final String TOMORROW = "Tomorrow";
	private static final String NEXT_WEEK = "Next Week";
	private final String TODAY = "Today";
	private final String XM_LOGO_LINK = "https://cloud.xm-cdn.com/static/xm/common/logos/XM_logo_black_2021.svg";
	private final String PRIVACY_MESSAGE = "We respect your privacy";
	private final String ECONOMIC_CALENDAR = "Economic Calendar";
	private LocalDate todayDate;
	private By modalDialogMessage = By.cssSelector(".cookie-modal__defaultBlock h4");
	private By xmLogoLink = By.cssSelector(".modal-dialog img");
	private By modalDialogWindow = By.cssSelector(".cookie-modal__defaultBlock");
	private By buttonAcceptAll = By.cssSelector(".modal-dialog [aria-label='Close']");
	private By openAccountButton = By.cssSelector("a[href='https://www.xm.com/goto/profile/en']");
	private By menuButtonMobile = By.cssSelector("button.toggleLeftNav");
	private By researchEducationMobile = By.cssSelector("#main-nav > li > [aria-controls='researchMenu']");
	private By economicCalendarMobile = By.cssSelector("li > a[href='https://www.xm.com/research/economicCalendar'] > span");
	private By economicCalendarHeaderMobile = By.cssSelector("li > a[href='/research/economicCalendar']");
	private By calendarIconMobile = By.xpath("//span[@aria-label='Show time filter']");
	private By researchEducation = By.cssSelector("li.main_nav_research");
	private By economicCalendar = By.cssSelector("a[href='https://www.xm.com/research/economicCalendar']");
	private By economicCalendarHeader = By.tagName("h2");
	private By sliderWeb = By.xpath("//mat-slider[@role='slider']");
	private By calendarEvent = By.xpath("//tc-economic-calendar-row[@class='ng-star-inserted']");
	private By dayWeekName = By.cssSelector(".tc-finalval-tmz");
	private By todayTomorrowDate = By.cssSelector(".mat-calendar-body-active");
	private By firstDayOfWeek = By.cssSelector(".mat-calendar-body-range-start");
	private By lastDayOfWeek = By.cssSelector(".mat-calendar-body-range-end");
	private By iframeModule = By.xpath("//iframe[@title='iframe']");

	@BeforeEach
	public void setup() {
		startChromeDriver();
	}

	/**
	 * Parameterized Test.
	 * Sets the browser window resolution.
	 *
	 * @param width  the width of the browser window.
	 * @param height the height of the browser window.
	 */
	@ParameterizedTest
	@CsvSource({
			"0, 0",
			"1024, 768",
			"800, 600"
	})
	public void economicCalendarTest(int width, int height) {
		setResolution(width, height);

//		Initializes dates for testing, including today, tomorrow, and the start and end
//		of the next week.
		todayDate = LocalDate.now();
		LocalDate tomorrowDate = todayDate.plusDays(1);
		LocalDate firstDayOfNextWeek = todayDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		LocalDate lastDayOfNextWeek = firstDayOfNextWeek.plusDays(6);

//		Opens the home page and verifying the privacy policy and accept them.
		driver.get("https://www.xm.com");
		waitElementDisplayed(modalDialogMessage);
		assertEquals(PRIVACY_MESSAGE, getText(modalDialogMessage));
		assertEquals(XM_LOGO_LINK, driver.findElement(xmLogoLink).getAttribute("src"));
		clickElement(buttonAcceptAll);

// 		Specifically checks for the presence of the 'Open an Account' button.
		assertEquals(true, existsElement(openAccountButton));

//		Go to Research & Education section
		navigateToResearchEducationSection();

//		Select Calendar Events Section
		waitElementDisplayed(calendarEvent);
		moveSliderRight(sliderWeb);
		waitElementDisplayed(calendarEvent);
		assertEquals(TODAY, getText(dayWeekName));
		//		get today date
		assertEquals(todayDate.getDayOfMonth(), Integer.parseInt(getText(todayTomorrowDate)));
		//		move slider to Tomorrow
		moveSliderRight(sliderWeb);
		waitElementDisplayed(calendarEvent);
		//		waitElement(driver, dayWeekName);
		assertEquals(TOMORROW, getText(dayWeekName));
		assertEquals(tomorrowDate.getDayOfMonth(), Integer.parseInt(getText(todayTomorrowDate)));
		//		waitElement(driver, sliderWeb);
		moveSliderRight(sliderWeb);
		waitElementDisplayed(calendarEvent);
		moveSliderRight(sliderWeb);
		waitElementDisplayed(calendarEvent);
		assertEquals(NEXT_WEEK, getText(dayWeekName));
		assertEquals(firstDayOfNextWeek.getDayOfMonth(), Integer.parseInt(getText(firstDayOfWeek)));
		assertEquals(lastDayOfNextWeek.getDayOfMonth(), Integer.parseInt(getText(lastDayOfWeek)));
	}

	private void clickElement(By locator) {
		waitElementDisplayed(locator);
		driver.findElement(locator).click();
	}

	private void switchToFrame(By frameTitle) {
		WebElement frame = driver.findElement(frameTitle);
		driver.switchTo().frame(frame);
	}

	private boolean existsElement(By selector) {
		try {
			driver.findElement(selector);
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	/**
	 * Navigates to the Research and Education section of the website.
	 * Determines whether to use the mobile or desktop method for navigation
	 * based on the current screen size.
	 */
	public void navigateToResearchEducationSection() {
		Dimension screenSize = driver.manage().window().getSize();
		if (isMobileView(screenSize)) {
			navigateToEconomicCalendarMobile();
		} else {
			navigateToEconomicCalendarDesktop();
		}
	}

	/**
	 * Checks if the current view is considered as mobile based on the screen size.
	 *
	 * @param screenSize the current size of the browser window.
	 * @return true if the screen size corresponds to mobile dimensions, false otherwise.
	 */
	private boolean isMobileView(Dimension screenSize) {
		return screenSize.getWidth() < 1024 && screenSize.getHeight() < 768;
	}

	private void navigateToEconomicCalendarMobile() {
		clickElement(menuButtonMobile);
		clickElement(researchEducationMobile);
//		scrollDownToElement(researchEducationMobile);
		//Go to economicCalendar
		clickElement(economicCalendarMobile);
//		//verifying that economicCalendar page
		assertEquals(ECONOMIC_CALENDAR, getText(economicCalendarHeaderMobile));
		switchToFrame(iframeModule);
		scrollDownToElement(calendarIconMobile);
		clickElement(calendarIconMobile);
	}

	private void navigateToEconomicCalendarDesktop() {
		clickElement(researchEducation);
		clickElement(economicCalendar);
		// Verify that the economic calendar page is open
		assertEquals(ECONOMIC_CALENDAR, getText(economicCalendarHeader));
		switchToFrame(iframeModule);
	}
}
