package com.excilys.cdb.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOUser;
import com.excilys.cdb.model.User;

@Service
public class ServiceUser implements UserDetailsService{

	private DAOUser daoUser;
	
	public ServiceUser(DAOUser daoUser) {
		this.daoUser = daoUser;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> optUser = daoUser.findByUsername(username);
		if(!optUser.isPresent()) {
			throw new UsernameNotFoundException(username);
		}
		return optUser.get();
    }
}
