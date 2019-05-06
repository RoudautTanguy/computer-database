package com.excilys.cdb.mapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Computer;

public class MapperComputer {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(MapperComputer.class);
	
	private static MapperComputer instance;
	
	public static MapperComputer getInstance() {
		if(instance == null) {
			instance = new MapperComputer();
		}
		return instance;
	}
	
	/**
	 * Map a model Computer to his DTO
	 * @param computer
	 * @return the corresponding DTO
	 */
	public DTOComputer modelToDTO(Computer computer) {
		String id = Integer.toString(computer.getId());
		String name = computer.getName();
		String introduced = "";
		if(computer.getIntroduced() != null) {
			introduced = dateFormat.format(computer.getIntroduced());
		}
		String discontinued = "";
		if(computer.getDiscontinued() != null) {
			discontinued = dateFormat.format(computer.getDiscontinued());;
		}
		String company = "";
		if(computer.getCompanyId() != null) {
			company = Integer.toString(computer.getCompanyId());
		}
		return new DTOComputer.DTOComputerBuilder(name)
							  .withId(id)
							  .withIntroduced(introduced)
							  .withDiscontinued(discontinued)
							  .withCompany(company)
							  .build();
	}
	
	/**
	 * Map a DTO Computer to his model
	 * @param computer
	 * @return the corresponding model
	 */
	public Computer DTOToModel(DTOComputer computer) {
		int id = Integer.parseInt(computer.getId());
		String name = computer.getName();
		Computer newComputer = new Computer.ComputerBuilder(name).withId(id).build();
		Optional<Timestamp> optionalIntroduced = tryParse(computer.getIntroduced());
		Optional<Timestamp> optionalDiscontinued = tryParse(computer.getDiscontinued());
		
		if(!optionalIntroduced.isEmpty()) {
			newComputer.setIntroduced(optionalIntroduced.get());
		} else {
			logger.warn("Can't parse introduced date {}", computer.getIntroduced());
		}
		if(!optionalDiscontinued.isEmpty()) {
			newComputer.setDiscontinued(optionalDiscontinued.get());
		} else {
			logger.warn("Can't parse introduced date {}", computer.getDiscontinued());
		}
		
		try {
			newComputer.setCompanyId(Integer.parseInt(computer.getCompany()));
		} catch(NumberFormatException e) {
			logger.warn("Can't parse company id {}", computer.getCompany());
		}
		return newComputer;
	}
	
	Optional<Timestamp> tryParse(String dateString){
		List<String> formatStrings = Arrays.asList("dd/MM/yyyy","dd-MM-yyyy","yyyy-MM-dd");
	    for (String formatString : formatStrings) {
	        try {
	            return Optional.ofNullable(new Timestamp(new SimpleDateFormat(formatString).parse(dateString).getTime()));
	        } catch (ParseException e) {}
	    }
	    return Optional.empty();
	}
}
