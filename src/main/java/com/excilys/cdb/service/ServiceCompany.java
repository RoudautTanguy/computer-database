package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOCompany;

public class ServiceCompany {

	private static final int COMPANIES_NUMBER_PER_PAGE = 20;
	
	DAOCompany daoCompany = DAOCompany.getInstance();
	
	private static ServiceCompany instance;
	
	public static ServiceCompany getInstance() {
		if(instance == null) {
			instance = new ServiceCompany();
		}
		return instance;
	}
	
	/**
	 * List companies with pagination
	 * @return the page of companies
	 * @throws PageNotFoundException 
	 * @return page
	 */
	public Page<DTOCompany> list() throws PageNotFoundException{
		List<DTOCompany> dtoCompanies = new ArrayList<DTOCompany>();
		List<Company> companies = daoCompany.list(0, COMPANIES_NUMBER_PER_PAGE);
		for(Company company:companies) {
			dtoCompanies.add(MapperCompany.getInstance().modelToDTO(company));
		}
		
		return new Page<DTOCompany>(dtoCompanies, COMPANIES_NUMBER_PER_PAGE);
	}
	
	/**
	 * List companies with pagination and index page
	 * @param index
	 * @return page
	 * @throws PageNotFoundException
	 */
	public Page<DTOCompany> list(int index) throws PageNotFoundException{
		List<DTOCompany> dtoCompanies = new ArrayList<DTOCompany>();
		List<Company> companies = daoCompany.list(index, COMPANIES_NUMBER_PER_PAGE);
		for(Company company:companies) {
			dtoCompanies.add(MapperCompany.getInstance().modelToDTO(company));
		}
		
		return new Page<DTOCompany>(dtoCompanies, index, COMPANIES_NUMBER_PER_PAGE);
	}
	
	/**
	 * Get the last page possible
	 * @return page
	 */
	public int getLastPage() {
		return daoCompany.count()/COMPANIES_NUMBER_PER_PAGE;
	}
}
