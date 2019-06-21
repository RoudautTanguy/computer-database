package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.dao.DAOComputer;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Service
public class ServiceCompany {

	private static final int COMPANIES_NUMBER_PER_PAGE = 20;
	
	private DAOCompany daoCompany;
	private MapperCompany mapperCompany;
	private DAOComputer daoComputer;
	
	private final static String COMPANY_DOESNT_EXIST = "The company %d doesn't exist";
	
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
		List<DTOCompany> dtoCompanies;
		try {
			dtoCompanies = daoCompany.findAll(PageRequest.of(index, limit)).stream().map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());		
		} catch(IllegalArgumentException e) {
			throw new PageNotFoundException("Page index or limit must not be less than zero!");
		}
		if(dtoCompanies.isEmpty()) {
			throw new PageNotFoundException("Page Not Found");
		} else {
			return new Page<>(dtoCompanies, index, limit, "");
		}
	}
	
	public List<DTOCompany> search(int index, int limit, String search) throws PageNotFoundException{
		List<DTOCompany> dtoCompanies = StreamSupport.stream(daoCompany.findAllByNameContaining(search, PageRequest.of(index, limit)).spliterator(), false).map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());
		if(dtoCompanies.isEmpty()) {
			throw new PageNotFoundException("Page Not Found");
		} else {
			return dtoCompanies;
		}
	}
	
	/**
	 * Get the last page possible
	 * @return page
	 */
	public int getLastPage() {
		return (int) (daoCompany.count()/COMPANIES_NUMBER_PER_PAGE);
	}
	
	public DTOCompany findById(Integer id) throws CompanyNotFoundException {
		if(id==null) {
			throw new CompanyNotFoundException("Company with null id can't exist");
		}
		Optional<Company> optCompany = daoCompany.findById(id);
		if(optCompany.isPresent()) {
			return mapperCompany.mapModelToDTO(optCompany.get());
		} else {
			throw new CompanyNotFoundException(String.format(COMPANY_DOESNT_EXIST, id));
		}
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
			throw new CompanyNotFoundException(String.format(COMPANY_DOESNT_EXIST, id));
		}
	}

	public long count() {
		return daoCompany.count();
	}
	
	public Company getLastCompany() {
		return daoCompany.findTopByOrderByIdDesc();
	}
	
	public void insert(DTOCompany dtoCompany) {
		Company company = mapperCompany.mapDTOToModel(dtoCompany);
		daoCompany.save(company);
	}

	public void update(int id, DTOCompany dtoCompany) throws CompanyNotFoundException, ConcurentConflictException {
		int version = checkConcurentConflict(id, dtoCompany);
		Company company = mapperCompany.mapDTOToModel(dtoCompany);
		company.setId(id);
		company.setVersion(version + 1);
		daoCompany.save(company);
	}
	
	public List<DTOCompany> searchByVersion(int version) throws PageNotFoundException{
		List<DTOCompany> dtoCompanies = StreamSupport.stream(daoCompany.findAllByVersion(version).spliterator(), false).map(x -> mapperCompany.mapModelToDTO(x)).collect(Collectors.toList());
		if(dtoCompanies.isEmpty()) {
			throw new PageNotFoundException("Page Not Found");
		} else {
			return dtoCompanies;
		}
	}
	
	private int checkConcurentConflict(int id, DTOCompany dtoCompany) throws ConcurentConflictException, CompanyNotFoundException {
		if(!daoCompany.findById(id).isPresent()) {
			throw new CompanyNotFoundException(String.format(COMPANY_DOESNT_EXIST, id));
		} else {
			Company company = daoCompany.findById(id).get();
			if(company.getVersion() != dtoCompany.getVersion()) {
				throw new ConcurentConflictException("This is not the actual version of the object");
			}
			return company.getVersion();
		}
	}
	
	
}
