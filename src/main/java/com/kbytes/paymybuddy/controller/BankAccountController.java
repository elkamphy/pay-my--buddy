package com.kbytes.paymybuddy.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kbytes.paymybuddy.model.BankAccount;
import com.kbytes.paymybuddy.model.dto.BankAccountDto;
import com.kbytes.paymybuddy.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BankAccountController {
	@Autowired
	UserService userService;
	private int currentPage, totalPages, pageSize;
	private Long totalItems;
	private BankAccountDto currentDto;
	List<BankAccountDto> linkedAccounts;

	@RequestMapping("/linked-accounts")
	public String transfer(Principal principal, Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable paging = PageRequest.of(page - 1, size);

		Page<BankAccount> pageLinkedAccount = userService.findBankAccountByUsername(paging, principal.getName());
		ModelMapper mapper = new ModelMapper();
		List<BankAccount> results = pageLinkedAccount.getContent();
		if (results == null)
			results = new ArrayList<>();
		linkedAccounts = results.stream().map(entity -> mapper.map(entity, BankAccountDto.class))
				.collect(Collectors.toList());
		currentPage = pageLinkedAccount.getNumber() + 1;
		totalItems = pageLinkedAccount.getTotalElements();
		totalPages = pageLinkedAccount.getTotalPages();
		pageSize = size;
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("tableData", linkedAccounts);
		model.addAttribute("currentLink", "Transfer");
		currentDto = new BankAccountDto();
		model.addAttribute("formObject", currentDto);
		return "bankaccount";
	}
}
