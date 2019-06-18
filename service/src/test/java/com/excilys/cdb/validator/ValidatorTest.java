package com.excilys.cdb.validator;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.SpringServiceTestConfiguration;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ValidatorTest {

	private static Validator validator;
	
	private static final String GOOD_COMPUTER = "Good Computer";
	private static final Company APPLE_COMPANY = new Company(1,"Apple Inc.",0);
	
	@BeforeClass
	public static void setUpBeforeClass() {
		@SuppressWarnings("resource")
		ApplicationContext vApplicationContext = new AnnotationConfigApplicationContext(SpringServiceTestConfiguration.class);
		validator = vApplicationContext.getBean(Validator.class);
	}

	@Test
	public void validateGoodComputerTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer(1,GOOD_COMPUTER,introduced,discontinued,APPLE_COMPANY,0));
	}

	@Test
	public void validateGoodComputerWithoutDiscontinuedTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer(1,GOOD_COMPUTER,introduced,null,APPLE_COMPANY,0));
	}

	@Test
	public void validateGoodComputerWithoutDatesTest() throws NotAValidComputerException, CompanyNotFoundException {
		validator.validateComputer(new Computer(1,GOOD_COMPUTER,null,null,APPLE_COMPANY,0));
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateWrongComputerTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer(1,"Wrong Computer",null,discontinued,APPLE_COMPANY,0));
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateComputerWithoutNameTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer(1,null,introduced,null,APPLE_COMPANY,0));
	}

}
