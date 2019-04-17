package com.excilys.cdb.mapper;

public class DTOCompany {

	private String id;
	private String name;
	
	public DTOCompany(String pId, String pName){
		setId(pId);
		setName(pName);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
