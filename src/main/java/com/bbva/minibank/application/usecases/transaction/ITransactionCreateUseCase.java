package com.bbva.minibank.application.usecases.transaction;

import com.bbva.minibank.domain.models.Transaction;
import com.bbva.minibank.presentation.request.transaction.TransactionCreateRequest;

public interface ITransactionCreateUseCase {
	
	
	Transaction createTransaction(TransactionCreateRequest transactionCreateRequest);
}
