package com.excilys.cdb.service;

import com.excilys.cdb.exception.SliceNotFoundException;
import com.excilys.cdb.model.Page;

public class ServicePage {
	
	/**
	 * Change the slice of a page to the previous one
	 * @param <T>
	 * @param page
	 * @return the page
	 * @throws SliceNotFoundException
	 */
	public static <T> Page<T> changeSliceToPrevious(Page<T> page) throws SliceNotFoundException {
		return page.previousSlice();
	}
	
	/**
	 * Change the slice of a page to the next one
	 * @param <T>
	 * @param page
	 * @return the page
	 * @throws SliceNotFoundException
	 */
	public static <T> Page<T> changeSliceToNext(Page<T> page) throws SliceNotFoundException {
		return page.nextSlice();
	}
	
	/**
	 * Change the slice of a page with the index of the new slice
	 * @param <T>
	 * @param page
	 * @param index
	 * @return the page
	 * @throws SliceNotFoundException
	 */
	public static <T> Page<T> setSlice(Page<T> page, int index) throws SliceNotFoundException {
		return page.setSlice(index);
	}
	
}
