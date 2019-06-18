package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.SpringBindingTestConfiguration;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class MapperComputerTest {

	private static MapperComputer mapper;
	
	private static final String APPLE_COMPUTER = "AppleComputer";
	private static final Company APPLE_COMPANY = new Company(1,"Apple Inc.",0);
	
	@BeforeClass
	public static void setUpBeforeClass() {
		@SuppressWarnings("resource")
		ApplicationContext vApplicationContext = new AnnotationConfigApplicationContext(SpringBindingTestConfiguration.class);
		mapper = vApplicationContext.getBean(MapperComputer.class);
	}

	@Test
	public void mapModelToDTOTest() {
		Computer computer = new Computer(0,APPLE_COMPUTER,null,null,null,0);
		DTOComputer dtoComputer = new DTOComputer(APPLE_COMPUTER);
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.mapModelToDTO(computer));
	}

	@Test
	public void mapModelToDTO2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer(0,APPLE_COMPUTER,introduced,discontinued,APPLE_COMPANY,0);
		DTOComputer dtoComputer = new DTOComputer(0,APPLE_COMPUTER,"10-10-2010",today.format(formatter),APPLE_COMPANY.getId(),APPLE_COMPANY.getName(),0);
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.mapModelToDTO(computer));
	}

	@Test
	public void mapDTOToModelTest() {
		Computer computer = new Computer(0,APPLE_COMPUTER,null,null,null,0);
		DTOComputer dtoComputer = new DTOComputer(APPLE_COMPUTER);
		assertEquals("Mapped Computer isn't equal to expected Computer",computer,mapper.mapDTOToModel(dtoComputer));
	}

	@Test
	public void mapDTOToModel2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer(0,APPLE_COMPUTER,introduced,discontinued,APPLE_COMPANY,0);
		DTOComputer dtoComputer = new DTOComputer(0,APPLE_COMPUTER,"10-10-2010",today.format(formatter),APPLE_COMPANY.getId(),APPLE_COMPANY.getName(),0);
		assertEquals("DTOComputer isn't equal to expected Computer",computer,mapper.mapDTOToModel(dtoComputer));
	}
}
