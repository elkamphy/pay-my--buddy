package com.kbytes.paymybuddy.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kbytes.paymybuddy.exceptions.InsufficientFundException;
import com.kbytes.paymybuddy.model.Transaction;
import com.kbytes.paymybuddy.model.User;
import com.kbytes.paymybuddy.model.dto.TransactionCreateDto;
import com.kbytes.paymybuddy.service.TransferService;
import com.kbytes.paymybuddy.service.UserService;
import com.kbytes.paymybuddy.utils.TransferMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TransferController {
	private List<Transaction> transactions;
	@Autowired
	TransferService transfertService;
	@Autowired
	UserService userService;
	private int currentPage, totalPages, pageSize;
	private Long totalItems;
	private TransactionCreateDto currentDto;

	private TransferMode action;

	@RequestMapping("/transfer")
	public String transfer(Principal principal, Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "PAY") TransferMode action) {
		Pageable paging = PageRequest.of(page - 1, size);

		Page<Transaction> pageTransactions = transfertService.findAll(paging);

		transactions = pageTransactions.getContent();
		currentPage = pageTransactions.getNumber() + 1;
		totalItems = pageTransactions.getTotalElements();
		totalPages = pageTransactions.getTotalPages();
		pageSize = size;
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("transactions", transactions);
		model.addAttribute("currentLink", "Transfer");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		boolean hasAdminRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		model.addAttribute("isAdmin", hasAdminRole);
		currentDto = new TransactionCreateDto();
		currentDto.setAction(action);
		model.addAttribute("transaction", currentDto);
		this.action = action;
		log.info("Selected action : " + action);
		model.addAttribute("action", action);
		return "transfer";
	}

	@PostMapping("/transfer")
	public String payBuddy(Principal principal, @Valid @ModelAttribute("transaction") TransactionCreateDto transaction,
			BindingResult result, Model model) throws Exception {
		User existing = userService.findByEmail(transaction.getEmail());
		if (existing == null) {
			result.rejectValue("email", null, "User doesn't exist");
		}
		if (result.hasErrors()) {
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("totalItems", totalItems);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("transactions", transactions);
			model.addAttribute("currentLink", "Transfer");
			log.error("An error occured " + result.toString());
			log.info("myEmail : " + principal.getName());
			log.info("contactEmail : " + transaction.getEmail());
			return "transfer";
		}
		transaction.setMyEmail(principal.getName());
		try {
			transfertService.addTransaction(transaction);
		} catch (InsufficientFundException e) {
			result.rejectValue("amount", null, e.getMessage());
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("totalItems", totalItems);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("transactions", transactions);
			model.addAttribute("currentLink", "Transfer");
			e.printStackTrace();
			return "transfer";
		} catch (Exception ex) {
			throw ex;
		}
		return "redirect:/transfer?success";
	}

	private void initData() {
		transactions = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Transaction t = new Transaction();
			t.setDate(new Date());
			// t.setConnection("User " + i);
			t.setDescription("School fees");
			t.setAmount(200000d);

			transactions.add(t);
		}
	}

	public TransferMode getAction() {
		return action;
	}

	public void setAction(TransferMode action) {
		this.action = action;
	}
}
