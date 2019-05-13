package com.excilys.cdb.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.NotAValidCompanyException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DAOCompany;
import com.excilys.cdb.persistence.DAOComputer;

public class DAOCompanyTest {

	DAOCompany dao = DAOCompany.getInstance();

	//Delete

	@Test(expected = CompanyNotFoundException.class)
	public void cantDeleteCompanyWithNegativeIdTest() throws CantConnectException, CompanyNotFoundException {
		dao.deleteCompany(-1);
	}
	
	//Insert & Delete
	@Test
	public void InsertAndDeleteCompany() throws NotAValidCompanyException, SQLException, NotAValidComputerException, CantConnectException, CompanyNotFoundException {
		int count = dao.countCompanies();
		dao.insertCompany("TestCompany");
		assertTrue("Company is not inserted",dao.countCompanies()>count);
		int insertedId = dao.getLastCompanyId();
		
		DAOComputer daoComputer = DAOComputer.getInstance();
		int countComputer = daoComputer.countComputers("");
		daoComputer.insertComputer(new Computer.ComputerBuilder("ComputerWillBeDeleted").withCompanyId(insertedId).build());
		assertTrue("Computer is not inserted",daoComputer.countComputers("")>countComputer);
		
		dao.deleteCompany(insertedId);
		assertTrue("Company is not deleted",dao.countCompanies()==count);
		assertTrue("Computer is not deleted",daoComputer.countComputers("")==countComputer);
	}

	//Count
	
	@Test 
	public void countCompaniesTest() {
		assertTrue("0 companies found", dao.countCompanies()>0);
	}

	//List
	
	@Test
	public void listCompaniesNotEmptyTest() throws PageNotFoundException {
		assertFalse("Companies list shouldn't be empty",dao.list().isEmpty());
		assertFalse("Companies list shouldn't be empty",dao.list(0,50).isEmpty());
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeIndexTest() throws PageNotFoundException {
		dao.list(-1,50);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeLimitTest() throws PageNotFoundException {
		dao.list(0,-1);
	}

	@Test(expected = PageNotFoundException.class)
	public void listWithUnexistingIndexTest() throws PageNotFoundException {
		dao.list(50,50);
	}

}
