package com.excilys.cdb.persistence;

import java.sql.Connection;

public class DAOFactory {
	protected static final Connection connection = DBConnection.getInstance();
	
	public static DAOCompany getDAOCompany() {
		return new DAOCompany(connection);
	}
	
	public static DAOComputer getDAOComputer() {
		return new DAOComputer(connection);
	}
}
