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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.UserNotFoundException;
import com.excilys.cdb.exception.UsernameAlreadyExistException;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.ServiceUser;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/")
public class UserRestController {

	private ServiceUser serviceUser;
	
	public UserRestController(ServiceUser serviceUser) {
		this.serviceUser = serviceUser;
	}
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public List<User> findAll(){
		return serviceUser.getUsers();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User findById(@PathVariable("id") int id) throws UserNotFoundException {
		return serviceUser.getUserById(id);
	}
	
	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody User user) throws UsernameAlreadyExistException {
		this.serviceUser.registerNewUserAccount(user);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable("id") Long id, @RequestBody User user) throws ConcurentConflictException, UserNotFoundException {
		this.serviceUser.update(id, user);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) throws UserNotFoundException {
		this.serviceUser.delete(id);
	}
}
