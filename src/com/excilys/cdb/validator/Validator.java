package com.excilys.cdb.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.excilys.cdb.mapper.DTOComputer;

public class Validator {

	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public boolean validateDTOComputer(DTOComputer computer) {
		if(computer.getName() == null || computer.getName() == "NULL") {
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
				return false;
			}
		}
	}
}
