package com.kbytes.paymybuddy.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.service.UserService;

@Controller
public class ProfileController {
	@Autowired
	private UserService userService;

	@GetMapping("profile")
	public String showRegistrationForm(Model model, Principal principal) {
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("currentLink", "Profile");
		return "profile";
	}
}
