package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;

public class DAOComputer extends DAO<Computer> {
	
	public static final  String INSERT = "INSERT into computer (name, introduced, discontinued, company_id)"
	        + " values (?, ?, ?, ?)";
	public static final String DELETE = "DELETE FROM computer where id = ?";
	public static final String UPDATE = "UPDATE computer set name = ?, introduced = ?, discontinued = ?, company_id = ? where id = ?";
	public static final String SELECT_ALL = "SELECT * FROM computer;";
	public static final String SELECT_ALL_PAGINATED = "SELECT * FROM computer LIMIT ?,?;";
	public static final String SELECT_ALL_WITH_NAMES_PAGINATED = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id=company.id LIMIT ?,?;";
	public static final String SELECT_BY_ID = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id = ? ;";
	public static final String COUNT = "SELECT COUNT(*) AS count FROM computer;";
	public static final String LAST_COMPUTER_ID = "SELECT MAX(id) AS id FROM computer;";
	
	private MapperComputer mapperComputer = MapperComputer.getInstance();
	
	private static final Logger logger = LoggerFactory.getLogger(DAOComputer.class);
	
	private static DAOComputer instance;
	
	public static DAOComputer getInstance() {
		if(instance == null) {
			instance = new DAOComputer();
		}
		return instance;
	}
	@Override
	public boolean insert(Computer computer) throws NotAValidComputerException, CantConnectException {
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT)){
			if(computer.getName()==null) {
				String message = "Name is mandatory to insert a new Computer";
				logger.warn(message);
				throw new NotAValidComputerException(message);
			}
		    statement.setString(1, computer.getName());
		    if(computer.getIntroduced()==null) {
		    	statement.setNull(2, java.sql.Types.TIMESTAMP);
		    } else {
		    	statement.setTimestamp(2, computer.getIntroduced());		    	
		    }
		    if(computer.getDiscontinued()==null) {
		    	statement.setNull(3, java.sql.Types.TIMESTAMP);
		    } else {
		    	statement.setTimestamp(3, computer.getDiscontinued());	    	
		    }
		    
		    if(computer.getCompanyId()==null || computer.getCompanyId()==0) {
		    	statement.setNull(4, java.sql.Types.INTEGER);
		    } else {
			    statement.setInt(4, computer.getCompanyId());		    	
		    }
		    statement.execute();
		    return true;
		} catch (SQLIntegrityConstraintViolationException e) {
			logger.error("Trying to insert a computer with an inexisting computer id");
			throw new NotAValidComputerException("The company "+computer.getCompanyId() + " doesn't exist !");
		} catch (SQLException e) {
			throw new CantConnectException("Can't connect.");
		} 
	}
	
	@Override
	public void delete(int id) throws CantConnectException, ComputerNotFoundException{
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE)){
			statement.setInt(1, id);

		    // execute the preparedstatement
		    int affectedRows = statement.executeUpdate();
		    if(affectedRows == 0) {
		    	logger.warn("0 computer deleted");
		    	throw new ComputerNotFoundException("The computer " + id +" doesn't exist");
		    }
		    logger.info("Computer {} deleted", id);
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
			throw new CantConnectException("Can't connect.");
		}
	}

	@Override
	public void update(int id, Computer computer) throws NotAValidComputerException, ComputerNotFoundException, CantConnectException{
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(UPDATE)){
			if(computer.getName()==null) {
				String message = "Name is mandatory to insert a new Computer";
				logger.warn(message);
				throw new NotAValidComputerException(message);
			}
			statement.setString(1, computer.getName());
			if(computer.getIntroduced()==null) {
		    	statement.setNull(2, java.sql.Types.TIMESTAMP);
		    } else {
		    	statement.setTimestamp(2, computer.getIntroduced());		    	
		    }
		    if(computer.getDiscontinued()==null) {
		    	statement.setNull(3, java.sql.Types.TIMESTAMP);
		    } else {
		    	statement.setTimestamp(3, computer.getDiscontinued());	    	
		    }
		    if(computer.getCompanyId()==null || computer.getCompanyId() == 0) {
		    	statement.setNull(4, java.sql.Types.INTEGER);
		    } else {
			    statement.setInt(4, computer.getCompanyId());		    	
		    }
		    statement.setInt(5, id);
		    // execute the java preparedstatement
		    int affectedRows = statement.executeUpdate();
		    if(affectedRows == 0) {
		    	logger.warn("0 computer updated");
		    	throw new ComputerNotFoundException("The computer " + id +" doesn't exist");
		    }
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
			throw new CantConnectException("Can't connect.");
		}
	}

	@Override
	public List<Computer> list() {
		List<Computer> computers = new ArrayList<Computer>();
		
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
			ResultSet resultat = statement.executeQuery( );
			while ( resultat.next() ) {
			    int id = resultat.getInt( "id" );
			    String name = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int companyId = resultat.getInt("company_id");
			    computers.add(new Computer.ComputerBuilder(name)
			    						  .withId(id)
			    						  .withIntroduced(introduced)
			    						  .withDiscontinued(discontinued)
			    						  .withCompanyId(companyId)
			    						  .build());
			}
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return computers;
	}
	
	@Override
	public List<Computer> list(int index, int limit) throws PageNotFoundException{
		if(index < 0) {
			logger.error("Trying to access a negative page");
			throw new PageNotFoundException("This page doesn't exist"); 
		} else if(limit < 0) {
			logger.error("Trying to access a page with negative limit");
			throw new PageNotFoundException("This page doesn't exist"); 
		}
		int offset = index * limit;
		List<Computer> computers = new ArrayList<Computer>();
		
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PAGINATED)){
			statement.setInt(1, offset);
			statement.setInt(2, limit);
			ResultSet resultat = statement.executeQuery( );
			if (!resultat.isBeforeFirst() ) {   
				logger.error("Query have no result, trying to access a page that doesn't exist");
			    throw new PageNotFoundException("This page doesn't exist"); 
			} 
			while ( resultat.next() ) {
			    int id = resultat.getInt( "id" );
			    String name = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int companyId = resultat.getInt("company_id");
			    computers.add(new Computer.ComputerBuilder(name)
						  				  .withId(id)
						  				  .withIntroduced(introduced)
						  				  .withDiscontinued(discontinued)
						  				  .withCompanyId(companyId)
						  				  .build());
			}
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return computers;
	}
	
	public List<DTOComputer> listWithNames(int index, int limit) throws PageNotFoundException{
		if(index < 0) {
			logger.error("Trying to access a negative page");
			throw new PageNotFoundException("This page doesn't exist"); 
		} else if(limit < 0) {
			logger.error("Trying to access a page with negative limit");
			throw new PageNotFoundException("This page doesn't exist"); 
		}
		int offset = index * limit;
		List<DTOComputer> computers = new ArrayList<DTOComputer>();
		
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL_WITH_NAMES_PAGINATED)){
			statement.setInt(1, offset);
			statement.setInt(2, limit);
			ResultSet resultat = statement.executeQuery( );
			if (!resultat.isBeforeFirst() ) {   
				logger.error("Query have no result, trying to access a page that doesn't exist");
			    throw new PageNotFoundException("This page doesn't exist"); 
			} 
			while ( resultat.next() ) {
			    int id = resultat.getInt( "id" );
			    String name = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int companyId = resultat.getInt("company_id");
			    String companyName = resultat.getString("company_name");
			    DTOComputer computer = mapperComputer.modelToDTO(new Computer.ComputerBuilder(name)
						  													 .withId(id)
						  													 .withIntroduced(introduced)
						  													 .withDiscontinued(discontinued)
						  													 .withCompanyId(companyId)
						  													 .build());
			    if(companyName != null) {
			    	logger.info("Replacing company id with company name");
			    	computer.setCompany(companyName);
			    } else {
			    	logger.info("Replacing company id with null company name");
			    	computer.setCompany("");
			    }
			    computers.add(computer);
			}
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
		}
		return computers;
	}
	
	/**
	 * Find object with his id
	 * @param id
	 * @return the object
	 * @throws ComputerNotFoundException
	 */
	public DTOComputer find(int idComputer) throws ComputerNotFoundException{
		try(Connection connection = this.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)){
			statement.setString(1, Integer.toString(idComputer));
			ResultSet resultat = statement.executeQuery();
			if(resultat.next()) {
				int id = resultat.getInt( "id" );
			    String name = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int companyId = resultat.getInt("company_id");
			    String companyName = resultat.getString("company_name");
			    DTOComputer computer = mapperComputer.modelToDTO(new Computer.ComputerBuilder(name)
							 .withId(id)
							 .withIntroduced(introduced)
							 .withDiscontinued(discontinued)
							 .withCompanyId(companyId)
							 .build());
			    if(companyName != null) {
			    	logger.info("Replacing company id with company name");
			    	computer.setCompany(companyName);
			    }
				return computer;
			} else {
				String message = "Computer "+idComputer+" is not found !";
				logger.warn(message);
				throw new ComputerNotFoundException(message);
			}
		} catch (SQLException e) {
			logger.trace("Can't connect. ",e);
			throw new ComputerNotFoundException("Computer "+idComputer+" is not found !");
		}
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
	
	public int getLastComputerId() {
		int lastId = 0;
		try(Connection connection = this.getConnection();
				PreparedStatement statement = connection.prepareStatement(LAST_COMPUTER_ID)){
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
