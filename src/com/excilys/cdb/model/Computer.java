package com.excilys.cdb.model;

import java.util.Date;

public class Computer {

	private int id;
	private String name;
	private Date introduced;
	private Date discontinued;
	private Integer companyId;
	
	public Computer(int pId, String pName, Date pIntroduced, Date pDiscontinued, Integer pCompanyId) {
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

	public Date getIntroduced() {
		return introduced;
	}

	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}

	public Date getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String toString() {
		return "Computer@" + this.id + ":" + this.name + 
				"[Introduced:" + this.introduced +
				"Discontinued:" + this.discontinued + 
				"CompanyId:"+this.companyId + "]";
	}
	
}
