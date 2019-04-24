package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
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
	public static final String SELECT_BY_ID = "SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,company.name AS company_name "
			+ "FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id = ? ;";
	public static final String COUNT = "SELECT COUNT(*) AS count FROM computer;";
	
	private static DAOComputer instance;
	
	public static DAOComputer getInstance() {
		if(instance == null) {
			instance = new DAOComputer();
		}
		return instance;
	}
	@Override
	public boolean insert(Computer computer) throws CompanyNotFoundException {
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(INSERT)){
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
		    
		    if(computer.getCompanyId()==null) {
		    	statement.setNull(4, java.sql.Types.INTEGER);
		    } else {
			    statement.setInt(4, computer.getCompanyId());		    	
		    }
		    statement.execute();
		    return true;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new CompanyNotFoundException("The company "+computer.getCompanyId() + " doesn't exist !");
		} catch (SQLException e) {
			return false;
		} 
	}
	
	@Override
	public boolean delete(int id){
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(DELETE)){
			statement.setInt(1, id);

		    // execute the preparedstatement
		    int affectedRows = statement.executeUpdate();
		    if(affectedRows == 0) {
		    	return false;
		    }
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(int id, Computer computer){
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(UPDATE)){
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
		    
		    if(computer.getCompanyId()==null) {
		    	statement.setNull(4, java.sql.Types.INTEGER);
		    } else {
			    statement.setInt(4, computer.getCompanyId());		    	
		    }
		    statement.setInt(5, id);
		    // execute the java preparedstatement
		    int affectedRows = statement.executeUpdate();
		    if(affectedRows == 0) {
		    	return false;
		    }
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Computer> list() {
		List<Computer> computers = new ArrayList<Computer>();
		
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
			ResultSet resultat = statement.executeQuery( );
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int idCompany = resultat.getInt("company_id");
			    computers.add(new Computer(idComputer,nameComputer,introduced ,discontinued ,idCompany));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}
	
	@Override
	public List<Computer> list(int index, int limit) throws PageNotFoundException{
		if(index < 0) {
			throw new PageNotFoundException("This page doesn't exist"); 
		}
		int offset = index * limit;
		List<Computer> computers = new ArrayList<Computer>();
		
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PAGINATED)){
			statement.setInt(1, offset);
			statement.setInt(2, limit);
			ResultSet resultat = statement.executeQuery( );
			if (!resultat.isBeforeFirst() ) {    
			    throw new PageNotFoundException("This page doesn't exist"); 
			} 
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int idCompany = resultat.getInt("company_id");
			    computers.add(new Computer(idComputer,nameComputer,introduced ,discontinued ,idCompany));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}
	
	/**
	 * Find object with his id
	 * @param id
	 * @return the object
	 * @throws ComputerNotFoundException
	 */
	public DTOComputer find(int id) throws ComputerNotFoundException{
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
			PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)){
			statement.setString(1, Integer.toString(id));
			ResultSet resultat = statement.executeQuery();
			if(resultat.next()) {
				int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int companyId = resultat.getInt("company_id");
			    String companyName = resultat.getString("company_name");
			    DTOComputer computer = MapperComputer.getInstance().modelToDTO(new Computer(idComputer,nameComputer,introduced,discontinued,companyId));
			    if(companyName != null) {
			    	computer.setCompany(companyName);
			    }
				return computer;
			} else {
				throw new ComputerNotFoundException("Computer "+id+" is not found !");
			}
		} catch (SQLException e) {
			throw new ComputerNotFoundException("Computer "+id+" is not found !");
		}
	}
	
	public int count() {
		int count = 0;
		try(Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPassword());
				PreparedStatement statement = connection.prepareStatement(COUNT)){
			ResultSet resultat = statement.executeQuery();
			
			if(resultat.next()) {
				count =  resultat.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
}
