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

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.model.Computer;

@Component
public class MapperComputer {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(MapperComputer.class);
	
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
		String company = "";
		if(computer.getCompanyId() != null && computer.getCompanyId() != 0) {
			company = Integer.toString(computer.getCompanyId());
		}
		return new DTOComputer(id,name,introduced,discontinued,company);
	}
	
	/**
	 * Map a DTO Computer to his model
	 * @param computer
	 * @return the corresponding model
	 */
	public Computer mapDTOToModel(DTOComputer computer) {
		int id = computer.getId();
		String name = computer.getName();
		Computer newComputer = new Computer.ComputerBuilder(name).withId(id).build();
		Optional<Timestamp> optionalIntroduced = tryParse(computer.getIntroduced());
		Optional<Timestamp> optionalDiscontinued = tryParse(computer.getDiscontinued());
		
		if(optionalIntroduced.isPresent()) {
			newComputer.setIntroduced(optionalIntroduced.get());
		} else {
			String introduced = computer.getIntroduced().replaceAll(Constant.SANITIZER_REPLACER, "_");
			logger.warn("Can't parse introduced date {}", introduced);
		}
		if(optionalDiscontinued.isPresent()) {
			newComputer.setDiscontinued(optionalDiscontinued.get());
		} else {
			String discontinued = computer.getDiscontinued().replaceAll(Constant.SANITIZER_REPLACER, "_");
			logger.warn("Can't parse introduced date {}", discontinued);
		}
		
		try {
			newComputer.setCompanyId(Integer.parseInt(computer.getCompany()));
		} catch(NumberFormatException e) {
			String company = computer.getCompany();
			logger.warn("Can't parse company id {}", company);
		}
		return newComputer;
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
