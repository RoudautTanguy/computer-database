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
		validator.validateComputer(new Computer(1, "Good Computer", introduced, discontinued, 1));
	}

	@Test
	public void validateGoodComputerWithoutDiscontinuedTest() throws NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer(1, "Good Computer", introduced, null, 1));
	}

	@Test
	public void validateGoodComputerWithoutDatesTest() throws NotAValidComputerException {
		validator.validateComputer(new Computer(1, "Good Computer", null, null, 1));
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateWrongComputerTest() throws NotAValidComputerException {
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		validator.validateComputer(new Computer(1, "Wrong Computer", null, discontinued, 1));
	}

	@Test(expected = NotAValidComputerException.class)
	public void dontValidateComputerWithoutNameTest() throws NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		validator.validateComputer(new Computer(1, null, introduced, null, 1));
	}

}
