package com.excilys.cdb.mapper;

public class DTOComputer {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String company;
	
	public DTOComputer(String pName){
		setId(Integer.toString(0));
		setName(pName);
		setIntroduced("NULL");
		setDiscontinued("NULL");
		setCompany("NULL");
	}
	
	public DTOComputer(String pName, String pIntroduced, String pDiscontinued, String pCompany){
		setId(Integer.toString(0));
		setName(pName);
		setIntroduced(pIntroduced);
		setDiscontinued(pDiscontinued);
		setCompany(pCompany);
	}
	
	public DTOComputer(String pId, String pName, String pIntroduced, String pDiscontinued, String pCompany){
		setId(pId);
		setName(pName);
		setIntroduced(pIntroduced);
		setDiscontinued(pDiscontinued);
		setCompany(pCompany);
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

	public String getIntroduced() {
		return introduced;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	
}
