package com.excilys.cdb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.main.AppConfig;
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
	
	private static final String IO_EXCEPTION_MESSAGE = "Input or output exception occurs";

	private final ServiceComputer serviceComputer;
	private final ServicePagination servicePagination;
	
	public ServletDashboard() {
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
			serviceComputer = context.getBean(ServiceComputer.class);
			servicePagination = context.getBean(ServicePagination.class);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ServletDashboard.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		String search = request.getParameter("search")==null?"":request.getParameter("search");
		
		OrderByEnum orderBy = setOrderByAttribute(request);
		int currentPage = setPageAttribute(request);
		int size = setSizeAttribute(request);
		int lastPage = setLastPage(request, size, search);
		setPaginationAttribute(request, currentPage, lastPage);
		setComputerCount(request, search);
		try {
			Page<DTOComputer> page = new Page<>(null,currentPage-1,size,search);
			setComputersAttribute(request, page, orderBy);
			forward("/WEB-INF/views/dashboard.jsp",request, response);
		} catch (PageNotFoundException e) {
			logger.info("Page {} not found", currentPage);
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				logger.error(IO_EXCEPTION_MESSAGE,e1);
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
					logger.error(IO_EXCEPTION_MESSAGE,e1);
				}
			} catch (ComputerNotFoundException e) {
				try {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				} catch (IOException e1) {
					logger.error(IO_EXCEPTION_MESSAGE,e1);
				}
			} catch (CantConnectException e) {
				logger.error(Constant.CANT_CONNECT,e);
			}
		}
		int size = setSizeAttribute(request);
		int lastPage = setLastPage(request,size,"");
		try {
			response.sendRedirect(request.getContextPath() + "/dashboard?page="+lastPage);
		} catch (IOException e) {
			logger.error(IO_EXCEPTION_MESSAGE,e);
		}
	}

	private int setPageAttribute(HttpServletRequest request) {
		int currentPage = servicePagination.getCurrentPage(request.getParameter("page"));
		request.setAttribute("currentPage", currentPage);
		return currentPage;
	}

	private int setSizeAttribute(HttpServletRequest request) {
		int size = servicePagination.getSize(request.getParameter("size"));
		request.setAttribute("size", size);
		return size;
	}

	private int setLastPage(HttpServletRequest request, int size, String search) {
		int lastPage = serviceComputer.lastPage(size,search);
		request.setAttribute("lastPage", lastPage);
		return lastPage;
	}
	
	private void setPaginationAttribute(HttpServletRequest request, int currentPage, int lastPage) {
		int pagination = servicePagination.getPagination(lastPage, currentPage);
		request.setAttribute("pagination", pagination);
	}

	private void setComputersAttribute(HttpServletRequest request, Page<DTOComputer> page, OrderByEnum orderBy) throws PageNotFoundException{
			Page<DTOComputer> computers = serviceComputer.search(page.getIndex(), page.getLimit(), page.getSearch(), orderBy);
			request.setAttribute("computers", computers.getList());
	}
	
	private void setComputerCount(HttpServletRequest request, String search) {
		int count = serviceComputer.count(search);
		request.setAttribute("count", count);
	}
	
	private OrderByEnum setOrderByAttribute(HttpServletRequest request) {
		OrderByEnum orderBy = OrderByEnum.DEFAULT;
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
		return orderBy;
	}
	
	private void forward(String url, HttpServletRequest request, HttpServletResponse response) {
		try {
			this.getServletContext().getRequestDispatcher( url ).forward( request, response );
		} catch (ServletException e) {
			logger.error("Target resource throws an exception",e);
		} catch (IOException e) {
			logger.error(IO_EXCEPTION_MESSAGE,e);
		}
	}
}
