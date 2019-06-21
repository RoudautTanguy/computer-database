package com.excilys.cdb.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.exceptions.BadRequestException;

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
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ResponseBody
    public Object handleComputerNotFoundException(ComputerNotFoundException e) {
		return e.getMessage();
    }
	
	@ExceptionHandler(CompanyNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ResponseBody
    public Object handleCompanyNotFoundException(CompanyNotFoundException e) {
		return e.getMessage();
    }
	
	@ExceptionHandler(PageNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object handlePageNotFound(PageNotFoundException e) {
        return e.getMessage();
    }
	
	@ExceptionHandler(NotAValidComputerException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleNotAValidComputerException(NotAValidComputerException e) {
		return e.getMessage();
    }
	
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object handleBadRequestException(BadRequestException e) {
		return e.getMessage();
    }
	
	@ExceptionHandler(ConcurentConflictException.class)
	@ResponseStatus(code = HttpStatus.CONFLICT)
    @ResponseBody
	public Object handleConcurentConflictException(ConcurentConflictException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
	public Object handle(AuthenticationCredentialsNotFoundException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleException(NotAValidComputerException e) {
		return e.getMessage();
    }
	
}
