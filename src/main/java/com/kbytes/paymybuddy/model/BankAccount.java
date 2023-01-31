package com.kbytes.paymybuddy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class BankAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int bankCode;
	private int branchCode;
	private Long accountNumber;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
