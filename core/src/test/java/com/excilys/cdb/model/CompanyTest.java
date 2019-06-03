package test.java.com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.excilys.cdb.model.Company;

public class CompanyTest {

	@Test
	public void companyEqualsWithIdTest(){
		assertEquals("Company should be equals",new Company(1,null),new Company(1,null));
	}
	
	@Test
	public void companyNotEqualsWithIdTest(){
		assertNotEquals("Company shouldn't be equals",new Company(0,null),new Company(1,null));
	}
	
	@Test
	public void companyEqualsWithNameTest(){
		assertEquals("Company should be equals",new Company(1,"Apple"),new Company(1,"Apple"));
	}
	
	@Test
	public void companyNotEqualsWithNameTest(){
		assertNotEquals("Company shouldn't be equals",new Company(0,"Apple"),new Company(0,"NotApple"));
	}
	
	@Test
	public void companyToStringTest() {
		assertEquals("Company to String isn't the expected string",new Company(23,"Lenovo").toString(),"Company@23:Lenovo");
	}
}
