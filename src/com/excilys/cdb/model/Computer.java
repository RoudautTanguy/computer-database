package com.excilys.cdb.model;

import java.sql.Timestamp;

public class Computer {

	private int id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Integer companyId;
	
	public Computer(int pId, String pName, Timestamp pIntroduced, Timestamp pDiscontinued, Integer pCompanyId) {
		setId(pId);
		setName(pName);
		setIntroduced(pIntroduced);
		setDiscontinued(pDiscontinued);
		setCompanyId(pCompanyId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getIntroduced() {
		return introduced;
	}

	public void setIntroduced(Timestamp introduced) {
		this.introduced = introduced;
	}

	public Timestamp getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(Timestamp discontinued) {
		this.discontinued = discontinued;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	public boolean validate() {
		// Discontinued should be null if introduced is null
		if(getIntroduced() == null) {
			return getDiscontinued() == null;
		} else if(getDiscontinued() == null) { // Introduced is not null 
			return true;
		} else { // Introduced and Discontinued are not null
			return  getDiscontinued().after(getIntroduced()) && getIntroduced().before(getDiscontinued());
		}
	}
	
	@Override
	public String toString() {
		return "Computer@" + this.id + ":" + this.name + 
				"[Introduced:" + this.introduced +
				"Discontinued:" + this.discontinued + 
				"CompanyId:"+this.companyId + "]";
	}
	
}
