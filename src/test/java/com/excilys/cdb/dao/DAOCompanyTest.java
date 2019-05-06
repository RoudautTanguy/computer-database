package com.excilys.cdb.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.DAOCompany;

public class DAOCompanyTest {

	DAOCompany dao = DAOCompany.getInstance();

	@Test
	public void cantInsertCompanyTest() {
		assertFalse("Company shouldn't be inserted", dao.insert(new Company(1,"Test")));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void cantUpdateCompanyTest() {
		dao.update(1, new Company(1,"Test"));
	}

	@Test
	public void cantDeleteCompanyTest() {
		assertFalse("Company shouldn't be deleted", dao.delete(1));
	}

	@Test 
	public void countCompaniesTest() {
		assertTrue("0 companies found", dao.count()>0);
	}

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
