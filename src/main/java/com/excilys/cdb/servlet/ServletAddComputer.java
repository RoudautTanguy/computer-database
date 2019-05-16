package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.main.AppConfig;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;

public class ServletAddComputer extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ServletAddComputer.class);
	
	private static final String IO_EXCEPTION_MESSAGE = "Input or output exception occurs";
	
	private final ServiceCompany serviceCompany;
	private final ServiceComputer serviceComputer;
	
	public ServletAddComputer() {
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
			serviceCompany = context.getBean(ServiceCompany.class);
			serviceComputer = context.getBean(ServiceComputer.class);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		List<DTOCompany> companies = serviceCompany.list();
		request.setAttribute("companies", companies);
		try {
			this.getServletContext().getRequestDispatcher( "/WEB-INF/views/addComputer.jsp" ).forward( request, response );
		} catch (ServletException e) {
			logger.error("Target resource throws an exception",e);
		} catch (IOException e) {
			logger.error(IO_EXCEPTION_MESSAGE,e);
		}
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String computerName = request.getParameter("computerName");
		computerName = computerName.replaceAll("[\n|\r|\t]", "_");
		String introduced = request.getParameter("introduced");
		String discontinued = request.getParameter("discontinued");
        String companyId = request.getParameter("companyId");
        DTOComputer dtoComputer = new DTOComputer.DTOComputerBuilder(computerName)
        										 .withIntroduced(introduced)
        										 .withDiscontinued(discontinued)
        										 .withCompany(companyId)
        										 .build();
        try {
        	serviceComputer.insert(dtoComputer);
			response.sendRedirect(request.getContextPath() + "/dashboard");
		} catch (NotAValidComputerException e) {
			logger.warn("This computer is not valid", e);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				logger.error(IO_EXCEPTION_MESSAGE,e1);
			}
		} catch (IOException e) {
			logger.error(IO_EXCEPTION_MESSAGE,e);
		}
	}

}
