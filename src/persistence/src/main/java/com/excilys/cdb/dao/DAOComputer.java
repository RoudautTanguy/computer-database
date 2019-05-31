package com.excilys.cdb.persistence;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;

@Repository
public interface DAOComputer  extends PagingAndSortingRepository<Computer, Integer>{

	@Query(value = "SELECT COUNT(*) FROM computer LEFT JOIN company ON computer.company_id=company.id " + 
    		"WHERE ( computer.name LIKE %:search% OR company.name LIKE %:search% )", nativeQuery = true)
    int countByNameContaining(@Param("search") String search);

	@Query(value = "SELECT c.id,c.name,c.introduced,c.discontinued,c.company_id,"
			+ "cp.name AS company_name FROM computer c LEFT JOIN company cp ON c.company_id=cp.id "
			+ "WHERE ( c.name LIKE %:search% OR cp.name LIKE %:search% ) ",
		    countQuery = "SELECT COUNT(*) FROM computer c LEFT JOIN company cp ON c.company_id=cp.id " + 
		    		"WHERE ( c.name LIKE %:search% OR cp.name LIKE %:search% )",
		    nativeQuery = true)
	public Page<Computer> findAllByNameContains(@Param("search") String search, Pageable pageable);
	
	@Transactional
	public Long deleteByCompanyId(int companyId);

	public Computer findTopByOrderByIdDesc();
}
