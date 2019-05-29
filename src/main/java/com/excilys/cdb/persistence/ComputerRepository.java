package com.excilys.cdb.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;

@Repository
public interface ComputerRepository extends PagingAndSortingRepository<Computer, Integer> {
 
    Computer findById(int id);
 
    @Query(value = "SELECT COUNT(*) FROM computer LEFT JOIN company ON computer.company_id=company.id " + 
    		"WHERE ( computer.name LIKE :search OR company.name LIKE :search )", nativeQuery = true)
    int countByNameContainingAndCompanyContaining(@Param("search") String search);
 
}