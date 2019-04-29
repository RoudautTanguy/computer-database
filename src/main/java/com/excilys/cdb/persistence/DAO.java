package com.excilys.cdb.persistence;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;

public abstract class DAO<T> {
	private String url;
	private String user;
	private String password;

	private static final String configFileName = "config.properties";
	private static final Logger logger = LoggerFactory.getLogger(DAO.class);
	
	DAO(){
		try {
			InputStream input = new FileInputStream(configFileName);
			logger.info("Config file loaded ");
			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			String url = prop.getProperty("db.url");
			String param = prop.getProperty("db.param");
			String user = prop.getProperty("db.user");
			String password = prop.getProperty("db.password");
			this.setUrl(url + param);
			this.setUser(user);
			this.setPassword(password);
		} catch (IOException ex) {
			logger.error("Config file {} doesn't exist", configFileName);
			ex.printStackTrace();
		}
	}


	/**
	 * Insert object
	 * @param obj
	 * @return boolean 
	 * @throws CompanyNotFoundException 
	 * @throws NotAValidComputerException 
	 */
	public abstract boolean insert(T obj) throws NotAValidComputerException;

	/**
	 * Delete object
	 * @param index of object to delete
	 * @return boolean 
	 */
	public abstract boolean delete(int index);

	/**
	 * Update object
	 * @param obj
	 * @return boolean
	 * @throws NotAValidComputerException 
	 */
	public abstract boolean update(int id, T obj) throws NotAValidComputerException;

	/**
	 * List objects
	 * @return List<T>
	 */
	public abstract List<T> list();
	
	/**
	 * List objects with pagination
	 * @param limit
	 * @param index
	 * @return List<T>
	 * @throws PageNotFoundException 
	 */
	public abstract List<T> list(int limit, int index) throws PageNotFoundException;
	
	/**
	 * Count the number of <T> in the base
	 * @return
	 */
	public abstract int count();


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

}