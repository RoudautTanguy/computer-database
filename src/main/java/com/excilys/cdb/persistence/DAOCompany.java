package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Company;

public class DAOCompany extends DAO<Company> {
	
	public final static String DB_NAME = "computer-database-db";
	public final static String SELECT_ALL = "SELECT * FROM company;";
	public final static String SELECT_ALL_PAGINATED = "SELECT * FROM company LIMIT ?,?;";
	public final static String COUNT = "SELECT COUNT(*) AS count FROM company;";
	
	private static final Logger logger = LoggerFactory.getLogger(DAOCompany.class);

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
	public void delete(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(int id, Company obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Company> list() {
		List<Company> companies = new ArrayList<Company>();
		
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
			
			ResultSet resultat = statement.executeQuery( SELECT_ALL );
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    companies.add(new Company(idComputer,nameComputer));
			}
			
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return companies;
	}
	
	@Override
	public List<Company> list(int index, int limit) throws PageNotFoundException{
		if(index < 0 || limit < 0) {
			throw new PageNotFoundException("This page doesn't exist"); 
		}
		List<Company> companies = new ArrayList<Company>();
		int offset = index * limit;
		
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PAGINATED)){
			statement.setInt(1, offset);
			statement.setInt(2, limit);
			ResultSet resultat = statement.executeQuery( );
			if (!resultat.isBeforeFirst()) {   
				String message = "This page doesn't exist";
				logger.error(message);
			    throw new PageNotFoundException(message); 
			} 
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    companies.add(new Company(idComputer,nameComputer));
			}
			
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return companies;
	}
	
	public int count() {
		int count = 0;
		try(Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(COUNT)){
			ResultSet resultat = statement.executeQuery();
			
			if(resultat.next()) {
				count =  resultat.getInt("count");
			}
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return count;
	}
	
}
