package com.excilys.cdb.validator;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Computer;


public class Validator {
	
	private static Logger logger = LoggerFactory.getLogger( Validator.class );

	
	
	public boolean validateComputer(Computer computer) {
		if(computer.getName() == null || computer.getName() == "") {
			logger.error("Computer name is null");
			return false;
		}
		if(computer.getIntroduced() == null) {
			return computer.getDiscontinued() == null;
		} else if(computer.getDiscontinued() == null) { 
			return true;
		} else {
			Date introduced;
			introduced = computer.getIntroduced();
			Date discontinued = computer.getDiscontinued();
			return discontinued.after(introduced) && introduced.before(discontinued);
		}
	}
}
