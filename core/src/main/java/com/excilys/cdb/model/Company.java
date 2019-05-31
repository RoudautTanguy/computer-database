package com.excilys.cdb.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy = "company",orphanRemoval=true,cascade = CascadeType.ALL)
	private Set<Computer> computers;
	
	public Company() {}
	
	public Company(int pId, String pName) {
		this.id = pId;
		this.name = pName;
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
	
	public Set<Computer> getComputers() {
		return computers;
	}

	public void setComputers(Set<Computer> computers) {
		this.computers = computers;
	}

	@Override
	public String toString() {
		return "Company@" + this.id + ":" + this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Company other = (Company) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	
}
