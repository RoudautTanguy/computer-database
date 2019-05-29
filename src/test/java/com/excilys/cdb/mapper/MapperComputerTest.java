package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
public class MapperComputerTest {

	@Autowired
	MapperComputer mapper;
	
	private static final String APPLE_COMPUTER = "AppleComputer";
	private Company APPLE_COMPANY = new Company(1,"Apple Inc.");

	@Test
	public void mapModelToDTOTest() {
		Computer computer = new Computer(0,APPLE_COMPUTER,null,null,null);
		DTOComputer dtoComputer = new DTOComputer(APPLE_COMPUTER);
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.mapModelToDTO(computer));
	}

	@Test
	public void mapModelToDTO2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer(0,APPLE_COMPUTER,introduced,discontinued,APPLE_COMPANY);
		DTOComputer dtoComputer = new DTOComputer(0,APPLE_COMPUTER,"10-10-2010",today.format(formatter),APPLE_COMPANY.getId(),APPLE_COMPANY.getName());
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.mapModelToDTO(computer));
	}

	@Test
	public void mapDTOToModelTest() throws CompanyNotFoundException {
		Computer computer = new Computer(0,APPLE_COMPUTER,null,null,null);
		DTOComputer dtoComputer = new DTOComputer(APPLE_COMPUTER);
		assertEquals("Mapped Computer isn't equal to expected Computer",computer,mapper.mapDTOToModel(dtoComputer));
	}

	@Test
	public void mapDTOToModel2Test() throws CompanyNotFoundException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer(0,APPLE_COMPUTER,introduced,discontinued,APPLE_COMPANY);
		DTOComputer dtoComputer = new DTOComputer(0,APPLE_COMPUTER,"10-10-2010",today.format(formatter),APPLE_COMPANY.getId(),APPLE_COMPANY.getName());
		assertEquals("DTOComputer isn't equal to expected Computer",computer,mapper.mapDTOToModel(dtoComputer));
	}
}
