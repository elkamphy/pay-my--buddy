package com.kbytes.paymybuddy.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kbytes.paymybuddy.config.CustomUserDetails;
import com.kbytes.paymybuddy.model.Role;
import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private final PasswordEncoder passwordEncoder;
	private final Map<String, CustomUserDetails> userRegistry = new HashMap<>();

	@Autowired
	UserRepository userRepository;

	public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	public void init() {
		userRegistry.put("user",
				new CustomUserDetails.Builder().withFirstName("Mark").withLastName("Johnson")
						.withEmail("mark.johnson@email.com").withUsername("user")
						.withPassword(passwordEncoder.encode("password"))
						.withAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).build());
		userRegistry.put("admin", new CustomUserDetails.Builder().withFirstName("James").withLastName("Davis")
				.withEmail("james.davis@email.com").withUsername("admin").withPassword(passwordEncoder.encode("admin"))
				.withAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))).build());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		CustomUserDetails userDetails = null;
		if (user == null) {
			// for demo only
			userDetails = userRegistry.get(username);
			log.error("No user found with username : " + username);
			log.info("Registry : " + userRegistry);
		} else {
			userDetails = new CustomUserDetails.Builder().withUsername(user.getEmail())
					.withFirstName(user.getFirstName()).withLastName(user.getLastName())
					.withPassword(user.getPassword()).withEmail(user.getEmail())
					.withAuthorities(mapRolesToAuthorities(user.getRoles())).build();
		}

		if (userDetails == null) {
			throw new UsernameNotFoundException(username);
		}

		return userDetails;

	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<? extends GrantedAuthority> mapRoles = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return mapRoles;
	}
}