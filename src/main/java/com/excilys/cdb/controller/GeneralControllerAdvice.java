package com.excilys.cdb.controller;

import javax.servlet.http.HttpServletRequest;

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
	static final String ERROR_MESSAGE = "errorMessage";
	
	@GetMapping("/errors")
	public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
		
		ModelAndView model = new ModelAndView(PAGE_NAME);
		model.addObject(ERROR_CODE, 404);
		model.addObject(ERROR_MESSAGE, "Resource not found");
		return model;
	}

	@ExceptionHandler(ComputerNotFoundException.class)
    public ModelAndView handleComputerNotFoundException(ComputerNotFoundException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 404);
        model.addObject(ERROR_MESSAGE, e.getMessage());
        return model;
    }
	
	@ExceptionHandler(CompanyNotFoundException.class)
    public ModelAndView handleCompanyNotFoundException(CompanyNotFoundException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 404);
        model.addObject(ERROR_MESSAGE, e.getMessage());
        return model;
    }
	
	@ExceptionHandler(NotAValidComputerException.class)
    public ModelAndView handleNotAValidComputerException(NotAValidComputerException e) {
 
        ModelAndView model = new ModelAndView(PAGE_NAME);
        model.addObject(ERROR_CODE, 500);
        model.addObject(ERROR_MESSAGE, e.getMessage());
        return model;
    }
	
	
}
