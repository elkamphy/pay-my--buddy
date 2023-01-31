package com.kbytes.paymybuddy.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.kbytes.paymybuddy.utils.TransferMode;

import lombok.Data;

@Data
public class TransactionCreateDto {
	@NotEmpty
	private String description;
	@Min(value = 1, message = "The value must be greater than zero")
	@NotNull
	private Double amount;
	@Email
	private String email;
	private String myEmail;
	private TransferMode action = TransferMode.PAY;
	private int transactionType;
}
