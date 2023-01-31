package com.kbytes.paymybuddy.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.ContactDto;
import com.kbytes.paymybuddy.service.ContactService;
import com.kbytes.paymybuddy.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ContactController {
	@Autowired
	private ContactService contactService;

	@Autowired
	private UserService userService;

	private List<User> contacts;
	private int currentPage, totalPages, pageSize;
	Long totalItems;

	@RequestMapping("/contact")
	public String contact(Principal principal, Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable paging = PageRequest.of(page - 1, size);

		Page<User> pageUsers = contactService.findMyContacts("", principal.getName(), paging);

		contacts = pageUsers.getContent();
		if (contacts == null)
			contacts = new ArrayList<>();
		currentPage = pageUsers.getNumber() + 1;
		totalItems = pageUsers.getTotalElements();
		totalPages = pageUsers.getTotalPages();
		pageSize = size;
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentLink", "Contact");
		ContactDto contactDto = new ContactDto();
		contactDto.setMyEmail(principal.getName());
		model.addAttribute("contact", contactDto);
		return "contact";
	}

	@PostMapping("/contact")
	public String registration(Principal principal, @Valid @ModelAttribute("contact") ContactDto contact,
			BindingResult result, Model model) {
		User existing = userService.findByEmail(contact.getEmail());
		if (existing == null) {
			result.rejectValue("email", null, "This user is not registered within the application!");
		}
		if (result.hasErrors()) {
			model.addAttribute("contact", contact);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("totalItems", totalItems);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("contacts", contacts);
			model.addAttribute("currentLink", "Contact");
			model.addAttribute("currentLink", "Contact");
			log.error("An error occured " + result.toString());
			log.info("myEmail : " + contact.getMyEmail());
			log.info("contactEmail : " + contact.getEmail());
			return "contact";
		}
		contact.setMyEmail(principal.getName());
		log.info("myEmail : " + contact.getMyEmail());
		log.info("contactEmail : " + contact.getEmail());
		contactService.addContact(contact);
		return "redirect:/contact?success";
	}

}
