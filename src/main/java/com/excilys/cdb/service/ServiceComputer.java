package com.excilys.cdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOComputer;
import com.excilys.cdb.persistence.OrderByEnum;
import com.excilys.cdb.validator.Validator;

@Service
public class ServiceComputer {
	
	private static final int COMPUTERS_NUMBER_PER_PAGE = 50;
	private Validator validator;
	private MapperComputer mapperComputer;
	private DAOComputer daoComputer;
	
	private static Logger logger = LoggerFactory.getLogger( ServiceComputer.class );
	
	public ServiceComputer(Validator validator, MapperComputer mapperComputer, DAOComputer daoComputer) {
		this.daoComputer = daoComputer;
		this.mapperComputer = mapperComputer;
		this.validator = validator;
	}
	
	/**
	 * Insert a new computer
	 * @param computer
	 * @return if the computer is inserted or not
	 * @throws NotAValidComputerException 
	 * @throws CompanyNotFoundException 
	 * @throws CantConnectException 
	 */
	public void insert(DTOComputer dtoComputer) throws NotAValidComputerException, CompanyNotFoundException {
		Computer computer = mapperComputer.mapDTOToModel(dtoComputer);
		try{
			validator.validateComputer(computer);
			daoComputer.insertComputer(computer);
		} catch(NotAValidComputerException e) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw e;
		}
	}
	
	/**
	 * Delete a computer by is id
	 * @param id of the computer
	 * @return if the computer is deleted or not
	 * @throws ComputerNotFoundException 
	 * @throws CantConnectException 
	 */
	public void delete(int id) throws ComputerNotFoundException{
		daoComputer.deleteComputer(id);
	}
	
	/**
	 * Update a computer
	 * @param id of the computer to update
	 * @param computer the new computer
	 * @return if the computer is updated or not
	 * @throws NotAValidComputerException 
	 * @throws ComputerNotFoundException 
	 * @throws CantConnectException 
	 */
	public void update(int id, DTOComputer dtoComputer) throws NotAValidComputerException, ComputerNotFoundException {
		Computer computer = mapperComputer.mapDTOToModel(dtoComputer);
		try {
			validator.validateComputer(computer);
			daoComputer.updateComputer(id, computer);
		} catch (NotAValidComputerException e) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw new NotAValidComputerException("This is not a valid Computer");
		}
	}
	
	/**
	 * List all the computers with pagination and names instead of id
	 * @return the current page of computer
	 * @throws PageNotFoundException 
	 * @return page
	 * @throws ComputerNotFoundException 
	 */
	public Page<DTOComputer> search(int index, int limit, String search, OrderByEnum orderBy) throws PageNotFoundException, ComputerNotFoundException{
		search = search == null?"":search;
		return new Page<>(daoComputer.search(index, limit, search, orderBy), index, limit, "");
	}
	
	/**
	 * List all the computers with pagination and names instead of id
	 * @return the current page of computer
	 * @throws PageNotFoundException 
	 * @return page
	 * @throws ComputerNotFoundException 
	 */
	public Page<DTOComputer> search(String search) throws PageNotFoundException, ComputerNotFoundException{
		search = search == null?"":search;
		return new Page<>(daoComputer.search(0, COMPUTERS_NUMBER_PER_PAGE, search, OrderByEnum.DEFAULT), 0, COMPUTERS_NUMBER_PER_PAGE, "");
	}
	
	/**
	 * Find a computer by is id
	 * @param id of the computer
	 * @return the computer if found
	 * @throws ComputerNotFoundException if not found
	 */
	public DTOComputer find(int id) throws ComputerNotFoundException {
		return daoComputer.find(id);
	}
	
	public int count(String search) {
		return daoComputer.countComputers(search);
	}
	
	public int lastPage() {
		return lastPage(COMPUTERS_NUMBER_PER_PAGE,"");
	}
	
	public int lastPage(int limit, String search) {
		int count = count(search);
		return (count%limit==0)?count/limit:count/limit+1;
	}
}
