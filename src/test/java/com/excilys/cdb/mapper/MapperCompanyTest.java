package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
public class MapperCompanyTest {

	@Autowired
	MapperCompany mapper;
	
	private static final String APPLE_COMPANY = "Apple";
	@Test
	public void mapModelToDTOTest(){
		Company company = new Company(1,APPLE_COMPANY);
		DTOCompany dtoCompany = new DTOCompany("1",APPLE_COMPANY);
		assertEquals("Mapped DTOCompany isn't equal to the expected DTOCompany",dtoCompany,mapper.mapModelToDTO(company));
	}

	@Test
	public void mapDTOToModelTest(){
		Company company = new Company(1,APPLE_COMPANY);
		DTOCompany dtoCompany = new DTOCompany("1",APPLE_COMPANY);
		assertEquals("Mapped Company isn't equal to the expected Company",company,mapper.mapDTOToModel(dtoCompany));
	}
}
