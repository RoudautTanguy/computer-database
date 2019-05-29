package com.excilys.cdb.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;

@Repository
public interface DAOCompany extends PagingAndSortingRepository<Company, Integer> {

	public long count() ;
	
	public Long deleteById(int id);

	public Company findTopByOrderByIdDesc();
}
