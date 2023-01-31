package com.kbytes.paymybuddy.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.ContactDto;

public interface ContactService {
	Page<User> findMyContacts(String name, String username, Pageable pageable);

	void addContact(@Valid ContactDto contact);
}
