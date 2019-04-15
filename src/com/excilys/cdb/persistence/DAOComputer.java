package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Computer;

public class DAOComputer extends DAO<Computer> {
	
	public final String DB_NAME = "computer-database-db";
	
	
	public DAOComputer(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Computer obj) {
		return false;
	}

	@Override
	public boolean delete(Computer obj) {
		return false;
	}

	@Override
	public boolean update(Computer obj) {
		return false;
	}

	@Override
	public List<Computer> list() {
		Statement statement;
		List<Computer> computers = new ArrayList<Computer>();
		
		try {
			statement = this.connection.createStatement();
			ResultSet resultat = statement.executeQuery( "SELECT id, name FROM computer;" );
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    computers.add(new Computer(idComputer,nameComputer,null,null,idComputer));
			    /* Traiter ici les valeurs récupérées. */
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return computers;
	}
	
}
