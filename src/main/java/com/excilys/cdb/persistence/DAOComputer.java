package com.excilys.cdb.persistence;

import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.HikariConnectionProvider;
import com.excilys.cdb.mapper.ComputerRowMapper;
import com.excilys.cdb.model.Computer;

@Repository
public class DAOComputer{

	public static final  String INSERT = "INSERT into computer (name, introduced, discontinued, company_id) values (?, ?, ?, ?)";
	public static final String DELETE = "DELETE FROM computer where id = ?";
	public static final String UPDATE = "UPDATE computer set name = ?, introduced = ?, discontinued = ?, company_id = ? where id = ?";
	public static final String SEARCH_WITH_NAMES_PAGINATED = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE ( computer.name LIKE ? OR company.name LIKE ? ) %s LIMIT ?,?;";
	public static final String SELECT_BY_ID = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id = ? ;";
	public static final String COUNT = "SELECT COUNT(*) AS count FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE ( computer.name LIKE ? OR company.name LIKE ? );";
	public static final String LAST_COMPUTER_ID = "SELECT MAX(id) AS id FROM computer;";

	private JdbcTemplate jdbcTemplate;
	private ComputerRowMapper rowMapper;

	private static final Logger logger = LoggerFactory.getLogger(DAOComputer.class);

	public DAOComputer(HikariConnectionProvider hikariConnectionProvider, ComputerRowMapper rowMapper) {
		this.jdbcTemplate = new JdbcTemplate(hikariConnectionProvider.getDs());
		this.rowMapper = rowMapper;
	}

	public void insertComputer(Computer computer) throws NotAValidComputerException {
		if(computer.getName()==null) {
			String message = "Name is mandatory to insert a new Computer";
			logger.warn(message);
			throw new NotAValidComputerException(message);
		} else {
			try{
				Object[] params = {
						new SqlParameterValue(Types.VARCHAR, computer.getName()),
						new SqlParameterValue(Types.TIMESTAMP, computer.getIntroduced()),
						new SqlParameterValue(Types.TIMESTAMP, computer.getDiscontinued()),
						new SqlParameterValue(Types.INTEGER, computer.getCompanyId()),
				};
				jdbcTemplate.update(INSERT, params);
			} catch (DataIntegrityViolationException e) {
				logger.error("Trying to insert a computer with an inexisting computer id");
				throw new NotAValidComputerException("The company "+computer.getCompanyId() + " doesn't exist !");
			} 
		}

	}

	public void deleteComputer(int id) throws ComputerNotFoundException{
		Object[] params = {
				new SqlParameterValue(Types.INTEGER, id)
		};
		int affectedRows = jdbcTemplate.update(DELETE, params);
		if(affectedRows == 0) {
			logger.warn("0 computer deleted");
			throw new ComputerNotFoundException("The computer " + id +" doesn't exist");
		}
		logger.info("Computer {} deleted", id);
	}

	public void updateComputer(int id, Computer computer) throws NotAValidComputerException, ComputerNotFoundException{
		if(computer.getName()==null) {
			String message = "Name is mandatory to insert a new Computer";
			logger.warn(message);
			throw new NotAValidComputerException(message);
		} else {
			try{
				Object[] params = {
						new SqlParameterValue(Types.VARCHAR, computer.getName()),
						new SqlParameterValue(Types.TIMESTAMP, computer.getIntroduced()),
						new SqlParameterValue(Types.TIMESTAMP, computer.getDiscontinued()),
						new SqlParameterValue(Types.INTEGER, computer.getCompanyId()),
						new SqlParameterValue(Types.INTEGER, id)
				};
				int affectedRows = jdbcTemplate.update(UPDATE, params);
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

	public List<DTOComputer> search(int index, int limit, String search, OrderByEnum orderBy) throws PageNotFoundException{
		checkIndexAndLimit(index, limit);
		int offset = index * limit;
		Object[] params = {
				new SqlParameterValue(Types.VARCHAR, "%" + search + "%"),
				new SqlParameterValue(Types.VARCHAR, "%" + search + "%"),
				new SqlParameterValue(Types.INTEGER, offset),
				new SqlParameterValue(Types.INTEGER, limit)
		};
		String query = String.format(SEARCH_WITH_NAMES_PAGINATED,orderBy.getQuery());
		List<DTOComputer> computers = jdbcTemplate.query(query, rowMapper, params);
		if(computers.isEmpty()) {
			throw new PageNotFoundException("This page doesn't exist");
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
		Object[] params = {
				new SqlParameterValue(Types.INTEGER, idComputer)
		};
		List<DTOComputer> computers = jdbcTemplate.query(SELECT_BY_ID, rowMapper, params);
		if(computers.isEmpty()) {
			throw new ComputerNotFoundException("This page doesn't exist");
		} else if(computers.size()>1){
			throw new ComputerNotFoundException("This page doesn't exist");
		} else {
			return computers.get(0);
		}
	}

	public int countComputers(String search) {
		Object[] params = {
				new SqlParameterValue(Types.VARCHAR, "%"+search+"%"),
				new SqlParameterValue(Types.VARCHAR, "%"+search+"%"),
		};
		return jdbcTemplate.queryForObject(COUNT, params, Integer.class);
	}

	public int getLastComputerId() {
		return jdbcTemplate.queryForObject(LAST_COMPUTER_ID, Integer.class);
	}

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
