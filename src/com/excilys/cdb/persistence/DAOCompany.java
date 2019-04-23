package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;

public class DAOCompany extends DAO<Company> {
	
	public final static String DB_NAME = "computer-database-db";
	public final static String SELECT_ALL = "SELECT * FROM company;";

	private static DAOCompany instance;
	
	public static DAOCompany getInstance() {
		if(instance == null) {
			instance = new DAOCompany();
		}
		return instance;
	}
	
	@Override
	public boolean insert(Company obj) {
		return false;
	}

	@Override
	public boolean delete(int index) {
		return false;
	}

	@Override
	public boolean update(int id, Company obj) {
		return false;
	}

	@Override
	public List<Company> list() {
		List<Company> companies = new ArrayList<Company>();
		
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
			
			ResultSet resultat = statement.executeQuery( SELECT_ALL );
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    companies.add(new Company(idComputer,nameComputer));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}
	
}
