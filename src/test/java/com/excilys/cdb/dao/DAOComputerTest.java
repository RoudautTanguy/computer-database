package com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DAOComputer;

public class DAOComputerTest {

	DAOComputer dao = DAOComputer.getInstance();
	int inserted = 0;

	// Insert 

	@Test
	public void insertFullComputerTest() throws  NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		assertTrue("Can't insert Computer",dao.insert(new Computer(1,"AttariComputerTest",introduced,discontinued,Integer.valueOf(20))));
	}

	@Test
	public void insertComputerWithNameTest() throws NotAValidComputerException {
		assertTrue("Can't insert Computer",dao.insert(new Computer(1,"ComputerTest",null,null,null)));
	}

	@Test(expected = NotAValidComputerException.class)
	public void insertNullComputerTest() throws NotAValidComputerException {
		dao.insert(new Computer(1,null,null,null,null));
	}

	@Test(expected = NotAValidComputerException.class)
	public void insertComputerWithInvalidCompanyIdTest() throws NotAValidComputerException {
		dao.insert(new Computer(1,"WrongCompanyId",null,null,100));
	}

	// Update

	@Test
	public void updateFullComputerTest() throws NotAValidComputerException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		assertTrue("Can't update Computer",dao.update(600, new Computer(600,"Laptop",introduced,discontinued,Integer.valueOf(36))));
	}

	@Test
	public void updateComputerWithNameTest() throws NotAValidComputerException {
		assertTrue("Can't update Computer",dao.update(600, new Computer(600,"Laptop",null,null,null)));
	}

	@Test(expected = NotAValidComputerException.class)
	public void updateNullComputerTest() throws NotAValidComputerException {
		dao.update(599,new Computer(1,null,null,null,null));
	}

	@Test
	public void updateComputerNotInDatabaseTest() throws NotAValidComputerException {
		assertFalse("Computer not in database is updated",dao.update(9999,new Computer(1,"Name",null,null,null)));
	}

	// Delete

	@Test
	public void getLastComputerAndDeleteComputerTest() {
		int id = dao.getLastComputerId();
		assertTrue("Can't delete Computer",dao.delete(id));
	}

	@Test
	public void deleteComputerWithNegativeIdTest() {
		assertFalse("Computer with negative index is deleted",dao.delete(-1));
	}

	@Test
	public void deleteComputerNotInDatabaseTest() {
		assertFalse("Computer not in database is deleted",dao.delete(9999));
	}

	// List

	@Test
	public void listComputerTest() {
		assertEquals("The list of computer should have the same length of computer present in database",dao.list().size(), dao.count());
	}

	@Test
	public void listWithPaginationTest() throws PageNotFoundException {
		assertEquals("The list should have the same length as the limit of pagination", 50, dao.list(0, 50).size());
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeIndexTest() throws PageNotFoundException {
		dao.list(-1, 50);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeLimitTest() throws PageNotFoundException {
		dao.list(50, -1);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNoResultTest() throws PageNotFoundException {
		dao.list(50, 50);
	}

	// Find

	@Test
	public void findComputerTest() throws ComputerNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2004,1,1).atStartOfDay());
		DTOComputer dtoComputer = MapperComputer.getInstance().modelToDTO(new Computer(283,"Nintendo DS",introduced,null,24));
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
		assertTrue("Some computers should be found in database", dao.count()>0);
	}


}
