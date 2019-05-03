package com.excilys.cdb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.service.ServicePagination;

public class ServletDashboard extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int size = 10;
	public int currentPage = 1;

	private ServiceComputer serviceComputer = ServiceComputer.getInstance();
	private ServicePagination servicePagination = ServicePagination.getInstance();

	private static final Logger logger = LoggerFactory.getLogger(ServletDashboard.class);


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setPageAttribute(request);
		setSizeAttribute(request);
		setLastPageAndPaginationAttribute(request);
		setComputerCount(request);
		try {
			setComputersAttribute(request, response);
			this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
		} catch (PageNotFoundException e) {
			logger.info("Page {} not found", currentPage);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void setPageAttribute(HttpServletRequest request) {
		currentPage = servicePagination.getCurrentPage(request.getParameter("page"));
		request.setAttribute("currentPage", currentPage);
	}

	private void setSizeAttribute(HttpServletRequest request) {
		size = servicePagination.getSize(request.getParameter("size"), size);
		request.setAttribute("size", size);
	}

	private void setLastPageAndPaginationAttribute(HttpServletRequest request) {
		int lastPage = serviceComputer.lastPage(size);
		request.setAttribute("lastPage", lastPage);

		int pagination = servicePagination.getPagination(lastPage, currentPage);
		request.setAttribute("pagination", pagination);
	}

	private void setComputersAttribute(HttpServletRequest request, HttpServletResponse response) throws IOException, PageNotFoundException{
			Page<DTOComputer> computers = serviceComputer.listWithNames(currentPage-1,size);
			request.setAttribute("computers", computers.getList());
	}
	
	private void setComputerCount(HttpServletRequest request) {
		int count = serviceComputer.count();
		request.setAttribute("count", count);
	}
}
