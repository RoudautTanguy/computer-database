package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DAOCompany;
import com.excilys.cdb.persistence.DAOFactory;

public class ServiceCompany {

	DAOCompany daoCompany = DAOFactory.getDAOCompany();
	
	public List<DTOCompany> list(){
		List<DTOCompany> dtoCompanies = new ArrayList<DTOCompany>();
		List<Company> companies = daoCompany.list();
		for(Company company:companies) {
			dtoCompanies.add(MapperCompany.modelToDTO(company));
		}
		return dtoCompanies;
	}
}
