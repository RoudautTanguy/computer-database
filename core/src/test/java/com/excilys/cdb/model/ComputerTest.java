package com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.junit.Test;

public class ComputerTest {
	
	LocalDate today = LocalDate.now();

	@Test
	public void computerEqualsWithIdTest(){
		assertEquals("Computer should be equals",new Computer(1,null,null,null,null),new Computer(1,null,null,null,null));
	}
	
	@Test
	public void computerNotEqualsWithIdTest(){
		assertNotEquals("Computer shouldn't be equals",new Computer(0,null,null,null,null),new Computer(1,null,null,null,null));
	}
	
	@Test
	public void computerEqualsWithNameTest(){
		assertEquals("Computer should be equals",new Computer(1,"AppleComputer",null,null,null),new Computer(1,"AppleComputer",null,null,null));
	}
	
	@Test
	public void computerNotEqualsWithNameTest(){
		assertNotEquals("Computer shouldn't be equals",new Computer(0,"AppleComputer",null,null,null),new Computer(0,"NotAppleComputer",null,null,null));
	}
	
	@Test
	public void computerEqualsWithIntroducedTest(){
		Timestamp introduced = Timestamp.valueOf(today.atStartOfDay());
		assertEquals("Computer should be equals",new Computer(0,null,introduced,null,null),new Computer(0,null,Timestamp.valueOf(today.atStartOfDay()),null,null));
	}
	
	@Test
	public void computerNotEqualsWithIntroducedTest(){
		Timestamp introduced = Timestamp.valueOf(today.atStartOfDay());
		assertNotEquals("Computer shouldn't be equals",new Computer(0,null,introduced,null,null),new Computer(0,null,Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay()),null,null));
	}
	
	@Test
	public void computerEqualsWithDiscontinuedTest(){
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		assertEquals("Computer should be equals",new Computer(0,null,null,discontinued,null),new Computer(0,null,null,Timestamp.valueOf(today.atStartOfDay()),null));
	}
	
	@Test
	public void computerNotEqualsWithDiscontinuedTest(){
		Timestamp discontinued = Timestamp.valueOf(today.atStartOfDay());
		assertNotEquals("Computer shouldn't be equals",new Computer(0,null,null,discontinued,null),new Computer(0,null,null,Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay()),null));
	}
	
	@Test
	public void computerEqualsWithCompanyTest(){
		assertEquals("Computer should be equals",new Computer(0,null,null,null,new Company(0,"Apple")),new Computer(0,null,null,null,new Company(0,"Apple")));
	}
	
	@Test
	public void computerNotEqualsWithCompanyTest(){
		assertNotEquals("Computer shouldn't be equals",new Computer(0,null,null,null,new Company(0,"Apple")),new Computer(0,null,null,null,new Company(0,"NotApple")));
	}
	
	@Test
	public void computerToStringTest() {
		assertEquals("Computer to String isn't the expected string",new Computer(23,"LenovoComputer",null,null,null).toString(),"Computer@23:LenovoComputer[Introduced:null Discontinued:null Company:null]");
	}
	
	@Test
	public void computerToStringWithDateTest() {
		Timestamp introduced = Timestamp.valueOf(today.atStartOfDay());
		Timestamp discontinued = Timestamp.valueOf(LocalDate.of(2010,10,10).atStartOfDay());
		assertEquals("Computer to String isn't the expected string",new Computer(23,"LenovoComputer",introduced,discontinued,null).toString(),"Computer@23:LenovoComputer[Introduced:"+introduced.toString()+" Discontinued:2010-10-10 00:00:00.0 Company:null]");
	}
	
	@Test
	public void computerToStringWithCompanyTest() {
		assertEquals("Computer to String isn't the expected string",new Computer(23,"LenovoComputer",null,null,new Company(23,"Lenovo")).toString(),"Computer@23:LenovoComputer[Introduced:null Discontinued:null Company:Company@23:Lenovo]");
	}
}
