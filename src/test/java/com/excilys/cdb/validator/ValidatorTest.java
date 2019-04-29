package com.excilys.cdb.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;

import com.excilys.cdb.model.Computer;

public class ValidatorTest {
	
	Validator validator = new Validator();
	
	@Test
	public void validateGoodComputerTest() {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		assertTrue(validator.validateComputer(new Computer(1, "Good Computer", introduced, discontinued, 1)));
	}
	
	@Test
	public void validateGoodComputerWithoutDiscontinuedTest() {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		assertTrue(validator.validateComputer(new Computer(1, "Good Computer", introduced, null, 1)));
	}
	
	@Test
	public void validateGoodComputerWithoutDatesTest() {
		assertTrue(validator.validateComputer(new Computer(1, "Good Computer", null, null, 1)));
	}
	
	@Test
	public void dontValidateWrongComputerTest() {
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		assertFalse(validator.validateComputer(new Computer(1, "Good Computer", null, discontinued, 1)));
	}
	
	@Test
	public void dontValidateComputerWithoutNameTest() {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		assertFalse(validator.validateComputer(new Computer(1, null, introduced, null, 1)));
	}

}
