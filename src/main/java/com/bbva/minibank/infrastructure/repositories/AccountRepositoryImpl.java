package com.bbva.minibank.infrastructure.repositories;

import com.bbva.minibank.application.repository.IAccountRepository;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.infrastructure.entities.AccountEntity;
import com.bbva.minibank.infrastructure.mappers.AccountEntityMapper;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.IAccountSpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class AccountRepositoryImpl implements IAccountRepository {
	
	private final IAccountSpringRepository accountSpringRepository;
	private final AccountEntityMapper accountEntityMapper;
	
	@Override
	public Account findByAccountNumber(UUID accountNumber) {
		log.info("Getting account by account number: {}", accountNumber);
		Optional<AccountEntity> accountEntity = accountSpringRepository.findById(accountNumber);
		log.info("Account found: {}", accountEntity);
		return accountEntityMapper.entityToDomain(accountEntity.orElse(null));
	}
	
	@Override
	public void saveAll(List<Account> accountsDefault) {
		log.info("Saving accounts: {}", accountsDefault);
		List<AccountEntity> accountEntities = accountsDefault.stream()
		                                                     .map(accountEntityMapper::domainToEntity)
		                                                     .toList();
		log.info("Accounts saved: {}", accountEntities);
		accountSpringRepository.saveAll(accountEntities);
	}
	
	@Override
	public Account save(Account newAccount) {
		log.info("Saving account: {}", newAccount);
		AccountEntity accountEntity = accountEntityMapper.domainToEntity(newAccount);
		AccountEntity accountSaved = accountSpringRepository.save(accountEntity);
		log.info("Account saved: {}", accountSaved);
		return accountEntityMapper.entityToDomain(accountSaved);
	}
	
	@Override
	public List<Account> findAll() {
		log.info("Getting all accounts");
		List<AccountEntity> accountEntities = accountSpringRepository.findAll();
		log.info("Accounts found: {}", accountEntities);
		return accountEntities.stream()
		                      .map(accountEntityMapper::entityToDomain)
		                      .toList();
	}
}
