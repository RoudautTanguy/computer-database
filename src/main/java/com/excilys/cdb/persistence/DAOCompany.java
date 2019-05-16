package com.excilys.cdb.persistence;

import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidCompanyException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.HikariConnectionProvider;
import com.excilys.cdb.mapper.CompanyRowMapper;
import com.excilys.cdb.model.Company;

@Repository
public class DAOCompany {

	public static final String COUNT = "SELECT COUNT(*) AS count FROM company;";
	public static final String DELETE = "DELETE FROM company WHERE company.id = :id";
	public static final String DELETE_COMPUTERS_BY_COMPANY_ID = "DELETE FROM computer WHERE computer.company_id = :id";
	public static final String INSERT = "INSERT into company (name) values (:name)";
	public static final String LAST_COMPANY_ID = "SELECT MAX(id) AS id FROM company;";
	public static final String SELECT_ALL = "SELECT company.id, company.name FROM company;";
	public static final String SELECT_ALL_PAGINATED = "SELECT company.id, company.name FROM company LIMIT :offset,:limit;";
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private static final Logger logger = LoggerFactory.getLogger(DAOCompany.class);

	public DAOCompany(HikariConnectionProvider hikariConnectionProvider) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariConnectionProvider.getDataSource());
	}
	
	/**
	 * Count companies
	 * @return the count
	 */
	public int countCompanies() {
		return namedParameterJdbcTemplate.queryForObject(COUNT, new MapSqlParameterSource(), Integer.class);
	}
	
	/**
	 * Delete company and associated computers
	 * @param id of the company
	 * @throws CompanyNotFoundException if company is not found
	 */
	@Transactional
	public void deleteCompany(int id) throws CompanyNotFoundException {
		if(id<0) {
			throw new CompanyNotFoundException("The company "+id+" doesn't exist.");
		}
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id,Types.INTEGER);
		
		namedParameterJdbcTemplate.update(DELETE_COMPUTERS_BY_COMPANY_ID, params);
		if(namedParameterJdbcTemplate.update(DELETE, params)==0) {
			throw new CompanyNotFoundException("The company "+id+" doesn't exist.");
		} 

	}

	/**
	 * Insert company
	 * @param name of the company
	 * @throws NotAValidCompanyException if the name is null or empty
	 */
	public void insertCompany(String name) throws NotAValidCompanyException {
		if(name == null || name.equals("")) {
			throw new NotAValidCompanyException(Constant.NAME_IS_MANDATORY);
		} else {
			try{
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("name", name,Types.VARCHAR);
				namedParameterJdbcTemplate.update(INSERT, params);
			} catch (DuplicateKeyException e) {
				logger.error("Company {} already exist : {}", name ,e);
			}
		}
	}
	
	/**
	 * Get the last company id
	 * @return id of the last company in database
	 */
	public int getLastCompanyId() {
		return namedParameterJdbcTemplate.queryForObject(LAST_COMPANY_ID, new MapSqlParameterSource(), Integer.class);
	}

	/**
	 * List companies
	 * @return a list of companies
	 */
	public List<Company> list() {
		RowMapper<Company> rowMapper = new CompanyRowMapper();
		return namedParameterJdbcTemplate.query(SELECT_ALL, rowMapper);
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
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("offset", offset,Types.INTEGER);
		params.addValue("limit", limit,Types.INTEGER);
		List<Company> companies = namedParameterJdbcTemplate.query(SELECT_ALL_PAGINATED, params, rowMapper);
		if(companies.isEmpty()) {
			throw new PageNotFoundException("This page doesn't exist");
		} else {
			return companies;
		}
	}

}
