package com.kbytes.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kbytes.paymybuddy.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);
}