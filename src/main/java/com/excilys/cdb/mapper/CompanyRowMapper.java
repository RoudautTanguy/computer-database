package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

@Component
public class CompanyRowMapper implements RowMapper<Company>{

	@Override
	public Company mapRow(ResultSet resultSet, int pRowNum) throws SQLException {
		return new Company(resultSet.getInt("id"), resultSet.getString( "name" ));
	}
}
