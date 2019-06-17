package com.excilys.cdb.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.OrderByEnum;

@Service
public class ServicePagination {
	
	private static final int DEFAULT_SIZE = 10;
	
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
	public int getSize(String sizeParameter) {
		int size = DEFAULT_SIZE;
		try {
			size = Integer.parseInt(sizeParameter);
			if(size != 10 && size != 50 && size !=100) {
				size = DEFAULT_SIZE;
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
	
	public OrderByEnum getOrderBy(String sort, boolean desc) {
		OrderByEnum orderBy = OrderByEnum.DEFAULT;
		if(sort != null && sort != "") {
			List<OrderByEnum> sortList = Stream.of(OrderByEnum.values()).filter((OrderByEnum order) -> sort.equals(order.getSortFieldName())).collect(Collectors.toList());
			if(sortList.size()==2) {
				orderBy = desc?sortList.get(1):sortList.get(0);
			}
		}
		return orderBy;
	}
}
