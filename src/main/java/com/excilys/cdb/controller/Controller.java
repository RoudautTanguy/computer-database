package com.excilys.cdb.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.CantConnectException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.mapper.DTOCompany;
import com.excilys.cdb.mapper.DTOComputer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.OrderByEnum;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.ui.CLI;

public class Controller {
	
	private ServiceCompany serviceCompany = ServiceCompany.getInstance();
	private ServiceComputer serviceComputer = ServiceComputer.getInstance();
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	private static Controller instance;
	
	/**
	 * Get the instance of Controller
	 * @return the instance of Controller
	 */
	public static Controller getInstance() {
		if(instance == null) {
			instance =  new Controller();
		}
		return instance;
	}
	
	/**
	 * Send the choice of the user to the dedicated service
	 * @param choice of the user
	 */
	public void sendToService(CLI cli, ChoiceMenuEnum choice) {
		switch(choice) {
		case LIST_COMPUTERS:
			try {
				cli.showComputers(serviceComputer.search(""));
			} catch (PageNotFoundException e) {
				logger.warn("Page Not found", e);
			}
			break;
			
		case LIST_COMPANIES:
			try {
				cli.showCompanies(serviceCompany.list(0));
			} catch (PageNotFoundException e) {
				logger.warn("Page Not found", e);
			}
			break;
			
		case SHOW_COMPUTER_DETAIL:
			int detailId = cli.askInteger("Please enter an Id (Integer)");
			try {
				DTOComputer computer = serviceComputer.find(detailId);
				cli.showComputerDetails(computer);
			} catch (ComputerNotFoundException e) {
				cli.printException(e);
				logger.warn("Computer Not found", e);
			}
			break;
			
		case CREATE_COMPUTER:
			DTOComputer computer;
			try {
				Optional<DTOComputer> optionalComputer = cli.askComputerCreationInformation();
				if(optionalComputer.isPresent()) {
					computer = optionalComputer.get();
					cli.printCreatedMessage(serviceComputer.insert(computer));
				} else {
					logger.warn("Computer is not present");
				}
			} catch (NotAValidComputerException e) {
				cli.printException(e);
				logger.warn("Computer not valid");
			} catch (CantConnectException e) {
				logger.warn("Can't connect");
			}
			break;
			
		case UPDATE_COMPUTER:
			DTOComputer dtoComputer;
			try {
				int id = cli.askComputerUpdateId();
				Optional<DTOComputer> optionalComputer = cli.askComputerCreationInformation();
				if(optionalComputer.isPresent()) {
					dtoComputer = optionalComputer.get();
					serviceComputer.update(id, dtoComputer);
					cli.printUpdatedMessage(true);
				}
			} catch(IllegalArgumentException | NotAValidComputerException | ComputerNotFoundException | CantConnectException e) {
				cli.printException(e);
			} 
			break;
			
		case DELETE_COMPUTER:
			int deleteId = cli.askInteger("Please enter the Id of the Computer you want to Delete.");
			try {
				serviceComputer.delete(deleteId);
				cli.printDeletedMessage(true, deleteId);
			} catch (CantConnectException | ComputerNotFoundException e) {
				cli.printDeletedMessage(false, deleteId);
			}
			
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
				page.setList(serviceCompany.list(page.decrementIndex()).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.incrementIndex();
				cli.printException(e);
			}
			break;
			
		case NEXT_PAGE:
			try {
				page.setList(serviceCompany.list(page.incrementIndex()).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.decrementIndex();
				cli.printException(e);
			}
			break;
		case SET_PAGE:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				page.setList(serviceCompany.list(pageNumber).getList());
				page.setIndex(pageNumber);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case FIRST_PAGE:
			try {
				page.setList(serviceCompany.list(0).getList());
				page.setIndex(0);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case LAST_PAGE:
			try {
				int lastSlice = serviceCompany.getLastPage();
				page.setList(serviceCompany.list(lastSlice).getList());
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
				page.setList(serviceComputer.search(page.decrementIndex(), 50, "", OrderByEnum.DEFAULT).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.incrementIndex();
				cli.printException(e);
			}
			break;
			
		case NEXT_PAGE:
			try {
				page.setList(serviceComputer.search(page.incrementIndex(), 50, "", OrderByEnum.DEFAULT).getList());
			} catch (PageNotFoundException e) {
				isOk = false;
				page.decrementIndex();
				cli.printException(e);
			}
			break;
			
		case SET_PAGE:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				page.setList(serviceComputer.search(pageNumber, 50, "", OrderByEnum.DEFAULT).getList());
				page.setIndex(pageNumber);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case FIRST_PAGE:
			try {
				page.setList(serviceComputer.search(0, 50, "", OrderByEnum.DEFAULT).getList());
				page.setIndex(0);
			} catch (PageNotFoundException e) {
				isOk = false;
				cli.printException(e);
			}
			break;
			
		case LAST_PAGE:
			try {
				int lastSlice = serviceComputer.lastPage();
				page.setList(serviceComputer.search(lastSlice, 50, "", OrderByEnum.DEFAULT).getList());
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
