package com.bbva.minibank.presentation.controllers;

import com.bbva.minibank.application.usecases.account.IAccountCreateUseCase;
import com.bbva.minibank.application.usecases.account.IAccountDeleteUseCase;
import com.bbva.minibank.application.usecases.account.IAccountFindUseCase;
import com.bbva.minibank.application.usecases.account.IAccountUpdateUseCase;
import com.bbva.minibank.application.usecases.client.IClientFindByUseCase;
import com.bbva.minibank.application.usecases.client.IClientUpdateUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.presentation.mappers.AccountPresentationMapper;
import com.bbva.minibank.presentation.request.account.AccountAddCoholder;
import com.bbva.minibank.presentation.request.account.AccountCreateRequest;
import com.bbva.minibank.presentation.response.account.AccountCreateResponse;
import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Log4j2
public class AccountController {
	
	private final IAccountCreateUseCase accountCreateUseCase;
	private final AccountPresentationMapper accountMapper;
	private final IClientFindByUseCase clientFindByUseCase;
	private final IClientUpdateUseCase clientUpdateUseCase;
	private final IAccountFindUseCase accountFindByUseCase;
	private final IAccountUpdateUseCase accountUpdateUseCase;
	private final IAccountDeleteUseCase accountDeleteUseCase;
	
	@PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> create(@RequestBody AccountCreateRequest accountCreateRequest) {
		if (accountCreateRequest == null) {
			log.error("Request is null");
			return ResponseEntity.badRequest()
			                     .body("Request is null");
		}
		Optional<Client> client = clientFindByUseCase.findById(accountCreateRequest.getHolderId());
		if (client.isEmpty()) {
			log.error("Client not found");
			return ResponseEntity.badRequest()
			                     .body("Client not found");
		}
		Optional<Client> secondHolder = Optional.empty();
		log.info("Second holder id: {}", accountCreateRequest.getSecondHolderId());
		if (accountCreateRequest.getSecondHolderId() != null && !accountCreateRequest.getSecondHolderId()
		                                                                             .equals(
				                                                                             accountCreateRequest.getHolderId())) {
			secondHolder = clientFindByUseCase.findById(accountCreateRequest.getSecondHolderId());
			if (secondHolder.isEmpty()) {
				log.error("Second holder not found");
				return ResponseEntity.badRequest()
				                     .body("Second holder not found");
			}
		}
		Optional<Account> accountOptional = client.get()
		                                          .getAccounts()
		                                          .stream()
		                                          .map(accountFindByUseCase::findByAccountNumber)
		                                          .filter(account -> account.getCurrency()
		                                                                    .equals(accountCreateRequest.getCurrency()))
		                                          .findFirst();
		
		if (accountOptional.isPresent()) {
			log.error("Account already exists");
			return ResponseEntity.badRequest()
			                     .body("Account already exists");
		}
		
		Account account = accountCreateUseCase.create(accountCreateRequest.getCurrency(), client.get(),
		                                              secondHolder.orElse(null));
		client.get()
		      .getAccounts()
		      .add(account.getAccountNumber());
		secondHolder.ifPresent(value -> value.getAccounts()
		                                     .add(account.getAccountNumber()));
		AccountCreateResponse accountCreateResponse = accountMapper.domainToCreateResponse(account);
		log.info("Account created: {}", accountCreateResponse);
		return ResponseEntity.ok(accountCreateResponse);
	}
	
	@PostMapping(value = "/coholder", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> coholder(@RequestBody AccountAddCoholder accountAddCoholder) {
		if (accountAddCoholder == null) {
			log.error("Request is null");
			return ResponseEntity.badRequest()
			                     .body("Request is null");
		}
		Optional<Client> client = clientFindByUseCase.findById(accountAddCoholder.getHolderId());
		if (client.isEmpty()) {
			log.error("Client not found");
			return ResponseEntity.badRequest()
			                     .body("Client not found");
		}
		Optional<Client> secondHolder = Optional.empty();
		if (accountAddCoholder.getSecondHolderId() != null) {
			log.info("Second holder id: {}", accountAddCoholder.getSecondHolderId());
			secondHolder = clientFindByUseCase.findById(accountAddCoholder.getSecondHolderId());
			if (secondHolder.isEmpty()) {
				log.error("Second holder not found");
				return ResponseEntity.badRequest()
				                     .body("Second holder not found");
			}
		}
		Optional<Account> accountOptional = client.get()
		                                          .getAccounts()
		                                          .stream()
		                                          .map(accountFindByUseCase::findByAccountNumber)
		                                          .filter(account -> account.getCurrency()
		                                                                    .equals(accountAddCoholder.getCurrency()))
		                                          .findFirst();
		
		if (accountOptional.isEmpty() || accountOptional.get()
		                                                .isLocked()) {
			log.error("Account not found or blocked");
			return ResponseEntity.badRequest()
			                     .body("Account not found or blocked");
		}
		
		Account account = accountOptional.get();
		
		secondHolder.ifPresent(value -> {
			if (value.getAccounts()
			         .contains(account.getAccountNumber())) {
				log.error("Account already has this coholder");
				return;
			} else {
				log.info("Adding coholder to account");
				value.getAccounts()
				     .add(account.getAccountNumber());
			}
		});
		secondHolder.ifPresent(value -> value.getAccounts()
		                                     .add(account.getAccountNumber()));
		Account accountUpdate = accountUpdateUseCase.update(account);
		clientUpdateUseCase.update(secondHolder.orElse(null));
		AccountCreateResponse accountCreateResponse = accountMapper.domainToCreateResponse(accountUpdate);
		log.info("Account updated: {}", accountCreateResponse);
		return ResponseEntity.ok(accountCreateResponse);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<?> findAll() {
		log.info("Find all accounts");
		List<Account> accounts = accountFindByUseCase.findAll();
		List<AccountDetailsResponse> accountCreateResponses = accounts.stream()
		                                                              .map(accountMapper::domainToDetailsResponse)
		                                                              .collect(Collectors.toList());
		log.info("Accounts found: {}", accountCreateResponses);
		return ResponseEntity.ok(accountCreateResponses);
	}
	
	@GetMapping(value = "/{accountNumber}", produces = "application/json")
	public ResponseEntity<AccountDetailsResponse> findByAccountNumber(
			@PathVariable("accountNumber") String accountNumber) {
		log.info("Find account by account number: {}", accountNumber);
		Account account = accountFindByUseCase.findByAccountNumber(UUID.fromString(accountNumber));
		AccountDetailsResponse accountDetailsResponse = accountMapper.domainToDetailsResponse(account);
		log.info("Account found: {}", accountDetailsResponse);
		return ResponseEntity.ok(accountDetailsResponse);
	}
	
	@DeleteMapping(value = "/{accountNumber}", produces = "application/json")
	public ResponseEntity<?> deleteByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
		log.info("Delete account by account number: {}", accountNumber);
		Account account = accountFindByUseCase.findByAccountNumber(UUID.fromString(accountNumber));
		accountDeleteUseCase.blockAccount(account);
		log.info("Account deleted");
		return ResponseEntity.ok()
		                     .build();
	}
	
}
