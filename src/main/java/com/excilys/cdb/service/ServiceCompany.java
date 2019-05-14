package com.excilys.cdb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOCompany;

@Service
public class ServiceCompany {

	private static final int COMPANIES_NUMBER_PER_PAGE = 20;
	
	@Autowired
	private DAOCompany daoCompany;
	@Autowired
	private MapperCompany mapperCompany;
	
	/**
	 * List companies with pagination
	 * @return the page of companies
	 * @throws PageNotFoundException 
	 * @return page
	 */
	public List<DTOCompany> list(){
		return daoCompany.list().stream().map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());
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
		List<DTOCompany> dtoCompanies = daoCompany.list(index, limit).stream().map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());		
		return new Page<>(dtoCompanies, index, limit, "");
	}
	
	/**
	 * Get the last page possible
	 * @return page
	 */
	public int getLastPage() {
		return daoCompany.countCompanies()/COMPANIES_NUMBER_PER_PAGE;
	}
}
