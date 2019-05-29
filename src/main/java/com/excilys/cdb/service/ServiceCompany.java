package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOCompany;
import com.excilys.cdb.persistence.DAOComputer;

@Service
public class ServiceCompany {

	private static final int COMPANIES_NUMBER_PER_PAGE = 20;
	
	private DAOCompany daoCompany;
	private MapperCompany mapperCompany;
	private DAOComputer daoComputer;
	
	public ServiceCompany(DAOCompany daoCompany, DAOComputer daoComputer, MapperCompany mapperCompany) {
		this.daoCompany = daoCompany;
		this.mapperCompany = mapperCompany;
		this.daoComputer = daoComputer;
	}
	
	/**
	 * List companies with pagination
	 * @return the page of companies
	 * @throws PageNotFoundException 
	 * @return page
	 */
	public List<DTOCompany> list(){
		return StreamSupport.stream(daoCompany.findAll().spliterator(), false).map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());
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
		try {
			List<DTOCompany> dtoCompanies = daoCompany.findAll(PageRequest.of(index, limit)).stream().map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());		
			if(dtoCompanies.isEmpty()) {
				throw new PageNotFoundException("Page Not Found");
			} else {
				return new Page<>(dtoCompanies, index, limit, "");
			}
		} catch (IllegalArgumentException e) {
			throw new PageNotFoundException("Page Not Found");
		}
	}
	
	/**
	 * Get the last page possible
	 * @return page
	 */
	public int getLastPage() {
		return (int) (daoCompany.count()/COMPANIES_NUMBER_PER_PAGE);
	}
	
	public Optional<Company> findById(Integer id) throws CompanyNotFoundException {
		if(id==null) {
			throw new CompanyNotFoundException("Company with null id can't exist");
		}
		return daoCompany.findById(id);
	}

	@Transactional
	public Long deleteById(int id) throws CompanyNotFoundException {
		if(id<0) {
			throw new CompanyNotFoundException("Company with negative id can't exist.");
		}
		try{
			daoComputer.deleteByCompanyId(id);
			return daoCompany.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new CompanyNotFoundException("Company doesn't exist.");
		}
	}

	public long count() {
		return daoCompany.count();
	}
	
	public Company getLastCompany() {
		return daoCompany.findTopByOrderByIdDesc();
	}
	
	public void insert(Company company) {
		daoCompany.save(company);
	}
	
	
}
