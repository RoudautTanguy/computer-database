package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;

public class DAOCompany extends DAO<Company> {
	
	public final String DB_NAME = "computer-database-db";

	public DAOCompany(Connection conn) {
		super(conn);
	}

	@Override
	public boolean insert(Company obj) {
		return false;
	}

	@Override
	public boolean delete(int index) {
		return false;
	}

	@Override
	public boolean update(int id, Company obj) {
		return false;
	}

	@Override
	public List<Company> list() {
		Statement statement;
		List<Company> companies = new ArrayList<Company>();
		
		try {
			statement = this.connection.createStatement();
			ResultSet resultat = statement.executeQuery( "SELECT * FROM company;" );
			while ( resultat.next() ) {
			    int idComputer = resultat.getInt( "id" );
			    String nameComputer = resultat.getString( "name" );
			    companies.add(new Company(idComputer,nameComputer));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}
	
}
