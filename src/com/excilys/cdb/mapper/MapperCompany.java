package com.excilys.cdb.mapper;

import com.excilys.cdb.model.Company;

public class MapperCompany {

	public static DTOCompany modelToDTO(Company company) {
		return new DTOCompany(Integer.toString(company.getId()),company.getName());
	}
	
	public static Company DTOToModel(DTOCompany dtoCompany) {
		return new Company(Integer.parseInt(dtoCompany.getId()),dtoCompany.getName());
	}
}
