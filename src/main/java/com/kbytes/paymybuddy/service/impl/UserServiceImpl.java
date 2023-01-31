package com.kbytes.paymybuddy.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kbytes.paymybuddy.model.BankAccount;
import com.kbytes.paymybuddy.model.Role;
import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.UserDto;
import com.kbytes.paymybuddy.repository.RoleRepository;
import com.kbytes.paymybuddy.repository.UserRepository;
import com.kbytes.paymybuddy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private String defaultRole = "ROLE_USER";

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void saveUser(UserDto userDto) {
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(userDto, User.class);
		// encrypt the password using spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setBalance(0.0);

		Role role = roleRepository.findByName(defaultRole);
		if (role == null) {
			role = createRole(defaultRole);
		}
		user.setRoles(Arrays.asList(role));
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
	}

	private UserDto mapToUserDto(User user) {
		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(user, UserDto.class);
		return userDto;
	}

	private Role createRole(String roleName) {
		Role role = new Role();
		role.setName(roleName);
		return roleRepository.save(role);
	}

	@Override
	public Page<BankAccount> findBankAccountByUsername(Pageable paging, String email) {
		Page<BankAccount> results = userRepository.findBankAccountByUsername(paging, email);
		return results;
	}
}