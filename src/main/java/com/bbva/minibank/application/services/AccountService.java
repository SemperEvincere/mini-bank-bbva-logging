package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.IAccountRepository;
import com.bbva.minibank.application.usecases.account.*;
import com.bbva.minibank.application.usecases.client.IClientUpdateUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService implements
                            IAccountCreateUseCase,
                            IAccountFindUseCase,
                            IAccountUpdateUseCase,
                            IAccountOperationsUseCase,
                            IAccountDeleteUseCase {
	
	private final IAccountRepository accountRepository;
	private final IClientUpdateUseCase clientUpdateUseCase;
	
	@Override
	public Account create(CurrencyEnum currency,
	                      Client holder,
	                      Client secondHolder) {
		log.info("Creating account for holder: {}", holder.getId());
		Account.AccountBuilder accountBuilder = Account.builder()
		                                               .accountNumber(UUID.randomUUID())
		                                               .currency(currency)
		                                               .creationDate(LocalDate.now())
		                                               .balance(BigDecimal.ZERO)
		                                               .transactions(new ArrayList<>())
		                                               .clientHolder(holder.getId());
		
		if (secondHolder != null) {
			log.info("Adding second holder: {}", secondHolder.getId());
			accountBuilder.listSecondsHolders(List.of(secondHolder.getId()));
		}
		
		Account account = accountBuilder.build();
		accountRepository.save(account);
		clientUpdateUseCase.addAccount(holder, account);
		if (secondHolder != null) {
			clientUpdateUseCase.addAccount(secondHolder, account);
		}
		log.info("Account created: {}", account);
		return account;
	}
	
	@Override
	public Account findByAccountNumber(UUID accountNumber) {
		log.info("Finding account by account number: {}", accountNumber);
		return accountRepository.findByAccountNumber(accountNumber);
	}
	
	@Override
	public List<Account> findAll() {
		log.info("Finding all accounts");
		return accountRepository.findAll();
	}
	
	@Override
	public Account update(Account accountUpdate) {
		log.info("Updating account: {}", accountUpdate);
		return accountRepository.save(accountUpdate);
	}
	
	
	@Override
	public BigDecimal substract(BigDecimal balance,
	                            BigDecimal amount) {
		log.info("Substracting amount: {} from balance: {}", amount, balance);
		if (balance.compareTo(amount) < 0) {
			log.error("Balance not must be negative");
			throw new IllegalArgumentException("Balance not must be negative");
		}
		return balance.subtract(amount);
	}
	
	@Override
	public BigDecimal add(BigDecimal balance,
	                      BigDecimal amount) {
		log.info("Adding amount: {} to balance: {}", amount, balance);
		return balance.add(amount);
	}
	
	@Override
	public void blockAccount(Account account) {
		log.info("Blocking account: {}", account);
		account.setLocked(true);
		accountRepository.save(account);
	}
}
