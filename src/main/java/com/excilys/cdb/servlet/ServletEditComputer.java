package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;

public class ServletEditComputer extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ServiceComputer serviceComputer = ServiceComputer.getInstance();
	private ServiceCompany serviceCompany = ServiceCompany.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = -1;
		try{
			id = Integer.parseInt(request.getParameter("id"));
			request.setAttribute("id", id);
			DTOComputer computer = serviceComputer.find(id);
			request.setAttribute("computer", computer);
			
			List<DTOCompany> companies = serviceCompany.list();
			request.setAttribute("companies", companies);
			this.getServletContext().getRequestDispatcher( "/WEB-INF/views/editComputer.jsp" ).forward( request, response );
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (ComputerNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
	}

}
