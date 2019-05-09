package com.excilys.cdb.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AddComputerWebUITest {

	private WebDriver driver;
	private final String PROJECT_URL = "http://localhost:8080/computer-database/addComputer";

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

	// Title

	@Test
	public void titleTest() {
		String expectedTitle = "Computer Database";
		String actualTitle = "";
		driver.get(PROJECT_URL);
		actualTitle = driver.getTitle();
		assertEquals("Actual title isn't the expected one", expectedTitle, actualTitle);
	}
	
	// Front validation
	
	@Test
	public void frontValidationPreventComputerWithoutNameTest() {
		driver.get(PROJECT_URL);
		driver.findElement(By.cssSelector("input[type=submit]")).click();
		assertTrue("Front validation don't prevent inserting computer without name",driver.findElement(By.cssSelector("#name-group.has-error")).findElement(By.cssSelector(".alert.alert-danger")).isDisplayed());
	}
	
	@Test
	public void frontValidationPreventWrongDateFormatTest() {
		driver.get(PROJECT_URL);
		driver.findElement(By.cssSelector("#introduced-group input[type=text]")).sendKeys("01-01");;
		driver.findElement(By.cssSelector("input[type=submit]")).click();
		assertTrue("Front validation don't prevent inserting computer with wrong date dormat",driver.findElement(By.cssSelector("#introduced-group.has-error")).findElement(By.cssSelector(".alert.alert-danger")).isDisplayed());
	}
	
	@Test
	public void frontValidationPreventDiscontinuedWithoutIntroducedTest() {
		driver.get(PROJECT_URL);
		driver.findElement(By.cssSelector("#discontinued-group input[type=text]")).sendKeys("01-01-2019");;
		driver.findElement(By.cssSelector("input[type=submit]")).click();
		assertTrue("Front validation don't prevent inserting computer with wrong date dormat",driver.findElement(By.cssSelector("#discontinued-group.has-error")).findElement(By.cssSelector(".alert.alert-danger")).isDisplayed());
	}
	
	@Test
	public void frontValidationPreventDiscontinuedBeforeIntroducedTest() {
		driver.get(PROJECT_URL);
		driver.findElement(By.cssSelector("#introduced-group input[type=text]")).sendKeys("02-02-2019");;
		driver.findElement(By.cssSelector("#discontinued-group input[type=text]")).sendKeys("01-01-2019");;
		driver.findElement(By.cssSelector("input[type=submit]")).click();
		assertTrue("Front validation don't prevent inserting computer with wrong date dormat",driver.findElement(By.cssSelector("#discontinued-group.has-error")).findElement(By.cssSelector(".alert.alert-danger")).isDisplayed());
	}

}
