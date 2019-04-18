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

	public boolean insert(DTOComputer computer) throws CompanyNotFoundException {
		return daoComputer.insert(MapperComputer.DTOToModel(computer));
	}
	
	public boolean delete(int id){
		return daoComputer.delete(id);
	}
	
	public boolean update(int id, DTOComputer computer) {
		return daoComputer.update(id, MapperComputer.DTOToModel(computer));
	}
	
	public Page<DTOComputer> list(){
		List<DTOComputer> dtoComputers = new ArrayList<DTOComputer>();
		List<Computer> computers = daoComputer.list();
		for(Computer computer:computers) {
			dtoComputers.add(MapperComputer.modelToDTO(computer));
		}
		return new Page<DTOComputer>(dtoComputers,50);
	}
	
	public DTOComputer find(int id) throws ComputerNotFoundException {
		return daoComputer.find(id);
	}
}
