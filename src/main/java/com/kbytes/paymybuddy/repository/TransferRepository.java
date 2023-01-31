package com.kbytes.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kbytes.paymybuddy.model.Transaction;

public interface TransferRepository extends JpaRepository<Transaction, Long> {

}