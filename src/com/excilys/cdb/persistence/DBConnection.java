package com.excilys.cdb.persistence;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	private static Connection connection;
	
	private DBConnection() {
		try {
			InputStream input = new FileInputStream("config.properties");

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            String url = prop.getProperty("db.url");
            String param = prop.getProperty("db.param");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

	        
			connection = DriverManager.getConnection(url+param, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public static Connection getInstance(){
		if(connection == null){
			new DBConnection();
	    }
	    	return connection;   
		}  
	
}
