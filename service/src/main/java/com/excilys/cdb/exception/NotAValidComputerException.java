package com.excilys.cdb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotAValidComputerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAValidComputerException(String message) {
		super(message);
	}
}
