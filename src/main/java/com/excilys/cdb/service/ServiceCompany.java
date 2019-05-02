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
	public List<DTOCompany> list(){
		List<DTOCompany> dtoCompanies = new ArrayList<DTOCompany>();
		List<Company> companies = daoCompany.list();
		for(Company company:companies) {
			dtoCompanies.add(MapperCompany.getInstance().modelToDTO(company));
		}
		return dtoCompanies;
	}
	
	/**
	 * List companies with pagination and index page
	 * @param index
	 * @return page
	 * @throws PageNotFoundException
	 */
	public Page<DTOCompany> list(int index) throws PageNotFoundException{
		return list(index, COMPANIES_NUMBER_PER_PAGE);
	}
	
	/**
	 * List companies with pagination and index page and limit
	 * @param index
	 * @return page
	 * @throws PageNotFoundException
	 */
	public Page<DTOCompany> list(int index, int limit) throws PageNotFoundException{
		List<DTOCompany> dtoCompanies = new ArrayList<DTOCompany>();
		List<Company> companies = daoCompany.list(index, limit);
		for(Company company:companies) {
			dtoCompanies.add(MapperCompany.getInstance().modelToDTO(company));
		}
		
		return new Page<DTOCompany>(dtoCompanies, index, limit);
	}
	
	/**
	 * Get the last page possible
	 * @return page
	 */
	public int getLastPage() {
		return daoCompany.count()/COMPANIES_NUMBER_PER_PAGE;
	}
}
