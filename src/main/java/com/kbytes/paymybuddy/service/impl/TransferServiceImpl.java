package com.kbytes.paymybuddy.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kbytes.paymybuddy.exceptions.InsufficientFundException;
import com.kbytes.paymybuddy.model.Transaction;
import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.TransactionCreateDto;
import com.kbytes.paymybuddy.repository.TransferRepository;
import com.kbytes.paymybuddy.repository.UserRepository;
import com.kbytes.paymybuddy.service.TransferService;
import com.kbytes.paymybuddy.utils.PayMyBuddyHelper;

@Service
public class TransferServiceImpl implements TransferService {

	private TransferRepository transferRepository;
	private UserRepository userRepository;

	public TransferServiceImpl(TransferRepository transferRepository, UserRepository userRepository) {
		this.transferRepository = transferRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<Transaction> findAll() {
		return transferRepository.findAll();
	}

	@Override
	public Page<Transaction> findAll(Pageable pageable) {
		return transferRepository.findAll(pageable);
	}

	@Transactional
	@Override
	public void addTransaction(TransactionCreateDto transactionDto) {
		User sender = null;
		User receiver = null;
		User appUser = userRepository.findByEmail(PayMyBuddyHelper.APPLICATION_USERNAME);
		User appFeesUser = userRepository.findByEmail(PayMyBuddyHelper.APPLICATION_FEES_USERNAME);

		Double senderAmount = 0d;
		Double receiverAmount = 0d;
		Double appFeeAmount = 0d;
		Double transactionFees = 0d;
		Double totalAmount = 0d;
		switch (transactionDto.getTransactionType()) {
		case PayMyBuddyHelper.PAYMENT: {
			sender = userRepository.findByEmail(transactionDto.getMyEmail());
			receiver = userRepository.findByEmail(transactionDto.getEmail());
			transactionFees = transactionDto.getAmount() * PayMyBuddyHelper.TRANSACTION_FEES;
			break;
		}
		case PayMyBuddyHelper.CASH_IN: {
			sender = appUser;
			receiver = userRepository.findByEmail(transactionDto.getEmail());
			break;
		}
		case PayMyBuddyHelper.CASH_OUT: {
			sender = userRepository.findByEmail(transactionDto.getEmail());
			receiver = appUser;
			transactionFees = transactionDto.getAmount() * PayMyBuddyHelper.TRANSACTION_FEES;
			break;
		}
		case PayMyBuddyHelper.TOP_UP: {
			receiver = userRepository.findByEmail(transactionDto.getEmail());
			break;
		}
		case PayMyBuddyHelper.TOP_DOWN: {
			sender = userRepository.findByEmail(transactionDto.getEmail());
			transactionFees = transactionDto.getAmount() * PayMyBuddyHelper.TRANSACTION_FEES;
			break;
		}

		}
		totalAmount = transactionDto.getAmount() + transactionFees;

		if (sender.getBalance() < totalAmount)
			throw new InsufficientFundException("Insufficient funds!");

		senderAmount = sender.getBalance() - totalAmount;
		receiverAmount = receiver.getBalance() + transactionDto.getAmount();
		appFeeAmount = appFeesUser.getBalance() + transactionFees;

		// Create a transaction entity and save
		Transaction transaction = new Transaction();
		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setTransactionType(transactionDto.getTransactionType());
		transaction.setDate(new Date());
		transaction.setDescription(transactionDto.getDescription());
		transaction.setAmount(transactionDto.getAmount());
		transferRepository.save(transaction);
		// Update appFeesUser balance
		// If no applicable fees, appFeeAmount = appFeeUser.balance --> No change
		appFeesUser.setBalance(appFeeAmount);
		userRepository.save(appFeesUser);

		if (transactionDto.getTransactionType() != PayMyBuddyHelper.TOP_UP) {
			// Update sender balance
			sender.setBalance(senderAmount);
			userRepository.save(sender);
		}
		if (transactionDto.getTransactionType() != PayMyBuddyHelper.TOP_DOWN) {
			// Update receiver balance
			receiver.setBalance(receiverAmount);
			userRepository.save(receiver);
		}

	}

}