package com.excilys.cdb.validator;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;

import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.model.Computer;

public class ValidatorTest {

	Validator validator = Validator.getInstance();

	@Test
	public void validateGoodComputerTest() throws NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer.ComputerBuilder("Good Computer")
											   .withId(1)
											   .withIntroduced(introduced)
											   .withDiscontinued(discontinued)
											   .withCompanyId(1)
											   .build());
	}

	@Test
	public void validateGoodComputerWithoutDiscontinuedTest() throws NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer.ComputerBuilder("Good Computer")
				   .withId(1)
				   .withIntroduced(introduced)
				   .withCompanyId(1)
				   .build());
	}

	@Test
	public void validateGoodComputerWithoutDatesTest() throws NotAValidComputerException {
		validator.validateComputer(new Computer.ComputerBuilder("Good Computer")
				   .withId(1)
				   .withCompanyId(1)
				   .build());
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateWrongComputerTest() throws NotAValidComputerException {
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer.ComputerBuilder("Wrong Computer")
				   .withId(1)
				   .withDiscontinued(discontinued)
				   .withCompanyId(1)
				   .build());
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateComputerWithoutNameTest() throws NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer.ComputerBuilder(null)
				   .withId(1)
				   .withIntroduced(introduced)
				   .withCompanyId(1)
				   .build());
	}

}
