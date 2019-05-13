package com.excilys.cdb.constant;

public final class Constant {
	
	private Constant() {
	    throw new IllegalStateException("Utility class");
	  }
	
	public static final String CANT_CONNECT = "Can't connect.";
	
	public static final String PAGE_DOESNT_EXIST = "This page doesn't exist";

	public static final String CANT_CLOSE_RESULT_SET = "Can't close ResultSet";
	
	public static final String NAME_IS_MANDATORY = "The name is Mandatory.";
	
	public static final String CHOICE_NOT_POSSIBLE_FORMAT = "Le choix %s n'est pas possible !";
	
	public static final String SANITIZER_REPLACER = "[\n|\r|\t]";
	
	public static final String FRONT_VALIDATION_PREVENT_WRONG_DATE = "Front validation don't prevent inserting computer with wrong date dormat";
	
	public static final String NUMBER_OF_ELEMENT_NOT_CORRECT = "Number of element in list isn't correct.";
}
