package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.excilys.cdb.model.Company;

public class MapperCompanyTest {

	MapperCompany mapper = MapperCompany.getInstance();
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
