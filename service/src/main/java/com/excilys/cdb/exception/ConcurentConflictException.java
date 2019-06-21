package com.excilys.cdb.exception;

public class ConcurentConflictException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConcurentConflictException(String message) {
		super(message);
	}

}
