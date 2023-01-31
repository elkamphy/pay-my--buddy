package com.kbytes.paymybuddy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kbytes.paymybuddy.model.BankAccount;
import com.kbytes.paymybuddy.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	@Query("Select b from BankAccount b join User u on (b.user.id=u.id) where u.email=:email ")
	Page<BankAccount> findBankAccountByUsername(Pageable paging, @Param("email") String email);

}