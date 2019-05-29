package com.excilys.cdb.validator;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
public class ValidatorTest {

	@Autowired
	Validator validator;
	
	private static final String GOOD_COMPUTER = "Good Computer";
	private Company APPLE_COMPANY = new Company(1,"Apple Inc.");

	@Test
	public void validateGoodComputerTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer(1,GOOD_COMPUTER,introduced,discontinued,APPLE_COMPANY));
	}

	@Test
	public void validateGoodComputerWithoutDiscontinuedTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer(1,GOOD_COMPUTER,introduced,null,APPLE_COMPANY));
	}

	@Test
	public void validateGoodComputerWithoutDatesTest() throws NotAValidComputerException, CompanyNotFoundException {
		validator.validateComputer(new Computer(1,GOOD_COMPUTER,null,null,APPLE_COMPANY));
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateWrongComputerTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer(1,"Wrong Computer",null,discontinued,APPLE_COMPANY));
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateComputerWithoutNameTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer(1,null,introduced,null,APPLE_COMPANY));
	}

}
