package com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DAOComputer;
import com.excilys.cdb.persistence.OrderByEnum;

public class DAOComputerTest {

	DAOComputer dao = DAOComputer.getInstance();
	int inserted = 0;

	// Insert 

	@Test
	public void insertFullComputerTest() throws  NotAValidComputerException, CantConnectException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		assertTrue("Can't insert Computer",dao.insertComputer(new Computer.ComputerBuilder("AttariComputerTest")
																  .withId(1)
																  .withIntroduced(introduced)
																  .withDiscontinued(discontinued)
																  .withCompanyId(20)
																  .build()));
	}

	@Test
	public void insertComputerWithNameTest() throws NotAValidComputerException, CantConnectException {
		assertTrue("Can't insert Computer",dao.insertComputer(new Computer.ComputerBuilder("AttariComputerTest")
				  												  .withId(1)
				  												  .build()));
	}

	@Test(expected = NotAValidComputerException.class)
	public void insertNullComputerTest() throws NotAValidComputerException, CantConnectException {
		dao.insertComputer(new Computer.ComputerBuilder(null).build());
	}

	@Test(expected = NotAValidComputerException.class)
	public void insertComputerWithInvalidCompanyIdTest() throws NotAValidComputerException, CantConnectException {
		dao.insertComputer(new Computer.ComputerBuilder("WrongCompanyId")
				  			   .withId(1)
				  			   .withCompanyId(100)
				  			   .build());
	}

	// Update

	@Test
	public void updateFullComputerTest() throws NotAValidComputerException, ComputerNotFoundException, CantConnectException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		dao.updateComputer(600, new Computer.ComputerBuilder("Laptop")
									.withId(600)
									.withIntroduced(introduced)
									.withDiscontinued(discontinued)
									.withCompanyId(Integer.valueOf(36))
									.build());
	}

	@Test
	public void updateComputerWithNameTest() throws NotAValidComputerException, ComputerNotFoundException, CantConnectException {
		dao.updateComputer(600, new Computer.ComputerBuilder("Laptop")
				   					.withId(600)
									.build());
	}

	@Test(expected = NotAValidComputerException.class)
	public void updateNullComputerTest() throws NotAValidComputerException, ComputerNotFoundException, CantConnectException {
		dao.updateComputer(599,new Computer.ComputerBuilder(null)
				   				   .withId(1)
				   				   .build());
	}

	@Test(expected = ComputerNotFoundException.class)
	public void updateComputerNotInDatabaseTest() throws NotAValidComputerException, ComputerNotFoundException, CantConnectException {
		dao.updateComputer(9999,new Computer.ComputerBuilder("Name")
				   					.withId(1)
				   					.build());
	}

	// Delete

	@Test
	public void getLastComputerAndDeleteComputerTest() throws CantConnectException, ComputerNotFoundException {
		int id = dao.getLastComputerId();
		dao.deleteComputer(id);
		id = dao.getLastComputerId();
		dao.deleteComputer(id);
	}

	@Test(expected = ComputerNotFoundException.class)
	public void deleteComputerWithNegativeIdTest() throws CantConnectException, ComputerNotFoundException {
		dao.deleteComputer(-1);
	}

	@Test(expected = ComputerNotFoundException.class)
	public void deleteComputerNotInDatabaseTest() throws CantConnectException, ComputerNotFoundException {
		dao.deleteComputer(9999);
	}

	// Search

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeIndexTest() throws PageNotFoundException {
		dao.search(-1, 50, "", OrderByEnum.DEFAULT);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeLimitTest() throws PageNotFoundException {
		dao.search(50, -1, "", OrderByEnum.DEFAULT);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNoResultTest() throws PageNotFoundException {
		dao.search(50, 50, "", OrderByEnum.DEFAULT);
	}

	// Find

	@Test
	public void findComputerTest() throws ComputerNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2004,1,1).atStartOfDay());
		DTOComputer dtoComputer = MapperComputer.getInstance().mapModelToDTO(new Computer.ComputerBuilder("Nintendo DS")
																					  .withId(283)
																					  .withIntroduced(introduced)
																					  .withCompanyId(24)
																					  .build());
		dtoComputer.setCompany("Nintendo");
		assertEquals("The Computer is not the expected one",dao.find(283), dtoComputer);
	}

	@Test(expected = ComputerNotFoundException.class)
	public void findInexistingComputerTest() throws ComputerNotFoundException {
		dao.find(9999);
	}

	// Count

	@Test 
	public void countComputersTest() {
		assertTrue("Some computers should be found in database", dao.countComputers("")>0);
	}
	
	@Test 
	public void countAppleComputersTest() {
		assertTrue("Some computers should be found in database", dao.countComputers("Apple")>0);
	}


}
