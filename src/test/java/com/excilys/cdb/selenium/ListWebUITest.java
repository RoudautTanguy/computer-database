package com.excilys.cdb.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ListWebUITest {

	private WebDriver driver;
	private final String PROJECT_URL = "http://localhost:8080/computer-database/";

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setupTest() {
		driver = new ChromeDriver();
	}

	@After
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	//Title
	
	@Test
	public void titleTest() {
		String expectedTitle = "Computer Database";
		String actualTitle = "";
		driver.get(PROJECT_URL);
		actualTitle = driver.getTitle();
		assertEquals("Actual title isn't the expected one", expectedTitle, actualTitle);
	}

	//Number computer 
	
	@Test
	public void numberComputerTest() {
		driver.get(PROJECT_URL);
		String number = driver.findElement(By.cssSelector("b.number")).getText();
		assertTrue("Display number of computer is 0 and shouldn't be", Integer.parseInt(number)>0);
	}

	@Test
	public void numberComputerTestWithSearch() {
		driver.get(PROJECT_URL+"?search=Apple");
		String number = driver.findElement(By.cssSelector("b.number")).getText();
		assertTrue("Display number of computer is 0 with Apple search and shouldn't be", Integer.parseInt(number)>0);
	}

	@Test(expected=NoSuchElementException.class)
	public void numberComputerDontExistOnWrongSearch() {
		driver.get(PROJECT_URL+"?search=zzz");
		driver.findElement(By.cssSelector("b.number"));
	}
	
	//Pagination
	
	@Test
	public void paginationTest() {
		driver.get(PROJECT_URL);
		WebElement pagination = driver.findElement(By.id("pagination"));
		List<String> realValues = pagination.findElements(By.tagName("li")).stream().map(WebElement::getText).collect(Collectors.toList());
		List<String> expectedValues = Arrays.asList("«","‹","1","2","3","4","5","›","»");
		assertEquals("Pagination is not as expected", expectedValues,realValues);  
	}
	
	@Test
	public void paginationFirstAndPreviousDisabledTest() {
		driver.get(PROJECT_URL);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		assertTrue("First is not disabled",driver.findElement(By.cssSelector("#First.disabled")).isDisplayed());
		assertTrue("Previous is not disabled",driver.findElement(By.cssSelector("#Previous.disabled")).isDisplayed());
	}
	
	@Test
	public void paginationFirstPageIsActiveTest() {
		driver.get(PROJECT_URL);
		assertEquals("First page is not the active page","1",driver.findElement(By.cssSelector("#pagination .active")).getText());
	}
	
	@Test
	public void paginationWithAnotherPageTest() {
		driver.get(PROJECT_URL+"?page=10&size=10");
		WebElement pagination = driver.findElement(By.id("pagination"));
		List<String> realValues = pagination.findElements(By.tagName("li")).stream().map(WebElement::getText).collect(Collectors.toList());
		List<String> expectedValues = Arrays.asList("«","‹","8","9","10","11","12","›","»");
		assertEquals("Pagination is not as expected", expectedValues,realValues);  
	}
	
	@Test
	public void paginationCurrentPageIsActiveTest() {
		driver.get(PROJECT_URL+"?page=10&size=10");
		assertEquals("Current page is not the active page","10",driver.findElement(By.cssSelector("#pagination .active")).getText());
	}
	
	// List
	
	@Test
	public void correctNumberInListTest() {
		driver.get(PROJECT_URL+"?size=10");
		WebElement pagination = driver.findElement(By.id("results"));
		assertEquals("Number of element in list isn't correct.",10,pagination.findElements(By.tagName("tr")).size());
	}
	
	@Test
	public void correctNumberInListSize50Test() {
		driver.get(PROJECT_URL+"?size=50");
		WebElement pagination = driver.findElement(By.id("results"));
		assertEquals("Number of element in list isn't correct.",50,pagination.findElements(By.tagName("tr")).size());
	}
	
	@Test
	public void correctNumberInListSize100Test() {
		driver.get(PROJECT_URL+"?size=100");
		WebElement pagination = driver.findElement(By.id("results"));
		assertEquals("Number of element in list isn't correct.",100,pagination.findElements(By.tagName("tr")).size());
	}
}
