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

public class ServletDashboard extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int size = 10;
	public int currentPage = 1;
	
	private ServiceComputer serviceComputer = ServiceComputer.getInstance();
	
	private static final Logger logger = LoggerFactory.getLogger(ServletDashboard.class);
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int page = 1;
		String pageParameter = request.getParameter("page");
		try {
			page = Integer.parseInt(pageParameter);
		} catch (NumberFormatException e) {
			logger.info("No parameter 'page'");
		} finally {
			currentPage = page;
			request.setAttribute("currentPage", currentPage);
		}
		
		String sizeParameter = request.getParameter("size");
		try {
			int oldSize = size;
			size = Integer.parseInt(sizeParameter);
			if(size != 10 && size != 50 && size !=100) {
				size = oldSize;
			} 
		} catch (NumberFormatException e) {
			logger.info("No parameter 'size'");
		} finally {
			request.setAttribute("size", size);
		}
		
		int lastPage = serviceComputer.lastPage(size);
		request.setAttribute("lastPage", lastPage);
		
		int pagination = Math.max(3, Math.min(lastPage -2, currentPage));
		request.setAttribute("pagination", pagination);
		
		try {
			Page<DTOComputer> computers = serviceComputer.listWithNames(page-1,size);
			request.setAttribute("computers", computers.getList());
			int count = serviceComputer.count();
			request.setAttribute("count", count);
			
			this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
		} catch (PageNotFoundException e) {
			logger.info("Page {} not found", page);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		
		
	}
}
