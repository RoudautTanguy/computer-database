package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOComputer;
import com.excilys.cdb.validator.Validator;

public class ServiceComputer {
	
	private static final int COMPUTERS_NUMBER_PER_PAGE = 50;

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
	 */
	public boolean insert(DTOComputer dtoComputer) throws NotAValidComputerException {
		Computer computer = MapperComputer.getInstance().DTOToModel(dtoComputer);
		Validator validator = new Validator();
		if(!validator.validateComputer(computer)) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw new NotAValidComputerException("This is not a valid Computer");
		}
		return DAOComputer.getInstance().insert(computer);
	}
	
	/**
	 * Delete a computer by is id
	 * @param id of the computer
	 * @return if the computer is deleted or not
	 */
	public boolean delete(int id){
		return DAOComputer.getInstance().delete(id);
	}
	
	/**
	 * Update a computer
	 * @param id of the computer to update
	 * @param computer the new computer
	 * @return if the computer is updated or not
	 * @throws NotAValidComputerException 
	 */
	public boolean update(int id, DTOComputer dtoComputer) throws NotAValidComputerException {
		Computer computer = MapperComputer.getInstance().DTOToModel(dtoComputer);
		Validator validator = new Validator();
		if(!validator.validateComputer(computer)) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw new NotAValidComputerException("This is not a valid Computer");
		}
		return DAOComputer.getInstance().update(id, computer);
	}
	
	public Page<DTOComputer> list(int index, int limit) throws PageNotFoundException{
		List<DTOComputer> dtoComputers = new ArrayList<DTOComputer>();
		List<Computer> computers = DAOComputer.getInstance().list(index, limit);
		for(Computer computer:computers) {
			dtoComputers.add(MapperComputer.getInstance().modelToDTO(computer));
		}
		return new Page<DTOComputer>(dtoComputers, index, limit);
	}
	
	/**
	 * List all the computers with pagination the index of the page
	 * @param index
	 * @throws PageNotFoundException
	 * @return page
	 */
	public Page<DTOComputer> list(int index) throws PageNotFoundException{
		return list(index, COMPUTERS_NUMBER_PER_PAGE);
	}
	
	/**
	 * List all the computers with pagination
	 * @return the current page of computer
	 * @throws PageNotFoundException 
	 * @return page
	 */
	public Page<DTOComputer> list() throws PageNotFoundException{
		return list(0);
	}
	
	
	
	
	
	/**
	 * Find a computer by is id
	 * @param id of the computer
	 * @return the computer if found
	 * @throws ComputerNotFoundException if not found
	 */
	public DTOComputer find(int id) throws ComputerNotFoundException {
		return DAOComputer.getInstance().find(id);
	}
	
	public int count() {
		return DAOComputer.getInstance().count();
	}
	
	public int lastPage() {
		return lastPage(COMPUTERS_NUMBER_PER_PAGE);
	}
	
	public int lastPage(int limit) {
		int count = DAOComputer.getInstance().count();
		return (count%limit==0)?count/limit:count/limit+1;
	}
}
