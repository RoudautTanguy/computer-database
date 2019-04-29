package com.excilys.cdb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.service.ServiceComputer;

public class ServletDashboard extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int var = 10;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int count = ServiceComputer.getInstance().count();
		request.setAttribute("variable", count);
		this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
	}
}
