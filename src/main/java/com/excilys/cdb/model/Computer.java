package com.excilys.cdb.model;

import java.sql.Timestamp;

public class Computer {

	private int id;
	private String name;
	private Timestamp introduced;
	private Timestamp discontinued;
	private Integer companyId;
	
	public Computer(int pId, String pName, Timestamp pIntroduced, Timestamp pDiscontinued, Integer pCompanyId) {
		this.companyId = pId;
		this.name = pName;
		this.introduced = pIntroduced;
		this.discontinued = pDiscontinued;
		this.companyId = pCompanyId;
	}
	
	public Computer(String pName, Timestamp pIntroduced, Timestamp pDiscontinued, Integer pCompanyId) {
		this(0, pName, pIntroduced, pDiscontinued, pCompanyId);
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
		
	@Override
	public String toString() {
		return "Computer@" + this.id + ":" + this.name + 
				"[Introduced:" + this.introduced +
				"Discontinued:" + this.discontinued + 
				"CompanyId:"+this.companyId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + id;
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id != other.id)
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
