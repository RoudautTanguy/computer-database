package com.excilys.cdb.controller;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.ui.CLI;

public class Controller {
	
	private ServiceCompany serviceCompany = new ServiceCompany();
	private ServiceComputer serviceComputer = new ServiceComputer();
	private static Controller controller;
	
	public void sendToService(int choice) {
		switch(choice) {
		case 1:
			CLI.getInstance().showComputers(serviceComputer.list());
			CLI.getInstance().startChoice();
			break;
		case 2:
			CLI.getInstance().showCompanies(serviceCompany.list());
			CLI.getInstance().startChoice();
			break;
		case 3:
			int id = CLI.getInstance().askDetails();
			try {
				CLI.getInstance().showComputerDetails(serviceComputer.find(id));
			} catch (ComputerNotFoundException e) {
				CLI.getInstance().printException(e);
			} finally {
				CLI.getInstance().startChoice();
			}
			break;
		case 4:
			CLI.getInstance().askComputerCreationInformation();
			break;
		case 5:
			CLI.getInstance().askComputerUpdateInformation();
			break;
		case 6:
			CLI.getInstance().askComputerDeletionInformation();
			break;
		}
	}
	
	public static Controller getInstance() {
		if(controller == null) {
			controller =  new Controller();
		}
		return controller;
	}

}
