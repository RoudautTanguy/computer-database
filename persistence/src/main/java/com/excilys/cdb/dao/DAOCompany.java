package com.excilys.cdb.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.excilys.cdb.model.Company;

public interface DAOCompany extends PagingAndSortingRepository<Company, Integer> {

	public long count() ;
	
	public Long deleteById(int id);

	public Company findTopByOrderByIdDesc();
	
	public List<Company> findAllByNameContaining(String name, Pageable pageable);

	public List<Company> findAllByVersion(int version);
}
