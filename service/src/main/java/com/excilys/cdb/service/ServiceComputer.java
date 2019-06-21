package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOComputer;
import com.excilys.cdb.dao.OrderByEnum;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.validator.Validator;

@Service
public class ServiceComputer {
	
	private static final int COMPUTERS_NUMBER_PER_PAGE = Integer.MAX_VALUE;
	private Validator validator;
	private MapperComputer mapperComputer;
	private DAOComputer daoComputer;
	
	private static Logger logger = LoggerFactory.getLogger( ServiceComputer.class );
	public static final String PAGE_DOESNT_EXIST = "This page doesn't exist";
	public static final String COMPUTER_DOESNT_EXIST = "The computer %d doesn't exist";
	
	public ServiceComputer(Validator validator, MapperComputer mapperComputer, DAOComputer daoComputer) {
		this.daoComputer = daoComputer;
		this.mapperComputer = mapperComputer;
		this.validator = validator;
	}
	
	public void insert(DTOComputer dtoComputer) throws NotAValidComputerException, CompanyNotFoundException {
		Computer computer = mapperComputer.mapDTOToModel(dtoComputer);
		try{
			validator.validateComputer(computer);
			daoComputer.save(computer);
		} catch(NotAValidComputerException e) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw e;
		} catch(DataAccessException e) {
			throw new CompanyNotFoundException("Company not found");
		}
	}
	
	public void delete(int id) throws ComputerNotFoundException {
		try {
			daoComputer.deleteById(id);
		} catch(IllegalArgumentException e) {
			throw new ComputerNotFoundException("The id is null !");
		} catch(EmptyResultDataAccessException e) {
			throw new ComputerNotFoundException(String.format(COMPUTER_DOESNT_EXIST, id));
		}
		
	}
	
	public void update(int id, DTOComputer dtoComputer) throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		int version = checkConcurentConflict(id, dtoComputer);
		Computer computer = mapperComputer.mapDTOToModel(dtoComputer);
		computer.setId(id);
		try {
			validator.validateComputer(computer);
			computer.setVersion(version + 1);
			daoComputer.save(computer);
		} catch (NotAValidComputerException e) {
			logger.warn("Back validation reject this computer : {}", computer);
			throw new NotAValidComputerException("This is not a valid Computer");
		}
	}

	public Page<DTOComputer> search(int index, int limit, String search, OrderByEnum orderBy) throws PageNotFoundException {
		checkPositive(index,limit);
		search = search == null?"":search;
		List<DTOComputer> dtoComputers = new ArrayList<>();
		for(Computer computer:daoComputer.findAllByNameContains(search, PageRequest.of(index, limit, orderBy.getSort()))) {
			dtoComputers.add(mapperComputer.mapModelToDTO(computer));
		}
		if(dtoComputers.isEmpty()) {
			throw new PageNotFoundException(PAGE_DOESNT_EXIST);
		} else {
			return new Page<>(dtoComputers, index, limit, "");
		}
	}

	public Page<DTOComputer> search(String search) throws PageNotFoundException{
		search = search == null?"":search;
		List<DTOComputer> dtoComputers = new ArrayList<>();
		for(Computer computer:daoComputer.findAllByNameContains(search, PageRequest.of(0, COMPUTERS_NUMBER_PER_PAGE))) {
			dtoComputers.add(mapperComputer.mapModelToDTO(computer));
		}
		if(dtoComputers.isEmpty()) {
			throw new PageNotFoundException(PAGE_DOESNT_EXIST);
		} else {
			return new Page<>(dtoComputers, 0, COMPUTERS_NUMBER_PER_PAGE, "");
		}
	}
	
	public DTOComputer find(int id) throws ComputerNotFoundException{
		Optional<Computer> computer = daoComputer.findById(id);
		if(!computer.isPresent()) {
			throw new ComputerNotFoundException(String.format(COMPUTER_DOESNT_EXIST, id));
		} else {
			return mapperComputer.mapModelToDTO(computer.get());
		}
	}
	
	public int countByName(String search) {
		return daoComputer.countByNameContaining(search==null?"":search);
	}
	
	public int lastPage() {
		return lastPage(COMPUTERS_NUMBER_PER_PAGE,"");
	}
	
	public int lastPage(int limit, String search) {
		int count = countByName(search);
		return (count%limit==0)?count/limit:count/limit+1;
	}

	public int getLastComputerId() {
		return daoComputer.findTopByOrderByIdDesc().getId();
	}
	
	public List<DTOComputer> searchByVersion(int version) throws PageNotFoundException{
		List<DTOComputer> dtoComputers = StreamSupport.stream(daoComputer.findAllByVersion(version).spliterator(), false).map(x -> mapperComputer.mapModelToDTO(x)).collect(Collectors.toList());
		if(dtoComputers.isEmpty()) {
			throw new PageNotFoundException("Page Not Found");
		} else {
			return dtoComputers;
		}
	}
	
	public void checkPositive(int... numbers) throws PageNotFoundException {
		int[] arrayNumbers = Arrays.stream(numbers).filter(n -> n>=0).toArray();
		if(numbers.length != arrayNumbers.length) {
			throw new PageNotFoundException(PAGE_DOESNT_EXIST);
		}
	}
	
	private int checkConcurentConflict(int id, DTOComputer dtoComputer) throws ComputerNotFoundException, ConcurentConflictException {
		if(!daoComputer.findById(id).isPresent()) {
			throw new ComputerNotFoundException(String.format(COMPUTER_DOESNT_EXIST, id));
		} else {
			Computer computer = daoComputer.findById(id).get();
			if(computer.getVersion() != dtoComputer.getVersion()) {
				throw new ConcurentConflictException("This is not the actual version of the object");
			}
			return computer.getVersion();
		}
	}
}
