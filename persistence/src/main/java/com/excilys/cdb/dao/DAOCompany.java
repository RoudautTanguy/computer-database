package com.excilys.cdb.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.excilys.cdb.model.Company;

public interface DAOCompany extends PagingAndSortingRepository<Company, Integer> {

	public long count() ;
	
	public Long deleteById(int id);

	public Company findTopByOrderByIdDesc();
}
