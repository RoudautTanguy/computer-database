package com.excilys.cdb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.service.ServiceCompany;

@Controller
public class LoginController {

	@Autowired
	private ServiceCompany serviceCompany;
	
	@GetMapping("/login")
    public String login() {
        return "login";
    }
	
	@GetMapping("/test")
    public String test(final ModelMap model) {
		List<DTOCompany> companies = serviceCompany.list();
		model.addAttribute("companies", companies);
		DTOComputer dtoComputer = new DTOComputer();
		model.addAttribute("dtoComputer",dtoComputer);
		return "addComputer";
    }
}
