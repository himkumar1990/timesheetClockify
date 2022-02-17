package dev.timesheet;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {

	WebDriver driver;
	WebDriverWait wait;
	private By email = By.id("email");
	private By password = By.id("password");
	private By login = By.xpath("//button[@type='submit']");
	private By projectButton = By.xpath("//span[text()='Project']");
	private By createNewProject = By.xpath("//span[text()=' Create new project ']");
	private By projectName = By.xpath("//input[@data-cy='project-name']");
	private By createProjectButton = By.xpath("//button[@data-cy='create-project']");
	private By acceptCookies = By.id("accept-cookies");
	private By createProjectDialog = By.xpath("//h1[text()=' Create new project ']");
	private By startTimer = By.xpath("//button[text()='Start' and contains(@class ,'cl-btn-sm')]");
	private By stopTimer = By.xpath("//button[text()='Stop' and contains(@class ,'cl-btn-sm')]");
	private By timePicker = By.cssSelector("input.cl-input-time-picker");
	private By timeSum = By.cssSelector("input.cl-input-time-picker-sum");
	private By projectNameOnTimerRecord = By.cssSelector("a.cl-project-name");
	private By toastMessage = By.cssSelector("*.toast-title");

	@BeforeTest
	public void initializeWebdriver() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\driver\\chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	public void login(String username, String password) {
		driver.findElement(email).sendKeys(username);
		driver.findElement(this.password).sendKeys(password);
		driver.findElement(login).submit();
	}

	public void createProject(String projectName) {
		click(projectButton);
		click(createNewProject);
		driver.findElement(this.projectName).sendKeys(projectName);
		click(createProjectButton);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(createProjectDialog));
	}

	public void createTimeTracker(String startTime, String endTime) {
		click(startTimer);
		wait.until(ExpectedConditions.visibilityOfElementLocated(stopTimer));
		click(stopTimer);
		wait.until(ExpectedConditions.visibilityOfElementLocated(startTimer));
		wait.until(ExpectedConditions.visibilityOfElementLocated(projectNameOnTimerRecord));

		List<WebElement> timepickerElements = driver.findElements(timePicker);
		enterValue(timepickerElements.get(0), startTime);
		enterValue(timepickerElements.get(1), endTime);
		driver.findElement(timeSum).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(toastMessage));
	}

	@Test
	public void createTimesheet() {
		driver.get("https://app.clockify.me/login");
		driver.findElement(acceptCookies).click();
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(acceptCookies)));
		login("himanshu.kumar3112@gmail.com", "Welcome@123");
		createProject("aut1");
		createTimeTracker("1100", "0000");
		Assert.assertEquals(driver.findElement(timeSum).getAttribute("value"), "13:00:00");
	}

	public void click(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator));
		driver.findElement(locator).click();
	}

	public void enterValue(WebElement e, String value) {
		e.sendKeys(Keys.CONTROL, "a", Keys.DELETE);
		e.click();
		e.sendKeys(value);
		wait.until(ExpectedConditions.attributeToBe(e, "value", value));
		e.sendKeys(Keys.TAB);
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}