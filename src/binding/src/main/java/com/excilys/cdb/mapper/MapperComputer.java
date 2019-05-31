package com.excilys.cdb.mapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ServiceCompany;

@Component
public class MapperComputer {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	ServiceCompany serviceCompany;
	
	private static final Logger logger = LoggerFactory.getLogger(MapperComputer.class);
	
	public MapperComputer(ServiceCompany serviceCompany) {
		this.serviceCompany = serviceCompany;
	}
	
	/**
	 * Map a model Computer to his DTO
	 * @param computer
	 * @return the corresponding DTO
	 */
	public DTOComputer mapModelToDTO(Computer computer) {
		int id = computer.getId();
		String name = computer.getName();
		String introduced = "";
		if(computer.getIntroduced() != null) {
			introduced = dateFormat.format(computer.getIntroduced());
		}
		String discontinued = "";
		if(computer.getDiscontinued() != null) {
			discontinued = dateFormat.format(computer.getDiscontinued());
		}
		Integer companyId = null;
		String companyName = "";
		if(computer.getCompany() != null) {
			companyName = computer.getCompany().getName();
			companyId = computer.getCompany().getId();
		}
		return new DTOComputer(id,name,introduced,discontinued,companyId,companyName);
	}
	
	/**
	 * Map a DTO Computer to his model
	 * @param computer
	 * @return the corresponding model
	 * @throws CompanyNotFoundException 
	 */
	public Computer mapDTOToModel(DTOComputer computer) throws CompanyNotFoundException {
		int id = computer.getId();
		String name = computer.getName();
		Timestamp introduced = tryParse(computer.getIntroduced()).orElse(null);
		Timestamp discontinued = tryParse(computer.getDiscontinued()).orElse(null);
		Integer companyId = computer.getCompanyId();
		if(companyId!=null) {
			Optional<Company> company = this.serviceCompany.findById(companyId);
			if(company.isPresent()) {
				return new Computer(id,name,introduced,discontinued,company.get());
			} else {
				throw new CompanyNotFoundException("Company "+companyId+" doesn't exist");
			}
		}
		return new Computer(id,name,introduced,discontinued,null);
	}
	
	Optional<Timestamp> tryParse(String dateString){
		List<String> formatStrings = Arrays.asList("dd/MM/yyyy","dd-MM-yyyy","yyyy-MM-dd");
	    for (String formatString : formatStrings) {
	        try {
	            return Optional.ofNullable(new Timestamp(new SimpleDateFormat(formatString).parse(dateString).getTime()));
	        } catch (ParseException e) {
	        	logger.info("Can't parse the date with {}",formatString);
	        }
	    }
	    return Optional.empty();
	}
}
