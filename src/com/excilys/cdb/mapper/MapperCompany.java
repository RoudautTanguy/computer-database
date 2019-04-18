package com.excilys.cdb.mapper;

import com.excilys.cdb.model.Company;

public class MapperCompany {

	/**
	 * Map a model Company to his DTO
	 * @param company
	 * @return the corresponding DTO
	 */
	public static DTOCompany modelToDTO(Company company) {
		return new DTOCompany(Integer.toString(company.getId()),company.getName());
	}
	
	/**
	 * Map a DTO Company to his model
	 * @param dtoCompany
	 * @return the corresponding model
	 */
	public static Company DTOToModel(DTOCompany dtoCompany) {
		return new Company(Integer.parseInt(dtoCompany.getId()),dtoCompany.getName());
	}
}
