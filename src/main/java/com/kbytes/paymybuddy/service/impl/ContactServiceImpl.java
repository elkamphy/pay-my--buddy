package com.kbytes.paymybuddy.service.impl;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kbytes.paymybuddy.model.Friend;
import com.kbytes.paymybuddy.model.FriendKey;
import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.ContactDto;
import com.kbytes.paymybuddy.repository.ContactRepository;
import com.kbytes.paymybuddy.repository.UserRepository;
import com.kbytes.paymybuddy.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
	private ContactRepository contactRepository;
	private UserRepository userRepository;

	public ContactServiceImpl(ContactRepository contactRepository, UserRepository userRepository) {
		this.contactRepository = contactRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Page<User> findMyContacts(String name, String username, Pageable pageable) {
		return contactRepository.findMyContacts(name, username, pageable);
	}

	@Override
	public void addContact(@Valid ContactDto contact) {
		User myUser = userRepository.findByEmail(contact.getMyEmail());
		User myContact = userRepository.findByEmail(contact.getEmail());
		FriendKey contactKey = new FriendKey();
		contactKey.setUserId1(myUser.getId());
		contactKey.setUserId2(myContact.getId());
		Friend friend = new Friend();
		friend.setId(contactKey);
		contactRepository.save(friend);
	}

}
