package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidCompanyException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.HikariConnectionProvider;
import com.excilys.cdb.mapper.CompanyRowMapper;
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
	private JdbcTemplate jdbcTemplate;

	private static final Logger logger = LoggerFactory.getLogger(DAOCompany.class);

	public DAOCompany(HikariConnectionProvider hikariConnectionProvider) {
		this.hikariConnectionProvider = hikariConnectionProvider;
		this.jdbcTemplate = new JdbcTemplate(hikariConnectionProvider.getDs());
	}

	public void insertCompany(String name) throws NotAValidCompanyException {
		if(name == null || name.equals("")) {
			throw new NotAValidCompanyException(Constant.NAME_IS_MANDATORY);
		} else {
			try{
				Object[] params = {
						new SqlParameterValue(Types.VARCHAR, name)
				};
				jdbcTemplate.update(INSERT, params);
			} catch (DuplicateKeyException e) {
				logger.error("Company {} already exist : {}", name ,e);
			}
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

	/**
	 * List companies
	 * @return a list of companies
	 */
	public List<Company> list() {
		RowMapper<Company> rowMapper = new CompanyRowMapper();
		return jdbcTemplate.query(SELECT_ALL, rowMapper);
	}

	/**
	 * List companies with an page index and a limit
	 * @param index of a page
	 * @param limit 
	 * @return a list of companies
	 * @throws PageNotFoundException
	 */
	public List<Company> list(int index, int limit) throws PageNotFoundException{
		if(index < 0 || limit < 0) {
			throw new PageNotFoundException(Constant.PAGE_DOESNT_EXIST); 
		}
		int offset = index * limit;
		RowMapper<Company> rowMapper = new CompanyRowMapper();
		Object[] params = {
				new SqlParameterValue(Types.INTEGER, offset),
				new SqlParameterValue(Types.INTEGER, limit)
		};
		List<Company> companies = jdbcTemplate.query(SELECT_ALL_PAGINATED, rowMapper, params);
		if(companies.isEmpty()) {
			throw new PageNotFoundException("This page doesn't exist");
		} else {
			return companies;
		}
	}

	/**
	 * Count companies
	 * @return the count
	 */
	public int countCompanies() {
		return jdbcTemplate.queryForObject(COUNT, Integer.class);
	}

	/**
	 * Get the last company id
	 * @return id of the last company in database
	 */
	public int getLastCompanyId() {
		return jdbcTemplate.queryForObject(LAST_COMPANY_ID, Integer.class);
	}

}
