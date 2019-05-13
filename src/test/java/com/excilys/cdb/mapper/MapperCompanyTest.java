package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.excilys.cdb.model.Company;

public class MapperCompanyTest {

	MapperCompany mapper = MapperCompany.getInstance();

	@Test
	public void mapModelToDTOTest(){
		Company company = new Company(1,"Apple");
		DTOCompany dtoCompany = new DTOCompany("1","Apple");
		assertEquals("Mapped DTOCompany isn't equal to the expected DTOCompany",dtoCompany,mapper.mapModelToDTO(company));
	}

	@Test
	public void mapDTOToModelTest(){
		Company company = new Company(1,"Apple");
		DTOCompany dtoCompany = new DTOCompany("1","Apple");
		assertEquals("Mapped Company isn't equal to the expected Company",company,mapper.mapDTOToModel(dtoCompany));
	}
}
