package com.excilys.cdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@GetMapping("/login")
    public String login(Model model, @RequestParam(value = "error", required = false) String error,
    		@RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
        	model.addAttribute("error", true);
        }

        if (logout != null) {
        	model.addAttribute("logout", true);
        }
        
        return "login";
    }
	
	@GetMapping("/logout")
	public String getLogout() {
		return "logout";
	}
}