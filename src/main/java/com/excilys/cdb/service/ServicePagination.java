package com.excilys.cdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicePagination {
	
	private static ServicePagination instance;
	
	/**
	 * Get instance of ServicePagination
	 * @return instance
	 */
	public static ServicePagination getInstance() {
		if(instance == null) {
			instance = new ServicePagination();
		}
		return instance;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ServicePagination.class);

	/**
	 * Get the current page of the application
	 * @param pageParameter
	 * @return the number of the page
	 */
	public int getCurrentPage(String pageParameter) {
		int page = 1;
		try {
			page = Integer.parseInt(pageParameter);
		} catch (NumberFormatException e) {
			logger.info("No parameter 'page'");
		}
		return page;
	}

	/**
	 * Get the size of the page. Can only be 10,50 or 100.
	 * @param sizeParameter new size of the page
	 * @param size actual size
	 * @return the new size of the page
	 */
	public int getSize(String sizeParameter, int size) {
		try {
			int oldSize = size;
			size = Integer.parseInt(sizeParameter);
			if(size != 10 && size != 50 && size !=100) {
				size = oldSize;
			} 
		} catch (NumberFormatException e) {
			logger.info("No parameter 'size'");
		}
		return size;
	}

	/**
	 * Get the pagination of the application
	 * @param lastPage of the application
	 * @param currentPage of the application
	 * @return the medium page of the application pagination
	 */
	public int getPagination(int lastPage, int currentPage) {
		return Math.max(3, Math.min(lastPage -2, currentPage));
	}
}
