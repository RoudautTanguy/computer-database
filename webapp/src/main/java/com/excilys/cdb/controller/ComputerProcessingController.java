package com.excilys.cdb.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
import com.excilys.cdb.exception.ConcurentConflictException;
import com.excilys.cdb.exception.NotAValidComputerException;
import com.excilys.cdb.service.ServiceCompany;
import com.excilys.cdb.service.ServiceComputer;

@Controller
public class ComputerProcessingController {

	@Autowired
	private ServiceComputer serviceComputer;
	@Autowired
	private ServiceCompany serviceCompany;

	private static final Logger logger = LoggerFactory.getLogger(ComputerProcessingController.class);
	
	//Add Computer

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/addComputer")
	public String addComputer(final ModelMap model){
		List<DTOCompany> companies = serviceCompany.list();
		model.addAttribute("companies", companies);
		DTOComputer dtoComputer = new DTOComputer();
		model.addAttribute("dtoComputer",dtoComputer);
		return "addComputer";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/addComputer")
	public String addComputer(@ModelAttribute("dtoComputer") DTOComputer dtoComputer) throws NotAValidComputerException, CompanyNotFoundException {
		serviceComputer.insert(dtoComputer);
		return "redirect:/dashboard";

	}

	//Edit Computer

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/editComputer/{id}")
	public String editComputer(@PathVariable("id") int pId, final ModelMap model) throws ComputerNotFoundException{
		int id = -1;
		System.out.println("Id: "+pId);
		id = pId;
		model.addAttribute("id", id);
		DTOComputer computer = serviceComputer.find(id);
		model.addAttribute("computer", computer);

		List<DTOCompany> companies = serviceCompany.list();
		model.addAttribute("companies", companies);
		
		DTOComputer dtoComputer = new DTOComputer();
		model.addAttribute("dtoComputer",dtoComputer);
		return "editComputer";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/editComputer")
	public String editComputer(@ModelAttribute("dtoComputer") DTOComputer dtoComputer) throws NotAValidComputerException, ComputerNotFoundException, CompanyNotFoundException, ConcurentConflictException{
		try {
			int parseId = dtoComputer.getId();
			serviceComputer.update(parseId,dtoComputer);
		} catch(NumberFormatException e) {
			logger.warn("Computer with id {} doesn't exist", dtoComputer.getId());
			throw new ComputerNotFoundException("Computer with id " + dtoComputer.getId() + " doesn't exist");
		}
		
		return "redirect:/dashboard";

	}
}
