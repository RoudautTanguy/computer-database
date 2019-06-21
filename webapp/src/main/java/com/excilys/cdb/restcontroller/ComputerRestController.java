package com.excilys.cdb.restcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.excilys.cdb.dao.OrderByEnum;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.exceptions.BadRequestException;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.service.ServicePagination;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/computers/")
public class ComputerRestController {

	private ServiceComputer serviceComputer;
	private ServicePagination servicePagination;
	
	public ComputerRestController(ServiceComputer serviceComputer, ServicePagination servicePagination) {
		this.serviceComputer = serviceComputer;
		this.servicePagination = servicePagination;
	}
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public List<DTOComputer> findAll(@RequestParam(value="search", required = false) String search,
									 @RequestParam(value="range",required = false) String range,
									 @RequestParam(value="sort", required = false) String sort,
									 @RequestParam(value="desc", required = false) String desc,
									 @RequestParam(value="version", required = false) Integer version)throws PageNotFoundException, BadRequestException {
		if(version!=null) {
			return serviceComputer.searchByVersion(version);
		}
		int first = 0;
		int limit = Integer.MAX_VALUE;
		if(range!=null) {
			String[] rangeArr = range.split("-");
			if(rangeArr.length != 2) {
				throw new BadRequestException("Range expect 2 param");
			}
			first = Integer.parseInt(rangeArr[0]);
			int last = Integer.parseInt(rangeArr[1]);
			if(first > last) {
				throw new BadRequestException("First element after Last element");
			}
			limit = last - first;
			if(first == last) {
				throw new PageNotFoundException("No element");
			}
		}
		
		int page = first / limit;
		OrderByEnum orderBy = servicePagination.getOrderBy(sort, desc!=null);
		
		return this.serviceComputer.search(page,limit,search,orderBy).getList();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
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
	public void update(@PathVariable("id") int id, @RequestBody DTOComputer computer) throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		this.serviceComputer.update(id, computer);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") int id) throws ComputerNotFoundException {
		this.serviceComputer.delete(id);
	}
	
}
