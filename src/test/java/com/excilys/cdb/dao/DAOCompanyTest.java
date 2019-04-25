package com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;
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
		assertEquals(false, dao.insert(new Company(1,"Test")));
	}
	
	@Test
	public void cantUpdateCompanyTest() {
		assertEquals(false, dao.update(1, new Company(1,"Test")));
	}
	
	@Test
	public void cantDeleteCompanyTest() {
		assertEquals(false, dao.delete(1));
	}
	
	@Test 
	public void countCompaniesTest() {
		assertTrue(dao.count()>0);
	}
	
	@Test
	public void listCompaniesNotEmptyTest() throws PageNotFoundException {
		assertFalse(dao.list().isEmpty());
		assertFalse(dao.list(0,50).isEmpty());
	}
	
	@Test(expected = PageNotFoundException.class)
	public void listWithNegativeIndexTest() throws PageNotFoundException {
		dao.list(-1,50);
	}

}
