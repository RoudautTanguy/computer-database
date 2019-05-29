package com.excilys.cdb.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
@WebAppConfiguration
@EnableTransactionManagement
public class ServiceCompanyTest {

	@Autowired
	ServiceCompany serviceCompany;
	@Autowired
	ServiceComputer serviceComputer;

	//Delete

	@Test(expected = CompanyNotFoundException.class)
	public void cantDeleteCompanyWithNegativeIdTest() throws CompanyNotFoundException {
		serviceCompany.deleteById(-1);
	}
	
	//Insert & Delete
	@Test
	public void insertAndDeleteCompany() throws NotAValidComputerException, CompanyNotFoundException {
		int count = (int) serviceCompany.count();
		serviceCompany.insert(new Company(0,"TestCompany"));
		assertTrue("Company is not inserted",serviceCompany.count()>count);
		Company lastCompany = serviceCompany.getLastCompany();
		int countComputer = serviceComputer.countByName("");
		serviceComputer.insert(new DTOComputer(1,"ComputerWillBeDeleted","","",lastCompany.getId(),lastCompany.getName()));
		assertTrue("Computer is not inserted",serviceComputer.countByName("")>countComputer);
		serviceCompany.deleteById(lastCompany.getId());
		assertTrue("Company is not deleted",serviceCompany.count()==count);
		assertTrue("Computer is not deleted",serviceComputer.countByName("")==countComputer);
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
