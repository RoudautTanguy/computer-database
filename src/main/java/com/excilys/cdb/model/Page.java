package com.excilys.cdb.model;

import java.util.List;

public class Page<T> {

	List<T> list;
	int limit = 0;
	int index = 0;
	String search = "";
	
	public Page(List<T> pList, int pLimit) {
		this.list = pList;
		this.limit = pLimit;
	}
	
	public Page(List<T> pList, int pIndex, int pLimit ,String search) {
		this.list = pList;
		this.limit = pLimit;
		this.index = pIndex;
		this.search = search;
	}
	
	/**
	 * Get the current Slice of the page
	 * @return a list of data
	 */
	public List<T> getList() {
		return list;
	}
	
	public void setList(List<T> pList) {
		this.list = pList;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int pIndex) {
		this.index = pIndex;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public String getSearch() {
		return search;
	}
	
	public int decrementIndex() {
		this.index--;
		return index;
	}
	
	public int incrementIndex() {
		this.index++;
		return index;
	}

}
