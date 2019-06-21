package com.excilys.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.SpringServiceTestConfiguration;
import com.excilys.cdb.dao.OrderByEnum;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ServiceComputerTest {

	private static ServiceComputer service;
	private static MapperComputer mapperComputer;
	
	int inserted = 0;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		@SuppressWarnings("resource")
		ApplicationContext vApplicationContext = new AnnotationConfigApplicationContext(SpringServiceTestConfiguration.class);
		service = vApplicationContext.getBean(ServiceComputer.class);
		mapperComputer = vApplicationContext.getBean(MapperComputer.class);
	}

	// Insert 

	@Test
	public void insertFullComputerTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		service.insert(mapperComputer.mapModelToDTO(new Computer(1,"AttariComputerTest",introduced,discontinued,new Company(20,"Atari",0),0)));
	}

	@Test
	public void insertComputerWithNameTest() throws NotAValidComputerException, CompanyNotFoundException {
		service.insert(mapperComputer.mapModelToDTO(new Computer(1,"AttariComputerTest",null,null,null,0)));
	}

	@Test(expected = CompanyNotFoundException.class)
	public void insertComputerWithInvalidCompanyTest() throws NotAValidComputerException, CompanyNotFoundException {
		service.insert(mapperComputer.mapModelToDTO(new Computer(1,"WrongCompanyId",null,null,new Company(1000,"NotWorkingCompany",0),0)));
	}

	// Update

	@Test
	public void updateFullComputerTest() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		int version = service.find(200).getVersion();
		service.update(200, mapperComputer.mapModelToDTO(new Computer(200,"Lenovo",introduced,discontinued,new Company(36,"Lenovo Group",0),version)));
	}

	@Test
	public void updateComputerWithNameTest() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		int version = service.find(200).getVersion();
		service.update(200, mapperComputer.mapModelToDTO(new Computer(200,"Laptop",null,null,null,version)));
	}

	@Test(expected = ComputerNotFoundException.class)
	public void updateComputerNotInDatabaseTest() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		int version = service.find(200).getVersion();
		service.update(9999, mapperComputer.mapModelToDTO(new Computer(200,"Lenovo Laptop",null,null,new Company(36,"Lenovo Group",0),version)));
	}
	
	@Test(expected = ConcurentConflictException.class)
	public void updateComputerWithConflictVersion() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		DTOComputer dtoComputer = new DTOComputer(1, "Conflict Computer",null, null, 0, null, 0);
		service.update(200, dtoComputer);
		service.update(200, dtoComputer);
	}
	
	@Test(expected = ConcurentConflictException.class)
	public void updateComputerWithNoConflictVersion() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException {
		DTOComputer dtoComputer = new DTOComputer(1, "Conflict Computer",null, null, 0, null, 0);
		service.update(200, dtoComputer);
		dtoComputer = service.find(200);
		service.update(200, dtoComputer);
	}

	// Delete

	@Test
	public void getLastComputerAndDeleteComputerTest() throws ComputerNotFoundException{
		int id = service.getLastComputerId();
		service.delete(id);
		id = service.getLastComputerId();
		service.delete(id);
	}

	@Test(expected = ComputerNotFoundException.class)
	public void deleteComputerWithNegativeIdTest() throws ComputerNotFoundException {
		service.delete(-1);
	}

	@Test(expected = ComputerNotFoundException.class)
	public void deleteComputerNotInDatabaseTest() throws ComputerNotFoundException {
		service.delete(9999);
	}

	// Search

	@Test(expected = PageNotFoundException.class)
	public void searchWithNegativeIndexTest() throws PageNotFoundException {
		service.search(-1, 50, "", OrderByEnum.DEFAULT);
	}

	@Test(expected = PageNotFoundException.class)
	public void searchWithNegativeLimitTest() throws PageNotFoundException {
		service.search(50, -1, "", OrderByEnum.DEFAULT);
	}

	@Test(expected = PageNotFoundException.class)
	public void searchWithNoResultTest() throws PageNotFoundException {
		service.search(50, 50, "", OrderByEnum.DEFAULT);
	}

	// Find

	@Test
	public void findComputerTest() throws ComputerNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2004,1,1).atStartOfDay());
		DTOComputer dtoComputer = mapperComputer.mapModelToDTO(new Computer(283,"Nintendo DS",introduced,null,new Company(24,"Nintendo",0),0));
		assertEquals("The Computer is not the expected one",service.find(283), dtoComputer);
	}

	@Test(expected = ComputerNotFoundException.class)
	public void findInexistingComputerTest() throws ComputerNotFoundException {
		service.find(9999);
	}

	// Count

	@Test 
	public void countComputersTest() {
		assertTrue("Some computers should be found in database", service.countByName("")>0);
	}
	
	@Test 
	public void countAppleComputersTest() {
		assertTrue("Some computers should be found in database", service.countByName("Apple")>0);
	}


}
