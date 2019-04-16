package com.excilys.cdb.main;

import java.sql.SQLException;
import com.excilys.cdb.persistence.DBConnection;
import com.excilys.cdb.ui.CLI;

public class Main {
	
	public static void main(String[] args) {
		/* Chargement du driver JDBC pour MySQL */
		try {
		    Class.forName( "com.mysql.cj.jdbc.Driver" );
		    CLI.getInstance().welcome();
		    CLI.getInstance().startChoice();
		} catch ( ClassNotFoundException e ) {
		    e.printStackTrace();
		} finally {
			try {
				DBConnection.getInstance().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}
