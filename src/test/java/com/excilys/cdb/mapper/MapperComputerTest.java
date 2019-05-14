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

import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.model.Computer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
public class MapperComputerTest {

	@Autowired
	MapperComputer mapper;
	
	private static final String APPLE_COMPUTER = "AppleComputer";

	@Test
	public void mapModelToDTOTest() {
		Computer computer = new Computer.ComputerBuilder(APPLE_COMPUTER).build();
		DTOComputer dtoComputer = new DTOComputer.DTOComputerBuilder(APPLE_COMPUTER).build();
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.mapModelToDTO(computer));
	}

	@Test
	public void mapModelToDTO2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer.ComputerBuilder(APPLE_COMPUTER)
										.withIntroduced(introduced)
										.withDiscontinued(discontinued)
										.withCompanyId(1)
										.build();
		DTOComputer dtoComputer = new DTOComputer.DTOComputerBuilder(APPLE_COMPUTER)
												 .withIntroduced("10-10-2010")
												 .withDiscontinued(today.format(formatter))
												 .withCompany("1")
												 .build();
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.mapModelToDTO(computer));
	}

	@Test
	public void mapDTOToModelTest() {
		Computer computer = new Computer.ComputerBuilder(APPLE_COMPUTER).build();
		DTOComputer dtoComputer = new DTOComputer.DTOComputerBuilder(APPLE_COMPUTER).build();
		assertEquals("Mapped Computer isn't equal to expected Computer",computer,mapper.mapDTOToModel(dtoComputer));
	}

	@Test
	public void mapDTOToModel2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer.ComputerBuilder(APPLE_COMPUTER)
										.withIntroduced(introduced)
										.withDiscontinued(discontinued)
										.withCompanyId(1)
										.build();
		DTOComputer dtoComputer = new DTOComputer.DTOComputerBuilder(APPLE_COMPUTER)
				 .withIntroduced("10-10-2010")
				 .withDiscontinued(today.format(formatter))
				 .withCompany("1")
				 .build();
		assertEquals("DTOComputer isn't equal to expected Computer",computer,mapper.mapDTOToModel(dtoComputer));
	}
}
