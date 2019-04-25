package com.excilys.cdb.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.excilys.cdb.mapper.DTOComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Validator {
	
	private static Logger logger = LoggerFactory.getLogger( Validator.class );

	
	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public boolean validateDTOComputer(DTOComputer computer) {
		if(computer.getName() == null || computer.getName() == "NULL") {
			logger.error("Computer name is null");
			return false;
		}
		if(computer.getIntroduced() == null || computer.getIntroduced() == "NULL") {
			return computer.getDiscontinued() == null || computer.getDiscontinued() == "NULL";
		} else if(computer.getDiscontinued() == null || computer.getDiscontinued() == "NULL") { 
			return true;
		} else {
			Date introduced;
			try {
				introduced = simpleDateFormat.parse(computer.getIntroduced());
				Date discontinued = simpleDateFormat.parse(computer.getDiscontinued());
				return discontinued.after(introduced) && introduced.before(discontinued);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
				return false;
			}
		}
	}
}
