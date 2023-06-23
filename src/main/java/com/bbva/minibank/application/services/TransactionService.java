package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.ITransactionRepository;
import com.bbva.minibank.application.usecases.account.IAccountFindUseCase;
import com.bbva.minibank.application.usecases.account.IAccountOperationsUseCase;
import com.bbva.minibank.application.usecases.account.IAccountUpdateUseCase;
import com.bbva.minibank.application.usecases.client.IClientFindByUseCase;
import com.bbva.minibank.application.usecases.transaction.ITransactionBalanceUseCase;
import com.bbva.minibank.application.usecases.transaction.ITransactionCreateUseCase;
import com.bbva.minibank.application.usecases.transaction.ITransactionFindUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.Transaction;
import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import com.bbva.minibank.presentation.request.transaction.TransactionCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionService implements ITransactionBalanceUseCase, ITransactionCreateUseCase,
                                           ITransactionFindUseCase {
	
	private final ITransactionRepository transactionRepository;
	private final IAccountFindUseCase accountFind;
	private final IAccountUpdateUseCase accountUpdate;
	private final IClientFindByUseCase clientFindBy;
	private final IAccountOperationsUseCase accountOperationsUseCase;
	
	public Transaction createTransaction(TransactionCreateRequest transactionCreateRequest) {
		log.info("Creating transaction");
		return Transaction
				       .builder()
				       .createdAt(LocalDateTime.now())
				       .type(TransactionTypeEnum.valueOf(transactionCreateRequest.getType()))
				       .accountNumberFrom(transactionCreateRequest.getIdAccountOrigin()
				                                                  .isBlank() ? null : UUID.fromString(
						       transactionCreateRequest.getIdAccountOrigin()))
				       .accountNumberTo(transactionCreateRequest.getIdAccountDestination()
				                                                .isBlank() ? null : UUID.fromString(
						       transactionCreateRequest.getIdAccountDestination()))
				       .amount(transactionCreateRequest.getAmount())
				       .build();
	}
	
	@Override
	public Transaction deposit(Transaction transaction,
	                           Client client) {
		log.info("Depositing");
		if (transaction.getAccountNumberFrom() == null) {
			log.error("AccountNumberFrom not must be null");
			throw new IllegalArgumentException("AccountNumberFrom not must be null");
		}
		// verificar que el cliente existe
		if (clientFindBy.findById(client.getId())
		                .isEmpty()) {
			log.error("Client not found");
			throw new IllegalArgumentException("Client not found");
		}
		// buscar la cuenta del depositante
		UUID accountClient = clientFindBy.getAccountClient(transaction, client);
		Account accountSaved = accountFind.findByAccountNumber(accountClient);
		// verificar que el cliente sea holder o coholder de la cuenta
		if (accountSaved.getClientHolder()
		                .equals(client.getId()) || Objects.requireNonNull(
				                                                  accountSaved.getListSecondsHolders())
		                                                  .contains(client.getId())) {
			// realizar el deposito
			log.info("Depositing");
			accountSaved.setBalance(accountOperationsUseCase.add(accountSaved.getBalance(), transaction.getAmount()));
		} else {
			log.error("Client not is holder or coholder of account");
			throw new IllegalArgumentException("Client not is holder or coholder of account");
		}
		// guardar la transaccion
		if (accountSaved.getTransactions() == null || accountSaved.getTransactions()
		                                                          .isEmpty()) {
			log.info("Creating new list of transactions");
			accountSaved.setTransactions(new ArrayList<>());
		}
		Transaction transactionSaved = transactionRepository.save(transaction);
		accountSaved.getTransactions()
		            .add(transactionSaved);
		accountUpdate.update(accountSaved);
		log.info("Transaction saved");
		return transactionSaved;
	}
	
	
	@Override
	public Transaction withdraw(Transaction transaction,
	                            Client client) {
		UUID accountClient = clientFindBy.getAccountClient(transaction, client);
		Account accountSaved = accountFind.findByAccountNumber(accountClient);
		accountSaved.setBalance(accountOperationsUseCase.substract(accountSaved.getBalance(), transaction.getAmount()));
		log.info("Withdrawing");
		if (accountSaved.getTransactions() == null || accountSaved.getTransactions()
		                                                          .isEmpty()) {
			log.info("Creating new list of transactions");
			accountSaved.setTransactions(new ArrayList<>());
		}
		Transaction transactionSaved = transactionRepository.save(transaction);
		accountSaved.getTransactions()
		            .add(transactionSaved);
		accountUpdate.update(accountSaved);
		log.info("Transaction saved");
		return transactionSaved;
	}
	
	@Override
	public Transaction transfer(Transaction transaction,
	                            Client clientSaved) {
		log.info("Transfering");
		if (transaction.getAccountNumberTo()
		               .equals(transaction.getAccountNumberFrom())) {
			log.error("Account origin and destination must be different");
			throw new IllegalArgumentException("Account origin and destination must be different");
		}
		
		UUID accountClient = clientFindBy.getAccountClient(transaction, clientSaved);
		Account accountOrigin = accountFind.findByAccountNumber(accountClient);
		Account accountDestination = accountFind.findByAccountNumber(transaction.getAccountNumberTo());
		
		if (accountOrigin.getBalance()
		                 .compareTo(transaction.getAmount()) < 0) {
			log.error("Insufficient funds");
			throw new IllegalArgumentException("Insufficient funds");
		}
		
		if (accountOrigin.getClientHolder()
		                 .equals(clientSaved.getId()) || Objects.requireNonNull(accountOrigin.getListSecondsHolders())
		                                                        .contains(clientSaved.getId())) {
			log.info("Client is holder or coholder of account origin");
			throw new IllegalArgumentException("Client not is holder or coholder of account origin");
		}
		
		if (!accountOrigin.getCurrency()
		                  .equals(accountDestination.getCurrency())) {
			log.error("Accounts must be in the same currency");
			throw new IllegalArgumentException("Accounts must be in the same currency");
		}
		
		accountOrigin.setBalance(accountOperationsUseCase.substract(accountOrigin.getBalance(), transaction.getAmount()));
		accountDestination.setBalance(
				accountOperationsUseCase.add(accountDestination.getBalance(), transaction.getAmount()));
		if (accountOrigin.getTransactions() == null || accountOrigin.getTransactions()
		                                                            .isEmpty()) {
			log.info("Creating new list of transactions");
			accountOrigin.setTransactions(new ArrayList<>());
		}
		if (accountDestination.getTransactions() == null || accountDestination.getTransactions()
		                                                                      .isEmpty()) {
			log.info("Creating new list of transactions");
			accountDestination.setTransactions(new ArrayList<>());
		}
		Transaction transactionSaved = transactionRepository.save(transaction);
		accountOrigin.getTransactions()
		             .add(transactionSaved);
		accountDestination.getTransactions()
		                  .add(transactionSaved);
		accountUpdate.update(accountOrigin);
		accountUpdate.update(accountDestination);
		log.info("Transaction saved");
		return transactionSaved;
	}
	
	
	@Override
	public List<Transaction> findAll() {
		log.info("Finding all transactions");
		return transactionRepository.findAll();
	}
	
	@Override
	public Transaction findById(UUID transactionNumber) {
		log.info("Finding transaction by id");
		return transactionRepository.findById(transactionNumber)
		                            .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
	}
	
}
