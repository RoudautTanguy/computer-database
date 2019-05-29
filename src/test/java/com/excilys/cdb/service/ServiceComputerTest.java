package com.excilys.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.OrderByEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
public class ServiceComputerTest {

	@Autowired
	ServiceComputer service;
	@Autowired
	MapperComputer mapperComputer;
	int inserted = 0;

	// Insert 

	@Test
	public void insertFullComputerTest() throws NotAValidComputerException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		service.insert(mapperComputer.mapModelToDTO(new Computer(1,"AttariComputerTest",introduced,discontinued,new Company(20,"Atari"))));
	}

	@Test
	public void insertComputerWithNameTest() throws NotAValidComputerException, CompanyNotFoundException {
		service.insert(mapperComputer.mapModelToDTO(new Computer(1,"AttariComputerTest",null,null,null)));
	}

	@Test(expected = CompanyNotFoundException.class)
	public void insertComputerWithInvalidCompanyTest() throws NotAValidComputerException, CompanyNotFoundException {
		service.insert(mapperComputer.mapModelToDTO(new Computer(1,"WrongCompanyId",null,null,new Company(100,"NotWorkingCompany"))));
	}

	// Update

	@Test
	public void updateFullComputerTest() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException {
		Timestamp introduced = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.now().atStartOfDay());
		service.update(200, mapperComputer.mapModelToDTO(new Computer(200,"Lenovo",introduced,discontinued,new Company(36,"Lenovo Group"))));
	}

	@Test
	public void updateComputerWithNameTest() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException {
		service.update(200, mapperComputer.mapModelToDTO(new Computer(200,"Laptop",null,null,null)));
	}

	@Test(expected = ComputerNotFoundException.class)
	public void updateComputerNotInDatabaseTest() throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException {
		service.update(9999, mapperComputer.mapModelToDTO(new Computer(200,"Lenovo Laptop",null,null,new Company(36,"Lenovo Group"))));
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
		DTOComputer dtoComputer = mapperComputer.mapModelToDTO(new Computer(283,"Nintendo DS",introduced,null,new Company(24,"Nintendo")));
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
