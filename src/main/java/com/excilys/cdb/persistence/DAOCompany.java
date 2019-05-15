package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidCompanyException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.HikariConnectionProvider;
import com.excilys.cdb.model.Company;

@Repository
public class DAOCompany {
	
	public static final String INSERT = "INSERT into company (name) values (?)";
	public static final String SELECT_ALL = "SELECT * FROM company;";
	public static final String SELECT_ALL_PAGINATED = "SELECT * FROM company LIMIT ?,?;";
	public static final String COUNT = "SELECT COUNT(*) AS count FROM company;";
	public static final String DELETE = "DELETE FROM company WHERE company.id = ?";
	public static final String DELETE_COMPUTERS_BY_COMPANY_ID = "DELETE FROM computer WHERE computer.company_id = ?";
	public static final String LAST_COMPANY_ID = "SELECT MAX(id) AS id FROM company;";
	
	private HikariConnectionProvider hikariConnectionProvider;

	private static final Logger logger = LoggerFactory.getLogger(DAOCompany.class);
	
	public DAOCompany(HikariConnectionProvider hikariConnectionProvider) {
		this.hikariConnectionProvider = hikariConnectionProvider;
	}

	public void insertCompany(String name) throws NotAValidCompanyException {
		try(Connection connection = hikariConnectionProvider.getDs().getConnection();
				PreparedStatement statement = connection.prepareStatement(INSERT)){
			if(name == null || name.equals("")) {
				throw new NotAValidCompanyException(Constant.NAME_IS_MANDATORY);
			} else {
				statement.setString(1, name);
				statement.execute();
			}
		} catch (SQLException e) {
			logger.error("SQL Exception",e);
		}
	}

	public void deleteCompany(int id) throws CantConnectException, CompanyNotFoundException {
		try(Connection connection = hikariConnectionProvider.getDs().getConnection();
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
			logger.error(Constant.CANT_CONNECT,e);
			throw new CantConnectException(Constant.CANT_CONNECT);
		}
	}

	public List<Company> list() {
		List<Company> companies = new ArrayList<>();

		try(Connection connection = hikariConnectionProvider.getDs().getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
				ResultSet resultat = statement.executeQuery();){
			while ( resultat.next() ) {
				int idComputer = resultat.getInt( "id" );
				String nameComputer = resultat.getString( "name" );
				companies.add(new Company(idComputer,nameComputer));
			}

		} catch (SQLException e) {
			logger.trace(Constant.CANT_CONNECT,e);
		} 
		return companies;
	}

	public List<Company> list(int index, int limit) throws PageNotFoundException{
		if(index < 0 || limit < 0) {
			throw new PageNotFoundException(Constant.PAGE_DOESNT_EXIST); 
		}
		List<Company> companies = new ArrayList<>();
		int offset = index * limit;
		ResultSet resultat = null;
		try(Connection connection = hikariConnectionProvider.getDs().getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PAGINATED)){
			statement.setInt(1, offset);
			statement.setInt(2, limit);
			resultat = statement.executeQuery( );
			if (!resultat.isBeforeFirst()) {   
				logger.error(Constant.PAGE_DOESNT_EXIST);
				throw new PageNotFoundException(Constant.PAGE_DOESNT_EXIST); 
			} 
			while ( resultat.next() ) {
				int idComputer = resultat.getInt( "id" );
				String nameComputer = resultat.getString( "name" );
				companies.add(new Company(idComputer,nameComputer));
			}

		} catch (SQLException e) {
			logger.trace(Constant.CANT_CONNECT,e);
		} finally {
			if(resultat != null) {
				try {
					resultat.close();
				} catch (SQLException e) {
					logger.error(Constant.CANT_CLOSE_RESULT_SET);
				}
			}
		}
		return companies;
	}

	public int countCompanies() {
		int count = 0;
		try(Connection connection = hikariConnectionProvider.getDs().getConnection();
				PreparedStatement statement = connection.prepareStatement(COUNT);
				ResultSet resultat = statement.executeQuery();){

			if(resultat.next()) {
				count =  resultat.getInt("count");
			}
		} catch (SQLException e) {
			logger.trace(Constant.CANT_CONNECT,e);
		}
		return count;
	}
	
	public int getLastCompanyId() {
		int lastId = 0;
		try(Connection connection = hikariConnectionProvider.getDs().getConnection();
				PreparedStatement statement = connection.prepareStatement(LAST_COMPANY_ID);
				ResultSet resultat = statement.executeQuery();){

			if(resultat.next()) {
				lastId =  resultat.getInt("id");
			}
		} catch (SQLException e) {
			logger.trace(Constant.CANT_CONNECT,e);
		}
		return lastId;
	}

}
