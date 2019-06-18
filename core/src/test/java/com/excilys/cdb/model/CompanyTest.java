package com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class CompanyTest {

	@Test
	public void companyEqualsWithIdTest(){
		assertEquals("Company should be equals",new Company(1,null,0),new Company(1,null,0));
	}
	
	@Test
	public void companyNotEqualsWithIdTest(){
		assertNotEquals("Company shouldn't be equals",new Company(0,null,0),new Company(1,null,0));
	}
	
	@Test
	public void companyEqualsWithNameTest(){
		assertEquals("Company should be equals",new Company(1,"Apple",0),new Company(1,"Apple",0));
	}
	
	@Test
	public void companyNotEqualsWithNameTest(){
		assertNotEquals("Company shouldn't be equals",new Company(0,"Apple",0),new Company(0,"NotApple",0));
	}
	
	@Test
	public void companyToStringTest() {
		assertEquals("Company to String isn't the expected string","Company@23:Lenovo",new Company(23,"Lenovo",0).toString());
	}
}
