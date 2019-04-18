package com.excilys.cdb.persistence;

import java.sql.Connection;

public class DAOFactory {
	protected static final Connection connection = DBConnection.getInstance();
	
	/**
	 * Get a Company DAO
	 * @return
	 */
	public static DAOCompany getDAOCompany() {
		return new DAOCompany(connection);
	}
	
	/**
	 * Get a Computer DAO
	 * @return
	 */
	public static DAOComputer getDAOComputer() {
		return new DAOComputer(connection);
	}
}
