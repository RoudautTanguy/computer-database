package com.excilys.cdb.validator;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DAOCompany;

@Component
public class Validator {

	private DAOCompany daoCompany;
	
	public Validator(DAOCompany daoCompany) {
		this.daoCompany = daoCompany;
	}
	
	private static Logger logger = LoggerFactory.getLogger( Validator.class );

	public void validateComputer(Computer computer) throws NotAValidComputerException, CompanyNotFoundException{
		validateNameIsPresent(computer.getName());
		validateDiscontinuedDateIsNullIfIntroducedIsNull(computer.getIntroduced(), computer.getDiscontinued());
		validateDiscontinuedDateIsAfterIntroducedDate(computer.getIntroduced(),computer.getDiscontinued());
		validateCompanyExist(computer.getCompany());
	}

	private void validateCompanyExist(Company company) throws CompanyNotFoundException {
		if(company!=null && !daoCompany.findById(company.getId()).isPresent()) {
			throw new CompanyNotFoundException("The company "+company.getId()+" doesn't exist");
		}
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
