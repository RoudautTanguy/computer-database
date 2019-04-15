package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.util.List;

import com.excilys.cdb.model.Company;

public class DAOCompany extends DAO<Company> {
	
	public final String DB_NAME = "computer-database-db";

	public DAOCompany(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Company obj) {
		return false;
	}

	@Override
	public boolean delete(Company obj) {
		return false;
	}

	@Override
	public boolean update(Company obj) {
		return false;
	}

	@Override
	public List<Company> list() {
		return null;
	}
	
}
