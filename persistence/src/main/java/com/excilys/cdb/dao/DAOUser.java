package com.excilys.cdb.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.excilys.cdb.model.User;

public interface DAOUser  extends CrudRepository<User, Long>{

	Optional<User> findByUsername(String username);
	
	public Long deleteById(int id);
}
