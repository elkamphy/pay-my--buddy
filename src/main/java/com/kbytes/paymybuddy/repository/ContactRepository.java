package com.kbytes.paymybuddy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kbytes.paymybuddy.model.Friend;
import com.kbytes.paymybuddy.model.FriendKey;
import com.kbytes.paymybuddy.model.User;

public interface ContactRepository extends JpaRepository<Friend, FriendKey> {

	@Query(" SELECT u FROM Friend f Join User u on (f.id.userId2 = u.id) Join User u2 on (f.id.userId1 = u2.id)  WHERE u2.email = :username and (u.firstName like %:name% or u.lastName like %:name%)  ")
	Page<User> findMyContacts(@Param("name") String name, @Param("username") String username, Pageable pageable);

}
