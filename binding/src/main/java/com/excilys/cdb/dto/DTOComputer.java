package com.excilys.cdb.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

import com.excilys.cdb.dto.validator.CheckDateFormat;

public class DTOComputer {

	@PositiveOrZero
	private int id;
	@NotEmpty
	private String name;
	@CheckDateFormat(pattern = "dd-MM-yyyy")
	private String introduced;
	@CheckDateFormat(pattern = "dd-MM-yyyy")
	private String discontinued;
	private Integer companyId;
	private String companyName;
	
	public DTOComputer() {
		this("");
	}
	
	public DTOComputer(int id, String name, String introduced, String discontinued, Integer companyId, String companyName) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.companyName = companyName;
	}
	
	public DTOComputer(String name) {
		this(0,name,"","",null,"");
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
	
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Override
	public String toString() {
		return "DTOComputer@" + this.id + ":" + this.name + 
				"[Introduced:" + this.introduced +
				"Discontinued:" + this.discontinued + 
				"Company:"+this.companyName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
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
		DTOComputer other = (DTOComputer) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName)) {
			return false;
		}
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued)) {
			return false;
		}
		if (id != other.id)
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced)) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	
	
}
