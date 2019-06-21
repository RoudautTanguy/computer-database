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

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.exceptions.BadRequestException;
import com.excilys.cdb.service.ServiceCompany;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/companies")
public class CompanyRestController {
	
	ServiceCompany serviceCompany;
	
	public CompanyRestController(ServiceCompany serviceCompany) {
		this.serviceCompany = serviceCompany;
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<DTOCompany> findAll(@RequestParam(value="search", required = false) String search,
			 						@RequestParam(value="range",required = false) String range,
			 						@RequestParam(value="version", required = false) Integer version) throws PageNotFoundException, BadRequestException{
		if(version!=null) {
			return serviceCompany.searchByVersion(version);
		}
		
		search = search==null?"":search;
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
		return this.serviceCompany.search(page, limit, search);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public DTOCompany findById(@PathVariable("id") int id) throws CompanyNotFoundException {
		return this.serviceCompany.findById(id);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody DTOCompany company) {
		this.serviceCompany.insert(company);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable("id") int id, @RequestBody DTOCompany company) throws CompanyNotFoundException, ConcurentConflictException {
		this.serviceCompany.update(id, company);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") int id) throws CompanyNotFoundException {
		this.serviceCompany.deleteById(id);
	}

}
