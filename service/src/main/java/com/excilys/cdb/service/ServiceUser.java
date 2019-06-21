package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOUser;
import com.excilys.cdb.exception.UserNotFoundException;
import com.excilys.cdb.exception.UsernameAlreadyExistException;
import com.excilys.cdb.model.User;

@Service
public class ServiceUser implements UserDetailsService{

	private DAOUser daoUser;
	private PasswordEncoder passwordEncoder;
	
	private final static String USER_DOESNT_EXIST = "The user %d doesn't exist";
	
	public ServiceUser(DAOUser daoUser, @Lazy PasswordEncoder passwordEncoder) {
		this.daoUser = daoUser;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public User loadUserByUsername(String username) {
		Optional<User> optUser = daoUser.findByUsername(username);
		if(!optUser.isPresent()) {
			throw new UsernameNotFoundException(username);
		}
		return optUser.get();
    }
	
	public List<User> getUsers(){
		return (List<User>) daoUser.findAll();
	}
	
	public User getUserById(long id) throws UserNotFoundException {
		Optional<User> optUser = daoUser.findById(id);
		if(!optUser.isPresent()) {
			throw new UserNotFoundException(String.format(USER_DOESNT_EXIST, id));
		}
		return optUser.get();
	}
	
	public void registerNewUserAccount(User user) throws UsernameAlreadyExistException {
	    if (checkUsernameExist(user.getUsername())) {
	    	System.out.println(user.getUsername());
	        throw new UsernameAlreadyExistException("An account already exist with this username : " + user.getUsername());
	    }
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    daoUser.save(user);
	}
	
	public User update(Long id, User user) throws UserNotFoundException {
		if(id<0) {
			throw new UserNotFoundException("Company with negative id can't exist.");
		}
		Optional<User> optUser = daoUser.findById(id);
		if(!optUser.isPresent()) {
			throw new UserNotFoundException(String.format(USER_DOESNT_EXIST, id));
		}
		user.setId(id);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return daoUser.save(user);
	}
	
	public void delete(Long id) throws UserNotFoundException {
		if(id<0) {
			throw new UserNotFoundException("Company with negative id can't exist.");
		}
		try{
			daoUser.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new UserNotFoundException(String.format(USER_DOESNT_EXIST, id));
		}
	}
	
	private boolean checkUsernameExist(String username) {
        try {
        	loadUserByUsername(username);
        	return true;
        } catch(UsernameNotFoundException e) {
        	return false;
        }
    }
}
