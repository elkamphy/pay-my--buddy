package com.kbytes.paymybuddy.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.UserDto;
import com.kbytes.paymybuddy.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * Displays the login form
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("currentLink", "");
		return "login";
	}

	/**
	 * Displays the home page
	 * 
	 * @return
	 */
	@RequestMapping({ "/index", "/home" })
	public String index(Model model) {
		model.addAttribute("currentLink", "");
		return "index";
	}

	/**
	 * This is method is used to display the registration form
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("register")
	public String showRegistrationForm(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		model.addAttribute("currentLink", "");
		return "register";
	}

	/**
	 * Called after submitting the register new user form
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("/register/save")
	public String registration(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
		User existing = userService.findByEmail(user.getEmail());
		if (existing != null) {
			result.rejectValue("email", null, "There is already an account registered with that email");
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "register";
		}
		userService.saveUser(user);
		return "redirect:/register?success";
	}

	/**
	 * Displays the list of all users. This is an admin feature
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/users")
	public String listRegisteredUsers(Model model) {
		List<UserDto> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "users";
	}
}