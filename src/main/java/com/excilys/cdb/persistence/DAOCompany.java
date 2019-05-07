package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidCompanyException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Company;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DAOCompany {
	
	public final static String INSERT = "INSERT into company (name) values (?)";
	public final static String SELECT_ALL = "SELECT * FROM company;";
	public final static String SELECT_ALL_PAGINATED = "SELECT * FROM company LIMIT ?,?;";
	public final static String COUNT = "SELECT COUNT(*) AS count FROM company;";
	public final static String DELETE = "DELETE FROM company WHERE company.id = ?";
	public final static String DELETE_COMPUTERS_BY_COMPANY_ID = "DELETE FROM computer WHERE computer.company_id = ?";
	public static final String LAST_COMPANY_ID = "SELECT MAX(id) AS id FROM company;";
	
	private HikariConfig config = new HikariConfig("/config.properties");
    private HikariDataSource ds = new HikariDataSource( config );

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	private static final Logger logger = LoggerFactory.getLogger(DAOCompany.class);
	
	private static DAOCompany instance;

	public static DAOCompany getInstance() {
		if(instance == null) {
			instance = new DAOCompany();
		}
		return instance;
	}

	public void insert(String name) throws NotAValidCompanyException, SQLException {
		try(Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT)){
			if(name == null || name.equals("")) {
				throw new NotAValidCompanyException("The name is Mandatory.");
			} else {
				statement.setString(1, name);
				statement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void delete(int id) throws CantConnectException, CompanyNotFoundException {
		try(Connection connection = this.getConnection();
				PreparedStatement deleteComputersStatement = connection.prepareStatement(DELETE_COMPUTERS_BY_COMPANY_ID);
				PreparedStatement deleteCompanyStatement = connection.prepareStatement(DELETE)){
			try{
				connection.setAutoCommit(false);
				deleteComputersStatement.setInt(1, id);
				deleteComputersStatement.executeUpdate();

				deleteCompanyStatement.setInt(1, id);
				if(deleteCompanyStatement.executeUpdate()==0) {
					connection.rollback();
					throw new CompanyNotFoundException("The company "+id+" doesn't exist.");
				} else {
					connection.commit();
				}
			} catch(SQLException e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new CantConnectException("Can't connect.");
		}
	}

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
	
	public int getLastCompanyId() {
		int lastId = 0;
		try(Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(LAST_COMPANY_ID)){
			ResultSet resultat = statement.executeQuery();

			if(resultat.next()) {
				lastId =  resultat.getInt("id");
			}
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return lastId;
	}

}
