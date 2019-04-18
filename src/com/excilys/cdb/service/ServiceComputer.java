package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOComputer;
import com.excilys.cdb.persistence.DAOFactory;

public class ServiceComputer {
	
	DAOComputer daoComputer = DAOFactory.getDAOComputer();

	/**
	 * Insert a new computer
	 * @param computer
	 * @return if the computer is inserted or not
	 * @throws CompanyNotFoundException
	 */
	public boolean insert(DTOComputer computer) throws CompanyNotFoundException {
		return daoComputer.insert(MapperComputer.DTOToModel(computer));
	}
	
	/**
	 * Delete a computer by is id
	 * @param id of the computer
	 * @return if the computer is deleted or not
	 */
	public boolean delete(int id){
		return daoComputer.delete(id);
	}
	
	/**
	 * Update a computer
	 * @param id of the computer to update
	 * @param computer the new computer
	 * @return if the computer is updated or not
	 */
	public boolean update(int id, DTOComputer computer) {
		return daoComputer.update(id, MapperComputer.DTOToModel(computer));
	}
	
	/**
	 * List all the computers with pagination
	 * @return the current page of computer
	 */
	public Page<DTOComputer> list(){
		List<DTOComputer> dtoComputers = new ArrayList<DTOComputer>();
		List<Computer> computers = daoComputer.list();
		for(Computer computer:computers) {
			dtoComputers.add(MapperComputer.modelToDTO(computer));
		}
		return new Page<DTOComputer>(dtoComputers,50);
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
}
