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
import com.excilys.cdb.persistence.OrderByEnum;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.service.ServicePagination;

public class ServletDashboard extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int size = 10;
	public int currentPage = 1;
	private OrderByEnum orderBy = OrderByEnum.DEFAULT;

	private ServiceComputer serviceComputer = ServiceComputer.getInstance();
	private ServicePagination servicePagination = ServicePagination.getInstance();

	private static final Logger logger = LoggerFactory.getLogger(ServletDashboard.class);


	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		String search = request.getParameter("search")==null?"":request.getParameter("search");
		setOrderByAttribute(request);
		setPageAttribute(request);
		setSizeAttribute(request);
		int lastPage = setLastPage(request, search);
		setPaginationAttribute(request, lastPage);
		setComputerCount(request, search);
		try {
			try {
				setComputersAttribute(request, response, search, orderBy);
				this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
			} catch (ServletException e) {
				logger.error("Target resource throws an exception",e);
			} catch (IOException e) {
				logger.error("Input or output exception occurs",e);
			}
			
		} catch (PageNotFoundException e) {
			logger.info("Page {} not found", currentPage);
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				logger.error("Input or output exception occurs",e1);
			}
		}
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
		String[] idComputers = request.getParameter("selection").split(",");
		for(String id:idComputers) {
			try {
				serviceComputer.delete(Integer.parseInt(id));
			} catch (NumberFormatException e) {
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (IOException e1) {
					logger.error("Input or output exception occurs",e1);
				}
			} catch (ComputerNotFoundException e) {
				try {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} catch (IOException e1) {
					logger.error("Input or output exception occurs",e1);
				}
			} catch (CantConnectException e) {
				logger.error("Can't connect",e);
			}
		}
		int lastPage = setLastPage(request,"");
		try {
			response.sendRedirect(request.getContextPath() + "/dashboard?page="+lastPage);
		} catch (IOException e) {
			logger.error("Input or output exception occurs",e);
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

	private int setLastPage(HttpServletRequest request, String search) {
		int lastPage = serviceComputer.lastPage(size,search);
		request.setAttribute("lastPage", lastPage);
		return lastPage;
	}
	
	private void setPaginationAttribute(HttpServletRequest request, int lastPage) {
		int pagination = servicePagination.getPagination(lastPage, currentPage);
		request.setAttribute("pagination", pagination);
	}

	private void setComputersAttribute(HttpServletRequest request, HttpServletResponse response, String search, OrderByEnum orderBy) throws IOException, PageNotFoundException{
			Page<DTOComputer> computers = serviceComputer.search(currentPage-1, size, search, orderBy);
			request.setAttribute("computers", computers.getList());
	}
	
	private void setComputerCount(HttpServletRequest request, String search) {
		int count = serviceComputer.count(search);
		request.setAttribute("count", count);
	}
	
	private void setOrderByAttribute(HttpServletRequest request) {
		String requestParam = request.getParameter("orderBy");
		try{
			int indexOrderBy = Integer.parseInt(requestParam);
			if(indexOrderBy>=0 && indexOrderBy<OrderByEnum.values().length) {
				orderBy = OrderByEnum.values()[indexOrderBy];
			}
		} catch(NumberFormatException e) {
			logger.warn("Cant parse {} to integer", requestParam);
		} finally {
			request.setAttribute("orderBy", orderBy.ordinal());
		}
	}
}
