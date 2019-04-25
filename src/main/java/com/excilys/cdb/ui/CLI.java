package com.excilys.cdb.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.excilys.cdb.controller.ChoiceMenuEnum;
import com.excilys.cdb.controller.Controller;
import com.excilys.cdb.controller.PageMenuEnum;
import com.excilys.cdb.exception.NotAChoiceException;
import com.excilys.cdb.exception.NotAnIntegerException;
import com.excilys.cdb.exception.WrongComputerArgumentException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.model.Page;

public class CLI {

	private Controller controller = new Controller();

	/**
	 * Welcome the user to use the CLI
	 */
	public void welcome() {
		CLIHelper.box("Welcome to the Computer Database Interface");
	}

	/**
	 * Help the user if he enter a bad choice
	 * @param maxChoiceNumber maximum number of choice
	 */
	public void boxUsage(int maxChoiceNumber) {
		CLIHelper.box("Please enter an integer between 1 and " + maxChoiceNumber);
	}

	/**
	 * Show the menu and wait the choice of the user
	 */
	public void startChoice() {
		int choice = 0;
		String[] choiceMenu = Stream.of(ChoiceMenuEnum.values()).map(ChoiceMenuEnum::getMenuMessage).toArray(String[]::new);
		while(choice==0) {
			try {
				CLIHelper.box(choiceMenu,true);
				choice = CLIHelper.choice(choiceMenu);
			} catch (NotAChoiceException e) {
				boxUsage(choiceMenu.length);
			}
		}
		controller.sendToService(this,ChoiceMenuEnum.values()[choice-1]);
	}

	/**
	 * Show a list of companies
	 * @param companies list of companies
	 */
	public void showCompanies(Page<DTOCompany> companyPage) {
		int choice = -1;
		boolean showPage = true;
		while (choice != 6) {
			if(showPage) {
				String[] headers = {"ID","Company Name"};
				CLIHelper.listCompaniesHelper(headers, companyPage.getList());
			} 
			String[] menu = Stream.of(PageMenuEnum.values()).map(PageMenuEnum::getMenuMessage).toArray(String[]::new);
			CLIHelper.box(menu,true);
			try {
				choice = CLIHelper.choice(menu);
				showPage = controller.sendToServiceCompany(this, PageMenuEnum.values()[choice-1], companyPage);
			} catch (NotAChoiceException e) {
				boxUsage(menu.length);
			}
		}
	}

	/**
	 * Show a list of computers
	 * @param computers list of computers
	 */
	public void showComputers(Page<DTOComputer> computerPage) {
		int choice = -1;
		boolean showPage = true;

		while (choice != 6) {
			if(showPage) {
				String[] headers = {"ID", "Computer Name", "Introduced", "Discontinued", "Company Id"};
				CLIHelper.listComputersHelper(headers, computerPage.getList());
			}
			String[] menu = Stream.of(PageMenuEnum.values()).map(PageMenuEnum::getMenuMessage).toArray(String[]::new);
			CLIHelper.box(menu,true);
			try {
				choice = CLIHelper.choice(menu);
				showPage = controller.sendToServiceComputer(this, PageMenuEnum.values()[choice-1], computerPage);
			} catch (NotAChoiceException e) {
				boxUsage(menu.length);
			}
		}
	}

	/**
	 * Show one computer
	 * @param computer
	 */
	public void showComputerDetails(DTOComputer computer) {
		List<DTOComputer> computers = new ArrayList<DTOComputer>(Arrays.asList(computer));
		String[] headers = {"ID", "Computer Name", "Introduced", "Discontinued", "Company Name"};
		CLIHelper.listComputersHelper(headers, computers);
	}

	/**
	 * Ask the user to enter an integer
	 * @param message The message that the user will see
	 * @return the integer the user enter
	 */
	public int askInteger(String message) {
		int id = -1;
		while(id == -1) {
			try {
				CLIHelper.box(message);
				id = CLIHelper.askIntegerHelper();
			} catch(NotAnIntegerException e) {
				CLIHelper.box("This is not an Integer !");
			}
		}
		return id;
	}  

	/**
	 * Ask the user to enter a new Computer
	 */
	public Optional<DTOComputer> askComputerCreationInformation(){
		String[] messages = {"Please enter a new Computer (Name) or (Name, Introduced Date,Discontinued Date, Company Id).",
				"Use a \",\" as separator between fields. Name is mandatory, the rest is optional.",
		"Date must be with the following format : \"dd-mm-yyyy\" and start at 02-01-1970."};
		CLIHelper.box(messages, false);
		Optional<DTOComputer> computer = Optional.empty();
		try {
			computer =  Optional.ofNullable(CLIHelper.askComputer());
		} catch (WrongComputerArgumentException e) {
			printException(e);
		}
		return computer;
	}

	/**
	 * Ask the user to enter the id of the Computer he want to update
	 */
	public int askComputerUpdateId() {
		return askInteger("Please enter the Id of the Computer you want to Update.");
	}

	/**
	 * Print message after insertion of a computer
	 * @param isInserted if the computer is successfully inserted or not
	 */
	public void printCreatedMessage(boolean isInserted) {
		if(isInserted) {
			CLIHelper.box("Computer inserted");
		} else {
			CLIHelper.box("Error. Please ensure that you respect the rules above.");
		}
	}

	/**
	 * Print message after updating a computer
	 * @param isUpdated if the computer is successfully updated or not
	 */
	public void printUpdatedMessage(boolean isUpdated) {
		if(isUpdated) {
			CLIHelper.box("Computer updated");
		} else {
			String[] messages = {"Error. Please ensure that the computer exist","or that you followed the creation above."};
			CLIHelper.box(messages,false);
		}
	}

	/**
	 * Print message after deletion of a computer
	 * @param isDeleted
	 * @param id of the computer
	 */
	public void printDeletedMessage(boolean isDeleted, int id) {
		if(isDeleted) {
			CLIHelper.box("Computer " + id + " deleted");
		} else {
			CLIHelper.box("Error. Please ensure that the computer " + id + " exist.");
		}
	}

	/**
	 * Print exception in a box
	 * @param e
	 */
	public void printException(Exception e) {
		CLIHelper.box(e.getMessage());
	}






}

