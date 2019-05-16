package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.model.Computer;

@Component
public class ComputerRowMapper implements RowMapper<DTOComputer>{
	
	private MapperComputer mapperComputer;
	
	public ComputerRowMapper(MapperComputer mapperComputer) {
		this.mapperComputer = mapperComputer;
	}

	@Override
	public DTOComputer mapRow(ResultSet resultSet, int pRowNum) throws SQLException {
		int id = resultSet.getInt( "id" );
		String name = resultSet.getString( "name" );
		Timestamp introduced = resultSet.getTimestamp( "introduced" );
		Timestamp discontinued = resultSet.getTimestamp( "discontinued" );
		int companyId = resultSet.getInt("company_id");
		String companyName = resultSet.getString("company_name");
		DTOComputer computer = mapperComputer.mapModelToDTO(new Computer.ComputerBuilder(name)
				.withId(id)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompanyId(companyId)
				.build());
		if(companyName != null) {
			computer.setCompany(companyName);
		} else {
			computer.setCompany("");
		}
		return computer;
	}
}
