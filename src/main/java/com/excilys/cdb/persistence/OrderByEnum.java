package com.excilys.cdb.persistence;

public enum OrderByEnum {
	DEFAULT(""),
	ORDER_BY_NAME_ASC("ORDER BY computer.name ASC"),
	ORDER_BY_NAME_DESC("ORDER BY computer.name DESC"),
	ORDER_BY_INTRODUCED_ASC("ORDER BY computer.introduced IS NULL, computer.introduced ASC"),
	ORDER_BY_INTRODUCED_DESC("ORDER BY computer.introduced IS NULL, computer.introduced DESC"),
	ORDER_BY_DISCONTINUED_ASC("ORDER BY computer.discontinued IS NULL, computer.discontinued ASC"),
	ORDER_BY_DISCONTINUED_DESC("ORDER BY computer.discontinued IS NULL, computer.discontinued DESC"),
	ORDER_BY_COMPANY_NAME_ASC("ORDER BY company.name IS NULL, company.name ASC"),
	ORDER_BY_COMPANY_NAME_DESC("ORDER BY company.name IS NULL, company.name DESC");
	
	private String query;
	
	private OrderByEnum(String pQuery) {
		this.query = pQuery;
	}

	public String getQuery() {
		return query;
	}
	
}
