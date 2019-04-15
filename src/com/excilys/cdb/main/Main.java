package com.excilys.cdb.main;

import java.util.List;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DAOComputer;
import com.excilys.cdb.persistence.DBConnection;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Hello world !");
		/* Chargement du driver JDBC pour MySQL */
		try {
		    Class.forName( "com.mysql.cj.jdbc.Driver" );
		    System.out.println("OK");
		    DAOComputer DaoComputer = new DAOComputer(DBConnection.getInstance());
		    List<Computer> computers = DaoComputer.list();
		    for(Computer computer:computers) {
		    	System.out.println(computer);
		    }
		} catch ( ClassNotFoundException e ) {
		    /* Gérer les éventuelles erreurs ici. */
		}
	}

}
