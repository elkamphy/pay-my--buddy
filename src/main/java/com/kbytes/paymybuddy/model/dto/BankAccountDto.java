package com.kbytes.paymybuddy.model.dto;

import lombok.Data;

@Data
public class BankAccountDto {
	private int bankCode;
	private int branchCode;
	private Long accountNumber;
	private String bankName;
}
