package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.model.Computer;

public class DAOComputer extends DAO<Computer> {
	
	public final String DB_NAME = "computer-database-db";
	
	
	public DAOComputer(Connection conn) {
		super(conn);
	}

	@Override
	public boolean insert(Computer computer) {
		// the mysql insert statement
	      String query = " INSERT into computer (name, introduced, discontinued, company_id)"
	        + " values (?, ?, ?, ?)";

	      // create the mysql insert preparedstatement
	      PreparedStatement statement;
		try {
			statement = connection.prepareStatement(query);
		    statement.setString(1, computer.getName());
		    if(computer.getCompanyId()==null) {
		    	statement.setNull(2, java.sql.Types.INTEGER);
		    } else {
		    	statement.setDate(2, new Date(computer.getIntroduced().getTime()));		    	
		    }
		    if(computer.getCompanyId()==null) {
		    	statement.setNull(3, java.sql.Types.INTEGER);
		    } else {
		    	statement.setDate(3, new Date(computer.getDiscontinued().getTime()));	    	
		    }
		    
		    if(computer.getCompanyId()==null) {
		    	statement.setNull(4, java.sql.Types.INTEGER);
		    } else {
			    statement.setInt(4, computer.getCompanyId());		    	
		    }


		    // execute the preparedstatement
		    statement.execute();
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
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
		    statement.execute();
		    return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(int id, Computer computer){
	      PreparedStatement preparedStmt;
		try {
			preparedStmt = connection.prepareStatement("UPDATE computer set name = ?, introduced = ?, discontinued = ?, computer_id = ? where id = ?");
			preparedStmt.setString(1, computer.getName());
			preparedStmt.setDate(2, new Date(computer.getIntroduced().getTime()));
		    preparedStmt.setDate(3, new Date(computer.getDiscontinued().getTime()));
		    preparedStmt.setInt(4, computer.getCompanyId());
		    preparedStmt.setInt(5, id);
		    // execute the java preparedstatement
		    preparedStmt.executeUpdate();
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
	
	public Computer find(int id) throws ComputerNotFoundException{
		try {
			PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM computer WHERE id = ? ;");
			statement.setString(1, Integer.toString(id));
			ResultSet resultat = statement.executeQuery();
			if(resultat.next()) {
				//int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    Timestamp introduced = resultat.getTimestamp( "introduced" );
			    Timestamp discontinued = resultat.getTimestamp( "discontinued" );
			    int idCompany = resultat.getInt("company_id");
				return new Computer(5, nameComputer, introduced, discontinued, idCompany);
			} else {
				throw new ComputerNotFoundException("Computer "+id+" is not found !");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ComputerNotFoundException("Computer "+id+" is not found !");
		}
	}
	
}
