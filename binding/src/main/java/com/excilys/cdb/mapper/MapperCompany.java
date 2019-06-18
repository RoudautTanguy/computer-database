package com.excilys.cdb.mapper;

import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.model.Company;

@Component
public class MapperCompany {
	
	/**
	 * Map a model Company to his DTO
	 * @param company
	 * @return the corresponding DTO
	 */
	public DTOCompany mapModelToDTO(Company company) {
		return new DTOCompany(company.getId(),company.getName(), company.getVersion());
	}
	
	/**
	 * Map a DTO Company to his model
	 * @param dtoCompany
	 * @return the corresponding model
	 */
	public Company mapDTOToModel(DTOCompany dtoCompany) {
		return new Company(dtoCompany.getId(),dtoCompany.getName(), dtoCompany.getVersion());
	}
}
