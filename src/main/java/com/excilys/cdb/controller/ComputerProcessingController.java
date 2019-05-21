package com.excilys.cdb.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.CompanyNotFoundException;
import com.excilys.cdb.exception.ComputerNotFoundException;
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

	@GetMapping("/addComputer")
	public String addComputer(final ModelMap model){
		List<DTOCompany> companies = serviceCompany.list();
		model.addAttribute("companies", companies);
		return "addComputer";
	}

	@PostMapping("/addComputer")
	public String addComputer(@RequestParam Map<String, String> requestParams) throws NotAValidComputerException, CompanyNotFoundException {
		String computerName = requestParams.get("computerName").replaceAll(Constant.SANITIZER_REPLACER, "_");
		String introduced = requestParams.get("introduced").replaceAll(Constant.SANITIZER_REPLACER, "_");
		String discontinued = requestParams.get("discontinued").replaceAll(Constant.SANITIZER_REPLACER, "_");
		String companyId = requestParams.get("companyId");
		DTOComputer dtoComputer = new DTOComputer(0,computerName,introduced,discontinued,companyId);
		serviceComputer.insert(dtoComputer);
		return "redirect:/dashboard";

	}

	//Edit Computer

	@GetMapping("/editComputer")
	public String editComputer(@RequestParam("id") String pId, final ModelMap model) throws ComputerNotFoundException{
		int id = -1;
		id = Integer.parseInt(pId);
		model.addAttribute("id", id);
		DTOComputer computer = serviceComputer.find(id);
		model.addAttribute("computer", computer);

		List<DTOCompany> companies = serviceCompany.list();
		model.addAttribute("companies", companies);
		return "editComputer";
	}

	@PostMapping("/editComputer")
	public String editComputer(@RequestParam Map<String, String> requestParams) throws NotAValidComputerException, ComputerNotFoundException{
		String id = requestParams.get("id");
		String computerName = requestParams.get("computerName").replaceAll(Constant.SANITIZER_REPLACER, "_");
		String introduced = requestParams.get("introduced").replaceAll(Constant.SANITIZER_REPLACER, "_");
		String discontinued = requestParams.get("discontinued").replaceAll(Constant.SANITIZER_REPLACER, "_");
		String companyId = requestParams.get("companyId");
		DTOComputer dtoComputer = new DTOComputer(0,computerName,introduced,discontinued,companyId);
		try {
			int parseId = Integer.parseInt(id);
			serviceComputer.update(parseId,dtoComputer);
		} catch(NumberFormatException e) {
			logger.warn("Computer with id {} doesn't exist", id);
			throw new ComputerNotFoundException("Computer with id " + id + " doesn't exist");
		}
		
		return "redirect:/dashboard";

	}
}
