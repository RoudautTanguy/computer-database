package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class DAO<T> {
	
	private HikariConfig config = new HikariConfig("/config.properties");
    private HikariDataSource ds = new HikariDataSource( config );

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
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
	 * @throws ComputerNotFoundException 
	 * @throws CantConnectException 
	 */
	public abstract void update(int id, T obj) throws NotAValidComputerException, ComputerNotFoundException, CantConnectException;

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

}