package com.excilys.cdb.persistence;


import java.sql.Connection;
import java.util.List;

public abstract class DAO<T> {
  protected Connection connection = null;
   
  public DAO(Connection conn){
    this.connection = conn;
  }
   
  /**
  * Méthode d'insertion
  * @param obj
  * @return boolean 
  */
  public abstract boolean insert(T obj);

  /**
   * Méthode pour effacer
   * @param index de l'objet a supprimer
   * @return boolean 
   */
   public abstract boolean delete(int index);

  /**
  * Méthode de mise à jour
  * @param obj
  * @return boolean
  */
  public abstract boolean update(int id, T obj);

  /**
  * Méthode de recherche des informations
  * @return List<T>
  */
  public abstract List<T> list();
}