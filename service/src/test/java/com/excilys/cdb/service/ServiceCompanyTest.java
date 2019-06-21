package com.excilys.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.cdb.config.SpringServiceTestConfiguration;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Company;

@EnableTransactionManagement
public class ServiceCompanyTest {

	private static ServiceCompany serviceCompany;
	private static ServiceComputer serviceComputer;

	@BeforeClass
	public static void setUpBeforeClass() {
		@SuppressWarnings("resource")
		ApplicationContext vApplicationContext = new AnnotationConfigApplicationContext(SpringServiceTestConfiguration.class);
		serviceCompany = vApplicationContext.getBean(ServiceCompany.class);
		serviceComputer = vApplicationContext.getBean(ServiceComputer.class);
	}
	
	//Delete

	@Test(expected = CompanyNotFoundException.class)
	public void cantDeleteCompanyWithNegativeIdTest() throws CompanyNotFoundException {
		serviceCompany.deleteById(-1);
	}
	
	//Insert & Delete
	@Test
	@Transactional
	public void insertAndDeleteCompany() throws NotAValidComputerException, CompanyNotFoundException {
		int count = (int) serviceCompany.count();
		serviceCompany.insert(new DTOCompany(0,"TestCompany",0));
		assertTrue("Company is not inserted",serviceCompany.count()>count);
		Company lastCompany = serviceCompany.getLastCompany();
		int countComputer = serviceComputer.countByName("");
		serviceComputer.insert(new DTOComputer(1,"ComputerWillBeDeleted","","",lastCompany.getId(),lastCompany.getName(),0));
		assertTrue("Computer is not inserted",serviceComputer.countByName("")>countComputer);
		serviceCompany.deleteById(lastCompany.getId());
		assertTrue("Company is not deleted",serviceCompany.count()==count);
		assertTrue("Computer is not deleted",serviceComputer.countByName("")==countComputer);
	}
	
	@Test
	public void updateCompany() throws CompanyNotFoundException, ConcurentConflictException {
		serviceCompany.insert(new DTOCompany(0,"TestCompany",0));
		Company lastCompany = serviceCompany.getLastCompany();
		serviceCompany.update(lastCompany.getId(), new DTOCompany(0,"NewTestCompany",0));
		assertEquals("Company is not updated","NewTestCompany",serviceCompany.getLastCompany().getName());
		serviceCompany.deleteById(lastCompany.getId());
	}
	
	@Test(expected = ConcurentConflictException.class)
	public void updateCompanyWithConflictVersion() throws CompanyNotFoundException, ConcurentConflictException {
		serviceCompany.insert(new DTOCompany(0,"TestCompany",0));
		Company lastCompany = serviceCompany.getLastCompany();
		DTOCompany insertedCompany = new DTOCompany(0, "Inserted Company", 0);
		serviceCompany.update(lastCompany.getId(), insertedCompany);
		serviceCompany.update(lastCompany.getId(), insertedCompany);
	}
	

	//Count
	
	@Test 
	public void countCompaniesTest() {
		assertTrue("0 companies found", serviceCompany.count()>0);
	}

	//List
	
	@Test
	public void listCompaniesNotEmptyTest() {
		assertFalse("Companies list shouldn't be empty",serviceCompany.list().isEmpty());
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeIndexTest() throws PageNotFoundException {
		serviceCompany.list(-1,50);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeLimitTest() throws PageNotFoundException {
		serviceCompany.list(0,-1);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithUnexistingIndexTest() throws PageNotFoundException {
		serviceCompany.list(50,50);
	}

}
