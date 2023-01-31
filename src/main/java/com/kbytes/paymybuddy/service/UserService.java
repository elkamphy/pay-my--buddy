package com.kbytes.paymybuddy.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kbytes.paymybuddy.model.BankAccount;
import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.UserDto;

public interface UserService {
	void saveUser(UserDto userDto);

	User findByEmail(String email);

	List<UserDto> findAllUsers();

	Page<BankAccount> findBankAccountByUsername(Pageable paging, String name);
}