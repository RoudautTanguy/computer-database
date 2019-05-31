package com.excilys.cdb.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.exception.PageNotFoundException;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.dao.OrderByEnum;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;
import com.excilys.cdb.ui.CLI;

@Component
public class Controller {

	private ServiceCompany serviceCompany;
	private ServiceComputer serviceComputer;

	public Controller(ServiceCompany serviceCompany, ServiceComputer serviceComputer) {
		this.serviceCompany = serviceCompany;
		this.serviceComputer = serviceComputer;
	}

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

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
				cli.printException(e);
			}
			break;

		case LIST_COMPANIES:
			try {
				cli.showCompanies(serviceCompany.list(0));
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			break;

		case SHOW_COMPUTER_DETAIL:
			int detailId = cli.askInteger("Please enter an Id (Integer)");
			DTOComputer computer;
			try {
				computer = serviceComputer.find(detailId);
				cli.showComputerDetails(computer);
			} catch (ComputerNotFoundException e) {
				cli.printException(e);
			}
			break;

		case CREATE_COMPUTER:
			Optional<DTOComputer> optionalComputerCreated = cli.askComputerCreationInformation();
			if(optionalComputerCreated.isPresent()) {
				try{
					serviceComputer.insert(optionalComputerCreated.get());
					cli.printCreatedMessage(true);
				} catch(NotAValidComputerException e) {
					cli.printCreatedMessage(false);
					logger.warn("Computer not valid");
				} catch (CompanyNotFoundException e) {
					cli.printException(e);
				} 
			} else {
				logger.warn("Computer is not present");
			}
			break;

		case UPDATE_COMPUTER:
			DTOComputer dtoComputer;
			try {
				int id = cli.askComputerUpdateId();
				Optional<DTOComputer> optionalComputerUpdated = cli.askComputerCreationInformation();
				if(optionalComputerUpdated.isPresent()) {
					dtoComputer = optionalComputerUpdated.get();
					serviceComputer.update(id, dtoComputer);
					cli.printUpdatedMessage(true);
				}
			} catch(IllegalArgumentException | NotAValidComputerException | ComputerNotFoundException | CompanyNotFoundException e) {
				cli.printException(e);
			}
			break;

		case DELETE_COMPUTER:
			int deleteId = cli.askInteger("Please enter the Id of the Computer you want to Delete.");
			try {
				serviceComputer.delete(deleteId);
			} catch (ComputerNotFoundException e) {
				cli.printException(e);
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
				cli.printException(e);
			}
			break;

		case NEXT_PAGE:
			try {
				page.setList(serviceCompany.list(page.incrementIndex()).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			break;
		case SET_PAGE:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				page.setList(serviceCompany.list(pageNumber).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			page.setIndex(pageNumber);
			break;

		case FIRST_PAGE:
			try {
				page.setList(serviceCompany.list(0).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			page.setIndex(0);
			break;

		case LAST_PAGE:
			int lastSlice = serviceCompany.getLastPage();
			try {
				page.setList(serviceCompany.list(lastSlice).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			page.setIndex(lastSlice);
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
				cli.printException(e);
			}
			break;

		case NEXT_PAGE:
			try {
				page.setList(serviceComputer.search(page.incrementIndex(), 50, "", OrderByEnum.DEFAULT).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			break;

		case SET_PAGE:
			int pageNumber = cli.askInteger("Please enter the number of the page.");
			try {
				page.setList(serviceComputer.search(pageNumber, 50, "", OrderByEnum.DEFAULT).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			page.setIndex(pageNumber);
			break;

		case FIRST_PAGE:
			try {
				page.setList(serviceComputer.search(0, 50, "", OrderByEnum.DEFAULT).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			page.setIndex(0);
			break;

		case LAST_PAGE:
			int lastSlice = serviceComputer.lastPage();
			try {
				page.setList(serviceComputer.search(lastSlice, 50, "", OrderByEnum.DEFAULT).getList());
			} catch (PageNotFoundException e) {
				cli.printException(e);
			}
			page.setIndex(lastSlice);
			break;

		case QUIT:
			cli.startChoice();
			break;
		}
		return isOk;
	}

}
