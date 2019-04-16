package com.excilys.cdb.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.excilys.cdb.model.Computer;

public class MapperComputer {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public static DTOComputer modelToDTO(Computer computer) {
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
		String companyId = "NULL";
		if(!Integer.toString(computer.getCompanyId()).equals(0)) {
			companyId = Integer.toString(computer.getCompanyId());
		}
		return new DTOComputer(id, name, introduced, discontinued, companyId);
	}
	
	public static Computer DTOToModel(DTOComputer computer) {
		int id = Integer.parseInt(computer.getId());
		String name = computer.getName();
		Date introduced;
		Date discontinued;
		try {
			introduced = dateFormat.parse(computer.getIntroduced());
			discontinued = dateFormat.parse(computer.getDiscontinued());
		} catch (ParseException e) {
			introduced = null;
			discontinued = null;
		}
		int companyId = Integer.parseInt(computer.getCompanyId());
		return new Computer(id, name, introduced, discontinued, companyId);
	}
}
