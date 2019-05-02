package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import com.excilys.cdb.model.Computer;

public class MapperComputerTest {

	MapperComputer mapper = MapperComputer.getInstance();

	@Test
	public void mapModelToDTOTest() {
		Computer computer = new Computer(0,"AppleComputer",null,null,null);
		DTOComputer dtoComputer = new DTOComputer("AppleComputer");
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.modelToDTO(computer));
	}

	@Test
	public void mapModelToDTO2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer(0, "AppleComputer", introduced, discontinued, 1);
		DTOComputer dtoComputer = new DTOComputer("AppleComputer", "10-10-2010", today.format(formatter), "1");
		assertEquals("Mapped DTOComputer isn't equal to expected DTOComputer",dtoComputer,mapper.modelToDTO(computer));
	}

	@Test
	public void mapDTOToModelTest() {
		Computer computer = new Computer(0,"AppleComputer",null,null,null);
		DTOComputer dtoComputer = new DTOComputer("AppleComputer");
		assertEquals("Mapped Computer isn't equal to expected Computer",computer,mapper.DTOToModel(dtoComputer));
	}

	@Test
	public void mapDTOToModel2Test() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
		LocalDate today = LocalDate.now();
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		Computer computer = new Computer(0, "AppleComputer", introduced, discontinued, 1);
		DTOComputer dtoComputer = new DTOComputer("AppleComputer", "10-10-2010", today.format(formatter), "1");
		assertEquals("DTOComputer isn't equal to expected Computer",computer,mapper.DTOToModel(dtoComputer));
	}
}
