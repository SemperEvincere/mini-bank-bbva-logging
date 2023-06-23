package com.bbva.minibank.infrastructure.repositories;

import com.bbva.minibank.application.repository.ITransactionRepository;
import com.bbva.minibank.domain.models.Transaction;
import com.bbva.minibank.infrastructure.entities.TransactionEntity;
import com.bbva.minibank.infrastructure.mappers.TransactionEntityMapper;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.ITransactionSpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class TransactionRepositoryImpl implements ITransactionRepository {
	
	private final ITransactionSpringRepository transactionSpringRepository;
	private final TransactionEntityMapper transactionEntityMapper;
	
	@Override
	public Transaction save(Transaction transaction) {
		log.info("Saving transaction: {}", transaction);
		TransactionEntity transactionEntity = transactionSpringRepository.save(
				transactionEntityMapper.ToEntity(transaction));
		log.info("Transaction saved: {}", transactionEntity);
		return transactionEntityMapper.toDomain(transactionEntity);
	}
	
	@Override
	public List<Transaction> findAll() {
		log.info("Getting all transactions");
		return transactionSpringRepository.findAll()
		                                  .stream()
		                                  .map(transactionEntityMapper::toDomain)
		                                  .toList();
	}
	
	@Override
	public Optional<Transaction> findById(UUID transactionNumber) {
		log.info("Getting transaction by transaction number: {}", transactionNumber);
		return transactionSpringRepository.findById(transactionNumber)
		                                  .map(transactionEntityMapper::toDomain);
	}
	
}
