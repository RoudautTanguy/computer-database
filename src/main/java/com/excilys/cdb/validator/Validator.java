package com.excilys.cdb.validator;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.model.Computer;


public class Validator {

	private static Logger logger = LoggerFactory.getLogger( Validator.class );

	private static Validator instance;

	public static Validator getInstance() {
		if(instance == null) {
			instance = new Validator();
		}
		return instance;
	}

	public void validateComputer(Computer computer) throws NotAValidComputerException{
		validateNameIsPresent(computer.getName());
		validateDiscontinuedDateIsNullIfIntroducedIsNull(computer.getIntroduced(), computer.getDiscontinued());
		validateDiscontinuedDateIsAfterIntroducedDate(computer.getIntroduced(),computer.getDiscontinued());
	}

	private void validateNameIsPresent(String name) throws NotAValidComputerException {
		if(name == null || name.equals("")) {
			String message = "Computer name is null";
			logger.error(message);
			throw new NotAValidComputerException(message);
		}
	}

	private void validateDiscontinuedDateIsNullIfIntroducedIsNull(Timestamp introduced, Timestamp discontinued) throws NotAValidComputerException {
		if(introduced == null && discontinued != null) {
			throw new NotAValidComputerException("Computer is not introduced but have a Discontinued date");
		}
	}
	
	private void validateDiscontinuedDateIsAfterIntroducedDate(Date introduced, Date discontinued) throws NotAValidComputerException {
		if(introduced != null && discontinued != null && introduced.after(discontinued) && discontinued.before(introduced)) {
			throw new NotAValidComputerException("Discontinued date is before Introduced date");
		}
	}
}
