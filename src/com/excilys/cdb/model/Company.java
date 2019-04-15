package com.excilys.cdb.model;

public class Company {

	private int id;
	private String name;
	
	public Company(int pId, String pName) {
		this.setId(pId);
		this.setName(pName);
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
	
	@Override
	public String toString() {
		return "Company@" + this.id + ":" + this.name;
	}
}
