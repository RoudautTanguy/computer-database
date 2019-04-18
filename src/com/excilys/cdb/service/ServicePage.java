package com.excilys.cdb.service;

import com.excilys.cdb.exception.SliceNotFoundException;
import com.excilys.cdb.model.Page;

public class ServicePage {
	
	public static <T> Page<T> changePageToPrevious(Page<T> page) throws SliceNotFoundException {
		return page.previousSlice();
	}
	
	public static <T> Page<T> changePageToNext(Page<T> page) throws SliceNotFoundException {
		return page.nextSlice();
	}
	
	public static <T> Page<T> setPage(Page<T> page, int index) throws SliceNotFoundException {
		return page.setSlice(index);
	}
	
}
