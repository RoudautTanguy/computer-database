package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOComputer;

public class ServiceComputer {
	
	private static final int COMPUTERS_NUMBER_PER_PAGE = 50;

	private static ServiceComputer instance;
	
	public static ServiceComputer getInstance() {
		if(instance == null) {
			instance = new ServiceComputer();
		}
		return instance;
	}
	
	/**
	 * Insert a new computer
	 * @param computer
	 * @return if the computer is inserted or not
	 * @throws CompanyNotFoundException
	 */
	public boolean insert(DTOComputer computer) throws CompanyNotFoundException {
		return DAOComputer.getInstance().insert(MapperComputer.getInstance().DTOToModel(computer));
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
	 */
	public boolean update(int id, DTOComputer computer) {
		return DAOComputer.getInstance().update(id, MapperComputer.getInstance().DTOToModel(computer));
	}
	
	/**
	 * List all the computers with pagination
	 * @return the current page of computer
	 * @throws PageNotFoundException 
	 */
	public Page<DTOComputer> list() throws PageNotFoundException{
		List<DTOComputer> dtoComputers = new ArrayList<DTOComputer>();
		List<Computer> computers = DAOComputer.getInstance().list(0,COMPUTERS_NUMBER_PER_PAGE);
		for(Computer computer:computers) {
			dtoComputers.add(MapperComputer.getInstance().modelToDTO(computer));
		}
		return new Page<DTOComputer>(dtoComputers, COMPUTERS_NUMBER_PER_PAGE);
	}
	
	/**
	 * List all the computers with pagination the index of the page
	 * @param index
	 * @return
	 */
	public Page<DTOComputer> list(int index) throws PageNotFoundException{
		List<DTOComputer> dtoComputers = new ArrayList<DTOComputer>();
		List<Computer> computers = DAOComputer.getInstance().list(index, COMPUTERS_NUMBER_PER_PAGE);
		for(Computer computer:computers) {
			dtoComputers.add(MapperComputer.getInstance().modelToDTO(computer));
		}
		return new Page<DTOComputer>(dtoComputers, index, COMPUTERS_NUMBER_PER_PAGE);
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
		return DAOComputer.getInstance().count()/COMPUTERS_NUMBER_PER_PAGE;
	}
}
