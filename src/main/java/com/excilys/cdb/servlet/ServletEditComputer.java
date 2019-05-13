package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;

public class ServletEditComputer extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String IO_EXCEPTION_MESSAGE = "Input or output exception occurs";

	private static ServiceComputer serviceComputer = ServiceComputer.getInstance();
	private static ServiceCompany serviceCompany = ServiceCompany.getInstance();

	private static final Logger logger = LoggerFactory.getLogger(ServletEditComputer.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		int id = -1;
		try{
			id = Integer.parseInt(request.getParameter("id"));
			request.setAttribute("id", id);
			DTOComputer computer = serviceComputer.find(id);
			request.setAttribute("computer", computer);

			List<DTOCompany> companies = serviceCompany.list();
			request.setAttribute("companies", companies);
			this.getServletContext().getRequestDispatcher( "/WEB-INF/views/editComputer.jsp" ).forward( request, response );
		} catch (NumberFormatException | ComputerNotFoundException e) {
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				logger.error(IO_EXCEPTION_MESSAGE,e1);
			}
		} catch (ServletException e) {
			logger.error("Target resource throws an exception",e);
		} catch (IOException e) {
			logger.error(IO_EXCEPTION_MESSAGE,e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String computerName = request.getParameter("computerName");
		String introduced = request.getParameter("introduced");
		String discontinued = request.getParameter("discontinued");
		String companyId = request.getParameter("companyId");
		DTOComputer dtoComputer = new DTOComputer.DTOComputerBuilder(computerName)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompany(companyId)
				.build();
		try {
			serviceComputer.update(Integer.parseInt(id),dtoComputer);
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (NotAValidComputerException e) {
			logger.warn("Computer not found",e);
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				logger.error(IO_EXCEPTION_MESSAGE,e1);
			}
		} catch(NumberFormatException e) {

			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				logger.error(IO_EXCEPTION_MESSAGE,e1);
			}
		} catch (IOException e) {
			logger.error(IO_EXCEPTION_MESSAGE,e);
		} catch (ComputerNotFoundException e) {
			logger.warn("Computer not found",e);
		} catch (CantConnectException e) {
			logger.warn("Can't connect",e);
		}
	}

}
