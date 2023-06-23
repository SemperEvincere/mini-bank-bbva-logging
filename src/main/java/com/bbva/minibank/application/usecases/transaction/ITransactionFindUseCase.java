package com.bbva.minibank.application.usecases.transaction;

import com.bbva.minibank.domain.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionFindUseCase {
	List<Transaction> findAll();
	
	Transaction findById(UUID transactionNumber);
	
}
