package com.bbva.minibank.application.repository;

import com.bbva.minibank.domain.models.Account;

import java.util.List;
import java.util.UUID;

public interface IAccountRepository {
	
	Account findByAccountNumber(UUID accountNumber);
	
	void saveAll(List<Account> accountsDefault);
	
	Account save(Account newAccount);
	
	
	List<Account> findAll();
}
