package com.kbytes.paymybuddy.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kbytes.paymybuddy.model.Transaction;
import com.kbytes.paymybuddy.model.dto.TransactionCreateDto;;

public interface TransferService {
	public List<Transaction> findAll();

	Page<Transaction> findAll(Pageable pageable);

	/**
	 * this method must handle the following types of transaction : 1) Payment from
	 * one user to another --> type 1 2) Deposit from merchant account to user
	 * account --> type 2 3) Debit from user account to merchant account --> type 3
	 * 4) Top up from bank account to user account --> type 4 5) Top down from user
	 * account to bank account --> type 5
	 * 
	 * @param transaction
	 * @throws Exception
	 */
	public void addTransaction(TransactionCreateDto transaction) throws Exception;
}