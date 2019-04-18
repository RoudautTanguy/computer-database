package com.excilys.cdb.controller;

import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.SliceNotFoundException;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.service.ServicePage;
import com.excilys.cdb.ui.CLI;

public class Controller {
	
	private ServiceCompany serviceCompany = new ServiceCompany();
	private ServiceComputer serviceComputer = new ServiceComputer();
	private static Controller controller;
	
	/**
	 * Send the choice of the user to the dedicated service
	 * @param choice of the user
	 */
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
				DTOComputer computer = serviceComputer.find(detailId);
				cli.showComputerDetails(computer);
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
			} catch (CompanyNotFoundException e) {
				cli.printException(e);
			}
			break;
		case 5:
			DTOComputer dtoComputer;
			try {
				int id = cli.askComputerUpdateId();
				dtoComputer = cli.askComputerCreationInformation();
				cli.printUpdatedMessage(serviceComputer.update(id, dtoComputer));
			} catch(IllegalArgumentException e) {
				cli.printException(e);
			}
			break;
		case 6:
			int deleteId = cli.askInteger("Please enter the Id of the Computer you want to Delete.");
			cli.printDeletedMessage(serviceComputer.delete(deleteId), deleteId);
			break;
		}
		cli.startChoice();
	}
	
	/**
	 * Send the choice of the user to the page service.
	 * @param <T> Type of the page
	 * @param choice of the user
	 * @param page
	 * @return true if there is no error else false
	 */
	public <T> boolean sendToPageService(int choice, Page<T> page) {
		boolean isOk = true;
		CLI cli = CLI.getInstance();
		switch(choice) { 
		case 1:
			try {
				ServicePage.changeSliceToPrevious(page);
			} catch (SliceNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
		case 2:
			try {
				ServicePage.changeSliceToNext(page);
			} catch (SliceNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
		case 3:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				ServicePage.setSlice(page, pageNumber);
			} catch (SliceNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
		case 4:
			try {
				ServicePage.setSlice(page, 0);
			} catch (SliceNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
		case 5:
			try {
				ServicePage.setSlice(page, page.lastSlice());
			} catch (SliceNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
		case 6:
			cli.startChoice();
			break;
		}
		return isOk;
	}
	
	/**
	 * Get the instance of Controller
	 * @return the instance of Controller
	 */
	public static Controller getInstance() {
		if(controller == null) {
			controller =  new Controller();
		}
		return controller;
	}

}
