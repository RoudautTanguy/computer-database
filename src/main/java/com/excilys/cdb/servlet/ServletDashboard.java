package com.excilys.cdb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
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
		String search = request.getParameter("search")==null?"":request.getParameter("search");
		setPageAttribute(request);
		setSizeAttribute(request);
		int lastPage = setLastPage(request, search);
		setPaginationAttribute(request, lastPage);
		setComputerCount(request, search);
		try {
			setComputersAttribute(request, response, search);
			this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
		} catch (PageNotFoundException e) {
			logger.info("Page {} not found", currentPage);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] idComputers = request.getParameter("selection").split(",");
		for(String id:idComputers) {
			try {
				serviceComputer.delete(Integer.parseInt(id));
			} catch (NumberFormatException | CantConnectException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (ComputerNotFoundException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		int lastPage = setLastPage(request,"");
		response.sendRedirect(request.getContextPath() + "/dashboard?page="+lastPage);
	}

	private void setPageAttribute(HttpServletRequest request) {
		currentPage = servicePagination.getCurrentPage(request.getParameter("page"));
		request.setAttribute("currentPage", currentPage);
	}

	private void setSizeAttribute(HttpServletRequest request) {
		size = servicePagination.getSize(request.getParameter("size"), size);
		request.setAttribute("size", size);
	}

	private int setLastPage(HttpServletRequest request, String search) {
		int lastPage = serviceComputer.lastPage(size,search);
		request.setAttribute("lastPage", lastPage);
		return lastPage;
	}
	
	private void setPaginationAttribute(HttpServletRequest request, int lastPage) {
		int pagination = servicePagination.getPagination(lastPage, currentPage);
		request.setAttribute("pagination", pagination);
	}

	private void setComputersAttribute(HttpServletRequest request, HttpServletResponse response, String search) throws IOException, PageNotFoundException{
			Page<DTOComputer> computers = serviceComputer.search(currentPage-1, size, search);
			request.setAttribute("computers", computers.getList());
	}
	
	private void setComputerCount(HttpServletRequest request, String search) {
		int count = serviceComputer.count(search);
		request.setAttribute("count", count);
	}
}
