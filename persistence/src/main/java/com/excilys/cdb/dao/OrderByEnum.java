package com.excilys.cdb.dao;

import org.springframework.data.domain.Sort;

public enum OrderByEnum {
	DEFAULT("id", Sort.by("id")),
	ORDER_BY_NAME_ASC("name", Sort.by("name")),
	ORDER_BY_NAME_DESC("name", Sort.by("name").descending()),
	ORDER_BY_INTRODUCED_ASC("introduced", Sort.by("introduced")),
	ORDER_BY_INTRODUCED_DESC("introduced", Sort.by("introduced").descending()),
	ORDER_BY_DISCONTINUED_ASC("discontinued", Sort.by("discontinued")),
	ORDER_BY_DISCONTINUED_DESC("discontinued", Sort.by("discontinued").descending()),
	ORDER_BY_COMPANY_NAME_ASC("company", Sort.by("cp.name")),
	ORDER_BY_COMPANY_NAME_DESC("company", Sort.by("cp.name").descending());
	
	private String sortFieldName;
	private Sort sort;
	
	private OrderByEnum(String sortFieldName,Sort sort) {
		this.sort = sort;
		this.sortFieldName = sortFieldName;
	}

	public Sort getSort() {
		return sort;
	}

	public String getSortFieldName() {
		return sortFieldName;
	}
	
}
