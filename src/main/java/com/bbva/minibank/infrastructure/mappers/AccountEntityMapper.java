package com.bbva.minibank.infrastructure.mappers;

import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.infrastructure.entities.AccountEntity;
import com.bbva.minibank.infrastructure.entities.ClientEntity;
import com.bbva.minibank.infrastructure.entities.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountEntityMapper {
	
	private final TransactionEntityMapper transactionMapper;
	
	public Account entityToDomain(AccountEntity accountEntity) {
		if (accountEntity == null) {
			return null;
		}
		
		// Construye una instancia de Account utilizando el patrón Builder
		return Account.builder()
		              .accountNumber(accountEntity.getAccountNumber())
		              .balance(accountEntity.getBalance())
		              .currency(accountEntity.getCurrency())
		              .creationDate(accountEntity.getCreationDate())
		              .clientHolder(accountEntity.getOwner()
		                                         .getId())
		              .listSecondsHolders(accountEntity.getCoHolders()
		                                               .stream()
		                                               .map(ClientEntity::getId)
		                                               .collect(Collectors.toList()))
		              
		              .transactions(Optional.ofNullable(accountEntity.getTransactions())
		                                    .orElse(Collections.emptyList())
		                                    .stream()
		                                    .map(transactionMapper::toDomain)
		                                    .toList())
		              .isLocked(accountEntity.isLocked())
		              .build();
		
	}
	
	public AccountEntity domainToEntity(Account account) {
		if (account == null) {
			return null;
		}
		
		AccountEntity.AccountEntityBuilder accountBuilder = AccountEntity.builder()
		                                                                 .accountNumber(account.getAccountNumber())
		                                                                 .balance(account.getBalance())
		                                                                 .currency(account.getCurrency())
		                                                                 .creationDate(account.getCreationDate())
		                                                                 .isLocked(account.isLocked());
		
		// Asignar el titular (holder)
		ClientEntity holderEntity = new ClientEntity();
		holderEntity.setId(account.getClientHolder());
		accountBuilder.owner(holderEntity);
		
		// Asignar el segundo titular (secondHolder)
		if (account.getListSecondsHolders() != null) {
			List<ClientEntity> coHolders = account.getListSecondsHolders()
			                                      .stream()
			                                      .map(clientId -> {
				                                      ClientEntity coHolderEntity = new ClientEntity();
				                                      coHolderEntity.setId(clientId);
				                                      return coHolderEntity;
			                                      })
			                                      .collect(Collectors.toList());
			accountBuilder.coHolders(coHolders);
		}
		
		// Asignar las transacciones
		List<TransactionEntity> transactionEntities = Optional.ofNullable(account.getTransactions())
		                                                      .orElse(Collections.emptyList())
		                                                      .stream()
		                                                      .map(transactionMapper::toEntity)
		                                                      .toList();
		
		accountBuilder.transactions(transactionEntities);
		
		return accountBuilder.build();
	}
}
