package com.kbytes.paymybuddy.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TransactionDto {
	private Date date;
	private String connection;
	private String description;
	private Double amount;
}
