package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;

public class DAOComputer extends DAO<Computer> {
	
	public DAOComputer(Connection conn) {
		super(conn);
	}

	@Override
	public boolean insert(Computer computer) throws CompanyNotFoundException {
		if(!computer.validate()) {
			return false;
		}
		// the mysql insert statement
	      String query = " INSERT into computer (name, introduced, discontinued, company_id)"
	        + " values (?, ?, ?, ?)";

	      // create the mysql insert preparedstatement
	      PreparedStatement statement;
		try {
			statement = connection.prepareStatement(query);
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

		    // execute the preparedstatement
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
	      PreparedStatement statement;
		try {
			statement = connection.prepareStatement("DELETE FROM computer where id = ?");
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
	      PreparedStatement statement;
	      if(!computer.validate()) {
				return false;
			}
		try {
			statement = connection.prepareStatement("UPDATE computer set name = ?, introduced = ?, discontinued = ?, company_id = ? where id = ?");
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
		
		try {
			Statement statement = this.connection.createStatement();
			ResultSet resultat = statement.executeQuery( "SELECT * FROM computer;" );
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
		try {
			PreparedStatement statement = this.connection.prepareStatement("SELECT computer.id,computer.name,computer.introduced,computer.discontinued,computer.company_id,company.name AS company_name FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id = ? ;");
			statement.setString(1, Integer.toString(id));
			ResultSet resultat = statement.executeQuery();
			if(resultat.next()) {
				int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int companyId = resultat.getInt("company_id");
			    String companyName = resultat.getString("company_name");
			    DTOComputer computer = MapperComputer.modelToDTO(new Computer(idComputer,nameComputer,introduced,discontinued,companyId));
			    if(companyName != null) {
			    	computer.setCompany(companyName);
			    }
				return computer;
			} else {
				throw new ComputerNotFoundException("Computer "+id+" is not found !");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ComputerNotFoundException("Computer "+id+" is not found !");
		}
	}
	
}
