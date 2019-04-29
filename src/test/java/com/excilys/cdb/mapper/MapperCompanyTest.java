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
		assertEquals(dtoCompany,mapper.modelToDTO(company));
	}
	
	@Test
	public void mapDTOToModelTest(){
		Company company = new Company(1,"Apple");
		DTOCompany dtoCompany = new DTOCompany("1","Apple");
		assertEquals(company,mapper.DTOToModel(dtoCompany));
	}
}
