package com.bbva.minibank.application.repository;

import com.bbva.minibank.domain.models.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITransactionRepository {
	
	Transaction save(Transaction transaction);
	
	List<Transaction> findAll();
	
	Optional<Transaction> findById(UUID transactionNumber);
	
}
