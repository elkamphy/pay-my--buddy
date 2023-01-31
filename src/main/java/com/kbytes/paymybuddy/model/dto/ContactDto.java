package com.kbytes.paymybuddy.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ContactDto {
	@Email
	@NotEmpty(message = "Email should not be empty")
	private String email;
	private String myEmail;
}
