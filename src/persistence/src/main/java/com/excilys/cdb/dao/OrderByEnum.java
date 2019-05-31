package com.excilys.cdb.persistence;

import org.springframework.data.domain.Sort;

public enum OrderByEnum {
	DEFAULT(Sort.by("id")),
	ORDER_BY_NAME_ASC(Sort.by("name")),
	ORDER_BY_NAME_DESC(Sort.by("name").descending()),
	ORDER_BY_INTRODUCED_ASC(Sort.by("introduced")),
	ORDER_BY_INTRODUCED_DESC(Sort.by("introduced").descending()),
	ORDER_BY_DISCONTINUED_ASC(Sort.by("discontinued")),
	ORDER_BY_DISCONTINUED_DESC(Sort.by("discontinued").descending()),
	ORDER_BY_COMPANY_NAME_ASC(Sort.by("cp.name")),
	ORDER_BY_COMPANY_NAME_DESC(Sort.by("cp.name").descending());
	
	private Sort sort;
	
	private OrderByEnum(Sort sort) {
		this.sort = sort;
	}

	public Sort getSort() {
		return sort;
	}
	
}
