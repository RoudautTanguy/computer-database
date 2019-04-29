package com.excilys.cdb.controller;

import java.util.Optional;

import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.ui.CLI;

public class Controller {
	
	private ServiceCompany serviceCompany = new ServiceCompany();
	private ServiceComputer serviceComputer = new ServiceComputer();
	private static Controller controller;
	
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
	
	/**
	 * Send the choice of the user to the dedicated service
	 * @param choice of the user
	 */
	public void sendToService(CLI cli, ChoiceMenuEnum choice) {
		switch(choice) {
		case LIST_COMPUTERS:
			try {
				cli.showComputers(serviceComputer.list());
			} catch (PageNotFoundException e1) {
				e1.printStackTrace();
			}
			break;
			
		case LIST_COMPANIES:
			try {
				cli.showCompanies(serviceCompany.list());
			} catch (PageNotFoundException e1) {
				e1.printStackTrace();
			}
			break;
			
		case SHOW_COMPUTER_DETAIL:
			int detailId = cli.askInteger("Please enter an Id (Integer)");
			try {
				DTOComputer computer = serviceComputer.find(detailId);
				cli.showComputerDetails(computer);
			} catch (ComputerNotFoundException e) {
				cli.printException(e);
			}
			break;
			
		case CREATE_COMPUTER:
			DTOComputer computer;
			try {
				Optional<DTOComputer> optionalComputer = cli.askComputerCreationInformation();
				if(optionalComputer.isPresent()) {
					computer = optionalComputer.get();
					cli.printCreatedMessage(serviceComputer.insert(computer));
				}
			} catch (NotAValidComputerException e) {
				cli.printException(e);
			}
			break;
			
		case UPDATE_COMPUTER:
			DTOComputer dtoComputer;
			try {
				int id = cli.askComputerUpdateId();
				Optional<DTOComputer> optionalComputer = cli.askComputerCreationInformation();
				if(optionalComputer.isPresent()) {
					dtoComputer = optionalComputer.get();
					cli.printUpdatedMessage(serviceComputer.update(id, dtoComputer));
				}
			} catch(IllegalArgumentException e) {
				cli.printException(e);
			} catch (NotAValidComputerException e) {
				cli.printException(e);
			}
			break;
			
		case DELETE_COMPUTER:
			int deleteId = cli.askInteger("Please enter the Id of the Computer you want to Delete.");
			cli.printDeletedMessage(serviceComputer.delete(deleteId), deleteId);
			break;
		}
		cli.startChoice();
	}
	
	/**
	 * 
	 * @param <T>
	 * @param cli
	 * @param choice
	 * @param page
	 * @return
	 */
	public boolean sendToServiceCompany(CLI cli, PageMenuEnum choice, Page<DTOCompany> page) {
		boolean isOk = true;
		switch(choice) { 
		case PREVIOUS_PAGE:
			try {
				page.setList(ServiceCompany.getInstance().list(page.decrementIndex()).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.incrementIndex();
				cli.printException(e);
			}
			break;
			
		case NEXT_PAGE:
			try {
				page.setList(ServiceCompany.getInstance().list(page.incrementIndex()).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.decrementIndex();
				cli.printException(e);
			}
			break;
		case SET_PAGE:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				page.setList(ServiceCompany.getInstance().list(pageNumber).getList());
				page.setIndex(pageNumber);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case FIRST_PAGE:
			try {
				page.setList(ServiceCompany.getInstance().list(0).getList());
				page.setIndex(0);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case LAST_PAGE:
			try {
				int lastSlice = ServiceCompany.getInstance().getLastPage();
				page.setList(ServiceCompany.getInstance().list(lastSlice).getList());
				page.setIndex(lastSlice);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case QUIT:
			cli.startChoice();
			break;
		}
		return isOk;
	}
	
	public boolean sendToServiceComputer(CLI cli, PageMenuEnum choice, Page<DTOComputer> page) {
		boolean isOk = true;
		switch(choice) { 
		case PREVIOUS_PAGE:
			try {
				page.setList(ServiceComputer.getInstance().list(page.decrementIndex()).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.incrementIndex();
				cli.printException(e);
			}
			break;
			
		case NEXT_PAGE:
			try {
				page.setList(ServiceComputer.getInstance().list(page.incrementIndex()).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.decrementIndex();
				cli.printException(e);
			}
			break;
			
		case SET_PAGE:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				page.setList(ServiceComputer.getInstance().list(pageNumber).getList());
				page.setIndex(pageNumber);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case FIRST_PAGE:
			try {
				page.setList(ServiceComputer.getInstance().list(0).getList());
				page.setIndex(0);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case LAST_PAGE:
			try {
				int lastSlice = ServiceComputer.getInstance().count();
				page.setList(ServiceComputer.getInstance().list(lastSlice).getList());
				page.setIndex(lastSlice);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case QUIT:
			cli.startChoice();
			break;
		}
		return isOk;
	}

}
