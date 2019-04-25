package com.excilys.cdb.mapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
		String introduced = "NULL";
		if(computer.getIntroduced() != null) {
			introduced = dateFormat.format(computer.getIntroduced());
		}
		String discontinued = "NULL";
		if(computer.getDiscontinued() != null) {
			discontinued = dateFormat.format(computer.getDiscontinued());;
		}
		String company = "NULL";
		if(!computer.getCompanyId().equals(0) && computer.getCompanyId() != null) {
			company = Integer.toString(computer.getCompanyId());
		}
		return new DTOComputer(id, name, introduced, discontinued, company);
	}
	
	/**
	 * Map a DTO Computer to his model
	 * @param computer
	 * @return the corresponding model
	 */
	public Computer DTOToModel(DTOComputer computer) {
		int id = Integer.parseInt(computer.getId());
		String name = computer.getName();
		Timestamp introduced;
		Timestamp discontinued;
		Integer companyId;
		try {
			introduced = new Timestamp(dateFormat.parse(computer.getIntroduced()).getTime());
		} catch (ParseException e) {
			logger.warn("Can't parse introduced date {}", computer.getIntroduced());
			introduced = null;
		}
		try {
			discontinued = new Timestamp(dateFormat.parse(computer.getDiscontinued()).getTime());
		} catch(ParseException e) {
			logger.warn("Can't parse discontinued date {}", computer.getDiscontinued());
			discontinued = null;
		}
		try {
			companyId = Integer.parseInt(computer.getCompany());
		} catch(NumberFormatException e) {
			logger.warn("Can't parse company id {}", computer.getCompany());
			companyId = null;
		}
		
		return new Computer(id, name, introduced, discontinued, companyId);
	}
}
