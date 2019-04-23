package com.excilys.cdb.persistence;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.excilys.cdb.exception.CompanyNotFoundException;

public abstract class DAO<T> {
	private String url;
	private String user;
	private String password;
	
	DAO(){
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
	        this.setUrl(url + param);
	        this.setUser(user);
	        this.setPassword(password);
		} catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

   
  /**
  * Insert object
  * @param obj
  * @return boolean 
  * @throws CompanyNotFoundException 
  */
  public abstract boolean insert(T obj) throws CompanyNotFoundException;

  /**
   * Delete object
   * @param index of object to delete
   * @return boolean 
   */
   public abstract boolean delete(int index);

  /**
  * Update object
  * @param obj
  * @return boolean
  */
  public abstract boolean update(int id, T obj);

  /**
  * List objects
  * @return List<T>
  */
  public abstract List<T> list();


public String getPassword() {
	return password;
}


public void setPassword(String password) {
	this.password = password;
}


public String getUser() {
	return user;
}


public void setUser(String user) {
	this.user = user;
}


public String getUrl() {
	return url;
}


public void setUrl(String url) {
	this.url = url;
}
}