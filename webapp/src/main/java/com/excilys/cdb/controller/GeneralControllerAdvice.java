package com.excilys.cdb.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;

@Controller
@ControllerAdvice
public class GeneralControllerAdvice {
	
	static final String PAGE_NAME = "errorPage";
	static final String ERROR_CODE = "errorCode";
	static final String ERROR_MESSAGE = "errorMsg";
	
	@GetMapping("/errors")
	public ModelAndView renderErrorPage(HttpServletResponse httpResponse) {
		ModelAndView model = new ModelAndView(PAGE_NAME);
		int errorCode = httpResponse.getStatus();
		model.addObject(ERROR_CODE, errorCode);
		if(errorCode == 404) {
			model.addObject(ERROR_MESSAGE,"error.resources_not_found");
			model.addObject("info","error.404_info");
		} else if(errorCode == 403) {
			model.addObject(ERROR_MESSAGE,"error.forbidden");
		} 
		return model;
	}

	@ExceptionHandler(ComputerNotFoundException.class)
    public ModelAndView handleComputerNotFoundException(ComputerNotFoundException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 404);
        model.addObject(ERROR_MESSAGE, "error.computer_not_found");
        return model;
    }
	
	@ExceptionHandler(CompanyNotFoundException.class)
    public ModelAndView handleCompanyNotFoundException(CompanyNotFoundException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 404);
        model.addObject(ERROR_MESSAGE, "error.company_not_found");
        return model;
    }
	
	@ExceptionHandler(NotAValidComputerException.class)
    public ModelAndView handleNotAValidComputerException(NotAValidComputerException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 500);
        model.addObject(ERROR_MESSAGE, "error.internal_server_error");
        return model;
    }
	
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ModelAndView handle(AuthenticationCredentialsNotFoundException e) {
		ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 403);
        model.addObject(ERROR_MESSAGE, "error.forbidden");
        return model;
	}
	
	@ExceptionHandler(Exception.class)
    public ModelAndView handleException(NotAValidComputerException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 501);
        model.addObject(ERROR_MESSAGE, "error.not_implemented");
        return model;
    }
	
}
