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
	
	@GetMapping("/errors")
	public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
		
		ModelAndView model = new ModelAndView("errorPage");
		model.addObject("errorCode", 404);
		model.addObject("errorMsg", "Resource not found");
		return model;
	}

	@ExceptionHandler(ComputerNotFoundException.class)
    public ModelAndView handleComputerNotFoundException(ComputerNotFoundException e) {
 
        ModelAndView model = new ModelAndView("errorPage");
        model.addObject("errorCode", 404);
        model.addObject("errorMsg", e.getMessage());
        return model;
    }
	
	@ExceptionHandler(CompanyNotFoundException.class)
    public ModelAndView handleCompanyNotFoundException(CompanyNotFoundException e) {
 
        ModelAndView model = new ModelAndView("errorPage");
        model.addObject("errorCode", 404);
        model.addObject("errorMsg", e.getMessage());
        return model;
    }
	
	@ExceptionHandler(NotAValidComputerException.class)
    public ModelAndView handleNotAValidComputerException(NotAValidComputerException e) {
 
        ModelAndView model = new ModelAndView("errorPage");
        model.addObject("errorCode", 500);
        model.addObject("errorMsg", e.getMessage());
        return model;
    }
	
	
}
