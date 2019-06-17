package com.excilys.cdb.restcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.service.ServiceComputer;
import com.mysql.cj.exceptions.WrongArgumentException;

@RestController
@RequestMapping("/api/v1/computers/")
public class ComputerRestController {

	private ServiceComputer serviceComputer;
	
	public ComputerRestController(ServiceComputer serviceComputer) {
		this.serviceComputer = serviceComputer;
	}
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.FOUND)
	public List<DTOComputer> findAll(@RequestParam(value="search", required = false) String search,
									 @RequestParam(value="range",required = false) String range)throws PageNotFoundException {
		String[] rangeArr = range.split("-");
		if(rangeArr.length != 2) {
			throw new WrongArgumentException("Range expect 2 param");
		}
		return this.serviceComputer.search(search).getList();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public DTOComputer findById(@PathVariable("id") int id) throws ComputerNotFoundException {
		return this.serviceComputer.find(id);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody DTOComputer computer) throws NotAValidComputerException, CompanyNotFoundException {
		this.serviceComputer.insert(computer);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable("id") int id, @RequestBody DTOComputer computer) throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException {
		this.serviceComputer.update(id, computer);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") int id) throws ComputerNotFoundException {
		this.serviceComputer.delete(id);
	}
	
}
