package com.excilys.cdb.persistence;

import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.HikariConnectionProvider;
import com.excilys.cdb.mapper.ComputerRowMapper;
import com.excilys.cdb.model.Computer;

@Repository
public class DAOComputer{

	public static final String COUNT = "SELECT COUNT(*) AS count FROM computer LEFT JOIN company ON computer.company_id=company.id "
			+ "WHERE ( computer.name LIKE :search OR company.name LIKE :search );";
	public static final String DELETE = "DELETE FROM computer where id = :id";
	public static final String INSERT = "INSERT into computer (name, introduced, discontinued, company_id) values (:name, :introduced, :discontinued, :company_id)";
	public static final String LAST_COMPUTER_ID = "SELECT MAX(id) AS id FROM computer;";
	public static final String SEARCH_WITH_NAMES_PAGINATED = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,"
			+ "company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id=company.id "
			+ "WHERE ( computer.name LIKE :search OR company.name LIKE :search ) %s LIMIT :offset,:limit;";
	public static final String SELECT_BY_ID = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,"
			+ "company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id = :id ;";
	public static final String UPDATE = "UPDATE computer set name = :name, introduced = :introduced, discontinued = :discontinued, company_id = :company_id WHERE id = :id";


	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private ComputerRowMapper rowMapper;

	private static final Logger logger = LoggerFactory.getLogger(DAOComputer.class);

	public DAOComputer(HikariConnectionProvider hikariConnectionProvider, ComputerRowMapper rowMapper) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(hikariConnectionProvider.getDataSource());
		this.rowMapper = rowMapper;
	}

	/**
	 * Count computers with search parameter
	 * @param search : if null or empty count all computers else count computers which have search in their name or company name
	 * @return the number of computers found
	 */
	public int countComputers(String search) {
		search = search==null?"":search;
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("search", "%" + search + "%", Types.VARCHAR);

		return namedParameterJdbcTemplate.queryForObject(COUNT, params, Integer.class);
	}

	/**
	 * Delete computer by id
	 * @param id of computer
	 * @throws ComputerNotFoundException if computer is not found
	 */
	public void deleteComputer(int id) throws ComputerNotFoundException{
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id, Types.INTEGER);
		int affectedRows = namedParameterJdbcTemplate.update(DELETE, params);
		if(affectedRows == 0) {
			logger.warn("0 computer deleted");
			throw new ComputerNotFoundException("The computer " + id +" doesn't exist");
		}
		logger.info("Computer {} deleted", id);
	}

	/**
	 * Insert a computer
	 * @param computer to insert
	 * @throws CompanyNotFoundException 
	 */
	public void insertComputer(Computer computer) throws CompanyNotFoundException {
		try{
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", computer.getName(), Types.VARCHAR);
			params.addValue("introduced", computer.getIntroduced(), Types.TIMESTAMP);
			params.addValue("discontinued", computer.getDiscontinued(), Types.TIMESTAMP);
			params.addValue("company_id", computer.getCompanyId()==0?null:computer.getCompanyId(), Types.INTEGER);

			namedParameterJdbcTemplate.update(INSERT, params);
		} catch (DataIntegrityViolationException e) {
			logger.error("Trying to insert a computer with an inexisting computer id : {}", computer.getCompanyId());
			throw new CompanyNotFoundException("The company "+computer.getCompanyId() + " doesn't exist !");
		} 

	}

	/**
	 * Get the id of the last computer in database
	 * @return id of the last computer in database
	 */
	public int getLastComputerId() {
		return namedParameterJdbcTemplate.queryForObject(LAST_COMPUTER_ID, new MapSqlParameterSource(), Integer.class);
	}

	/**
	 * Update computer
	 * @param id of the computer to update
	 * @param computer to replace old computer data
	 * @throws NotAValidComputerException if new computer name is null or empty
	 * @throws ComputerNotFoundException if computer with this id is not found
	 */
	public void updateComputer(int id, Computer computer) throws NotAValidComputerException, ComputerNotFoundException{
		if(computer.getName()==null || computer.getName().equals("")) {
			String message = "Name is mandatory to insert a new Computer";
			logger.warn(message);
			throw new NotAValidComputerException(message);
		} else {
			try{
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("name", computer.getName(), Types.VARCHAR);
				params.addValue("introduced", computer.getIntroduced(), Types.TIMESTAMP);
				params.addValue("discontinued", computer.getDiscontinued(), Types.TIMESTAMP);
				params.addValue("company_id", computer.getCompanyId()==0?null:computer.getCompanyId(), Types.INTEGER);
				params.addValue("id", id, Types.INTEGER);

				int affectedRows = namedParameterJdbcTemplate.update(UPDATE, params);
				if(affectedRows == 0) {
					logger.warn("0 computer updated");
					throw new ComputerNotFoundException("The computer " + id +" doesn't exist");
				}
			} catch (DataIntegrityViolationException e) {
				logger.error("Trying to update a computer with an inexisting computer id");
				throw new NotAValidComputerException("The company "+computer.getCompanyId() + " doesn't exist !");
			} 
		}
	}

	/**
	 * Search computers
	 * @param index of the page
	 * @param limit 
	 * @param search : if null or empty search all computers else search computers which have search in their name or company name 
	 * @param orderBy to know the order of the query
	 * @return list of computers
	 * @throws PageNotFoundException
	 * @throws ComputerNotFoundException 
	 */
	public List<DTOComputer> search(int index, int limit, String search, OrderByEnum orderBy) throws ComputerNotFoundException, PageNotFoundException{
		checkIndexAndLimit(index, limit);
		int offset = index * limit;
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("search", "%" + search + "%", Types.VARCHAR);
		params.addValue("offset", offset, Types.INTEGER);
		params.addValue("limit", limit, Types.INTEGER);

		String query = String.format(SEARCH_WITH_NAMES_PAGINATED,orderBy.getQuery());
		List<DTOComputer> computers = namedParameterJdbcTemplate.query(query, params, rowMapper);
		if(computers.isEmpty()) {
			throw new ComputerNotFoundException("No computer found.. Try again !");
		} else {
			return computers;
		}
	}

	/**
	 * Find object with his id
	 * @param id
	 * @return the object
	 * @throws ComputerNotFoundException
	 */
	public DTOComputer find(int idComputer) throws ComputerNotFoundException{
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", idComputer, Types.INTEGER);

		List<DTOComputer> computers = namedParameterJdbcTemplate.query(SELECT_BY_ID, params, rowMapper);
		if(computers.isEmpty()) {
			throw new ComputerNotFoundException("This page doesn't exist");
		} else if(computers.size()>1){
			throw new ComputerNotFoundException("This page doesn't exist");
		} else {
			return computers.get(0);
		}
	}


	/**
	 * Check if index and limit are correct
	 * @param index of a page
	 * @param limit of a page
	 * @throws PageNotFoundException
	 */
	private void checkIndexAndLimit(int index, int limit) throws PageNotFoundException {
		if(index < 0) {
			logger.error("Trying to access a negative page");
			throw new PageNotFoundException("This page doesn't exist"); 
		} else if(limit < 0) {
			logger.error("Trying to access a page with negative limit");
			throw new PageNotFoundException("This page doesn't exist"); 
		}
	}

}
