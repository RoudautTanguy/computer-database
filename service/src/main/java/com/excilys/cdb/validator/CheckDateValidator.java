package com.excilys.cdb.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excilys.cdb.dto.validator.CheckDateFormat;

public class CheckDateValidator implements ConstraintValidator<CheckDateFormat, String> {

	private String pattern;

	@Override
	public void initialize(CheckDateFormat constraintAnnotation) {
		this.pattern = constraintAnnotation.pattern();
	}

	@Override
	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if ( object == null ) {
			return true;
		}

		DateFormat dateFormat = new SimpleDateFormat( pattern ); 
		dateFormat.setLenient( false ); 
		try { 
			dateFormat.parse(object); return true;
		} catch (ParseException e) { 
			return false; 
		}
	}
}