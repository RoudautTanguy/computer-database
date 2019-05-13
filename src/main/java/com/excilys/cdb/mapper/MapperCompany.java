package com.excilys.cdb.mapper;

import com.excilys.cdb.model.Company;

public class MapperCompany {

	private static MapperCompany instance;
	
	public static MapperCompany getInstance() {
		if(instance == null) {
			instance = new MapperCompany();
		}
		return instance;
	}
	
	/**
	 * Map a model Company to his DTO
	 * @param company
	 * @return the corresponding DTO
	 */
	public DTOCompany mapModelToDTO(Company company) {
		return new DTOCompany(Integer.toString(company.getId()),company.getName());
	}
	
	/**
	 * Map a DTO Company to his model
	 * @param dtoCompany
	 * @return the corresponding model
	 */
	public Company mapDTOToModel(DTOCompany dtoCompany) {
		return new Company(Integer.parseInt(dtoCompany.getId()),dtoCompany.getName());
	}
}
