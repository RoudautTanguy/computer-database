package com.excilys.cdb;

import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.ui.CLI;

public class Main {
	
	public static void main(String[] args) {
		CLI cli = new CLI();
	    cli.welcome();
	    try {
			cli.startChoice();
		} catch (ConcurentConflictException e) {
			e.printStackTrace();
		}
	}

}
