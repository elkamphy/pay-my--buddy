package com.kbytes.paymybuddy.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id", insertable = false, updatable = false)
	private User sender;
	@ManyToOne
	@JoinColumn(name = "receiver_id", insertable = false, updatable = false)
	private User receiver;

	@Column(nullable = false, unique = true)
	private Date date;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	private int transactionType;

}