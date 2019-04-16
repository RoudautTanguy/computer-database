package com.excilys.cdb.ui;

import java.util.List;
import java.util.Scanner;

import com.excilys.cdb.controller.Controller;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAChoiceException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.DTOComputer;

public class CLI {
	
	private final int CHOICE_NUMBER = 6;
	private final String WELCOME = "+--------------------------------------------+\n"
							     + "| Welcome to the Computer Database Interface |\n"
							     + "+--------------------------------------------+";
	
	private final String USAGE = "+-----------------------------------------+\n"
							   + "| Please enter an integer between 1 and " + CHOICE_NUMBER + " |\n"
						       + "+-----------------------------------------+";
	
	private final String COMPANY_LIST_FORMAT = "| %-4s | %-45s |%n";
	private final String COMPANY_SEPARATOR = "+------+-----------------------------------------------+";
	private final String COMPUTER_LIST_FORMAT = "| %-4s | %-65s | %-15s | %-15s | %-10s |%n";
	private final String COMPUTER_SEPARATOR = "+------+-------------------------------------------------------------------+-----------------+-----------------+------------+";
	
	private Scanner in = new Scanner(System.in);
	private Controller controller = new Controller();
	
	private static CLI cli;
	
	public void welcome() {
		System.out.println(WELCOME);
	}
	
	private void usage() {
		System.out.println(USAGE);
	}
	
	public void startChoice() {
		int choice = 0;
		while(choice==0) {
			try {
				choice = this.choice();
			} catch (NotAChoiceException e) {
				this.usage();
			}
		}
		controller.sendToService(choice);
	}
	
	private void printCompanyHeader() {
		System.out.println(COMPANY_SEPARATOR);
		System.out.println("| ID   | Company Name                                  |");
		System.out.println(COMPANY_SEPARATOR);
	}
	
	public void showCompanies(List<DTOCompany> companies) {
		this.printCompanyHeader();
		for(DTOCompany company:companies) {
			System.out.format(COMPANY_LIST_FORMAT, company.getId(), company.getName());
		}
		System.out.println(COMPANY_SEPARATOR);
	}
	
	private void printComputerHeader() {
		System.out.println(COMPUTER_SEPARATOR);
		System.out.println("| ID   | Computer Name                                                     | Introduced      | Discontinued    | Company ID |");
		System.out.println(COMPUTER_SEPARATOR);
	}
	
	public void showComputers(List<DTOComputer> computers) {
		printComputerHeader();
		for(DTOComputer computer:computers) {
			String id = computer.getId();
			String name = computer.getName();
			String introduced = computer.getIntroduced();
			String discontinued = computer.getDiscontinued();
			String companyId = computer.getCompanyId();
			System.out.format(COMPUTER_LIST_FORMAT, id, name, introduced, discontinued, companyId);
		}
		System.out.println(COMPUTER_SEPARATOR);
	}
	
	private int choice() throws NotAChoiceException{
		System.out.println("+--------------------------+");
		System.out.println("| 1. List computers        |");
		System.out.println("| 2. List companies        |");
		System.out.println("| 3. Show computer details |");
		System.out.println("| 4. Create a computer     |");
		System.out.println("| 5. Update a computer     |");
		System.out.println("| 6. Delete a computer     |");
		System.out.println("+--------------------------+");
		String choice = in.nextLine();
		try {
			int intChoice = Integer.parseInt(choice); // If this doesn't fail then it's integer
			if(intChoice>=1 && intChoice<=6) {
				return intChoice;
			} else {
				throw new NotAChoiceException("Le choix "+choice + " n'est pas possible !");
			}
	    } catch(NumberFormatException e) {
	    	throw new NotAChoiceException("Le choix " + choice + " n'est pas possible !");
	    }
	}
	
	public static CLI getInstance() {
		if(cli == null){
			cli = new CLI();
	    }
	    	return cli;   
		}

	public int askDetails() {
		int id = 0;
		while(id==0) {
			try {
				id = askInteger();
			} catch(NumberFormatException e) {
				System.out.println("+------------------------+\n"
				 		 		 + "| This is not an Integer |\n"
				 		 		 + "+------------------------+");
			}
		}
		return id;
	}  
	
	private int askInteger() throws NumberFormatException{
		System.out.println("+------------------------------+\n"
				 		 + "| Please enter an Id (Integer) |\n"
				 		 + "+------------------------------+");
		String integer = in.nextLine();
		int intChoice = Integer.parseInt(integer); // If this doesn't fail then it's integer
		return intChoice;
	}

	public void askComputerCreationInformation() {
		// TODO Auto-generated method stub
		
	}

	public void askComputerUpdateInformation() {
		// TODO Auto-generated method stub
		
	}

	public void askComputerDeletionInformation() {
		// TODO Auto-generated method stub
		
	}

	public void showComputerDetails(DTOComputer computer) {
		printComputerHeader();
		String id = computer.getId();
		String name = computer.getName();
		String introduced = computer.getIntroduced();
		String discontinued = computer.getDiscontinued();
		String companyId = computer.getCompanyId();
		System.out.format(COMPUTER_LIST_FORMAT, id, name, introduced, discontinued, companyId);
		System.out.println(COMPUTER_SEPARATOR);
	}

	public void printException(ComputerNotFoundException e) {
		System.err.println(e.getMessage());
	}
	
}
