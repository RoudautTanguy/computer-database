package com.excilys.cdb.main;

import com.excilys.cdb.ui.CLI;

public class Main {
	public static void main(String[] args) {
		/* Chargement du driver JDBC pour MySQL */
		try {
		    Class.forName( "com.mysql.cj.jdbc.Driver" );
		    CLI cli = new CLI();
		    cli.welcome();
		    cli.startChoice();
		} catch ( ClassNotFoundException e ) {
		    e.printStackTrace();
		}
	}

}
