package com.excilys.cdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOComputer;
import com.excilys.cdb.persistence.OrderByEnum;
import com.excilys.cdb.validator.Validator;

public class ServiceComputer {
	
	private static final int COMPUTERS_NUMBER_PER_PAGE = 50;
	private Validator validator = Validator.getInstance();
	private MapperComputer mapperComputer = MapperComputer.getInstance();
	private DAOComputer daoComputer = DAOComputer.getInstance();
	
	private static ServiceComputer instance;
	
	public static ServiceComputer getInstance() {
		if(instance == null) {
			instance = new ServiceComputer();
		}
		return instance;
	}
	
	private static Logger logger = LoggerFactory.getLogger( ServiceComputer.class );
	
	/**
	 * Insert a new computer
	 * @param computer
	 * @return if the computer is inserted or not
	 * @throws NotAValidComputerException 
	 * @throws CantConnectException 
	 */
	public boolean insert(DTOComputer dtoComputer) throws NotAValidComputerException, CantConnectException {
		Computer computer = mapperComputer.DTOToModel(dtoComputer);
		try{
			validator.validateComputer(computer);
			return daoComputer.insert(computer);
		} catch(NotAValidComputerException e) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw new NotAValidComputerException("This is not a valid Computer");
		}
	}
	
	/**
	 * Delete a computer by is id
	 * @param id of the computer
	 * @return if the computer is deleted or not
	 * @throws ComputerNotFoundException 
	 * @throws CantConnectException 
	 */
	public void delete(int id) throws CantConnectException, ComputerNotFoundException{
		daoComputer.delete(id);
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
	public void update(int id, DTOComputer dtoComputer) throws NotAValidComputerException, ComputerNotFoundException, CantConnectException {
		Computer computer = mapperComputer.DTOToModel(dtoComputer);
		try {
			validator.validateComputer(computer);
			daoComputer.update(id, computer);
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
	 */
	public Page<DTOComputer> search(int index, int limit, String search, OrderByEnum orderBy) throws PageNotFoundException{
		search = search == null?"":search;
		return new Page<DTOComputer>(daoComputer.search(index, limit, search, orderBy), index, limit);
	}
	
	/**
	 * List all the computers with pagination and names instead of id
	 * @return the current page of computer
	 * @throws PageNotFoundException 
	 * @return page
	 */
	public Page<DTOComputer> search(String search) throws PageNotFoundException{
		search = search == null?"":search;
		return new Page<DTOComputer>(daoComputer.search(0, COMPUTERS_NUMBER_PER_PAGE, search, OrderByEnum.DEFAULT), 0, COMPUTERS_NUMBER_PER_PAGE);
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
		return daoComputer.count(search);
	}
	
	public int lastPage() {
		return lastPage(COMPUTERS_NUMBER_PER_PAGE,"");
	}
	
	public int lastPage(int limit, String search) {
		int count = count(search);
		return (count%limit==0)?count/limit:count/limit+1;
	}
}
