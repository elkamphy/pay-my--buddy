package com.kbytes.paymybuddy.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.service.ContactService;

@RestController
public class ContactRestController {
	@Autowired
	ContactService contactService;

	@GetMapping("/contacts")
	public List<User> stateItems(@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "email", required = true) String username, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		if (StringUtils.isEmpty(query)) {
			query = "";
		}
		Pageable pageable = PageRequest.of(page - 1, size);
		List<User> myContacts = contactService.findMyContacts(query, username, pageable).getContent();
		return myContacts;
	}
}
