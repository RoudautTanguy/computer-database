package com.excilys.cdb.controller;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.ui.CLI;

public class Controller {
	
	private ServiceCompany serviceCompany = new ServiceCompany();
	private ServiceComputer serviceComputer = new ServiceComputer();
	private static Controller controller;
	
	public void sendToService(int choice) {
		CLI cli = CLI.getInstance();
		switch(choice) {
		case 1:
			cli.showComputers(serviceComputer.list());
			break;
		case 2:
			cli.showCompanies(serviceCompany.list());
			break;
		case 3:
			int detailId = cli.askInteger("Please enter an Id (Integer)");
			try {
				cli.showComputerDetails(serviceComputer.find(detailId));
			} catch (ComputerNotFoundException e) {
				cli.printException(e);
			}
			break;
		case 4:
			DTOComputer computer;
			try {
				computer = cli.askComputerCreationInformation();
				cli.printCreatedMessage(serviceComputer.insert(computer));
			} catch(IllegalArgumentException e) {
				cli.printException(e);
			}
			break;
		case 5:
			int id = cli.askComputerUpdateId();
			cli.printUpdatedMessage(serviceComputer.update(id, cli.askComputerCreationInformation()));
			break;
		case 6:
			int deleteId = cli.askInteger("Please enter the Id of the Computer you want to Delete.");
			cli.printDeletedMessage(serviceComputer.delete(deleteId), deleteId);
			break;
		}
		cli.startChoice();
	}
	
	public static Controller getInstance() {
		if(controller == null) {
			controller =  new Controller();
		}
		return controller;
	}

}
