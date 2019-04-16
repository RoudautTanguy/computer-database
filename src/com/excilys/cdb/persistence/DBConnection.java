package com.excilys.cdb.persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public final String URL = "jdbc:mysql://localhost:3306/computer-database-db";
	public String param = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public final String user = "admincdb";
	public final String password = "qwerty1234";
	
	private static Connection connection;
	
	private DBConnection() {
		try {
			connection = DriverManager.getConnection(URL+param, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getInstance(){
		if(connection == null){
			new DBConnection();
	    }
	    	return connection;   
		}  
	
}
