package com.excilys.cdb.validator;

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
		if(computer.getName() == null || computer.getName() == "") {
			String message = "Computer name is null";
			logger.error(message);
			throw new NotAValidComputerException(message);
		}
		if(computer.getIntroduced() == null && computer.getDiscontinued() != null) {
			throw new NotAValidComputerException("Computer is not introduced but have a Discontinued date");
		} else {
			Date introduced;
			introduced = computer.getIntroduced();
			Date discontinued = computer.getDiscontinued();
			if(discontinued.after(introduced) && introduced.before(discontinued)) {
				throw new NotAValidComputerException("Discontinued date is before Introduced date");
			};
		}
	}
}
