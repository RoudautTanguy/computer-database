package com.excilys.cdb.persistence;


import java.sql.Connection;
import java.util.List;

import com.excilys.cdb.exception.CompanyNotFoundException;

public abstract class DAO<T> {
  protected Connection connection = null;
   
  public DAO(Connection conn){
    this.connection = conn;
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
}