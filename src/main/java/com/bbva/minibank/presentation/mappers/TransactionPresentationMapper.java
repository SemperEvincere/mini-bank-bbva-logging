package com.bbva.minibank.presentation.mappers;

import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.Transaction;
import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import com.bbva.minibank.presentation.response.transaction.TransactionDepositResponse;
import com.bbva.minibank.presentation.response.transaction.TransactionResponse;
import com.bbva.minibank.presentation.response.transaction.TransactionTransferResponse;
import com.bbva.minibank.presentation.response.transaction.TransactionWithdrawalResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log4j2
public class TransactionPresentationMapper {
	
	public TransactionDepositResponse toDepositResponse(Transaction transaction,
	                                                    Client clientSaved) {
		log.info("Transaction: {}", transaction);
		return TransactionDepositResponse.builder()
		                                 .id(transaction.getId()
		                                                .toString())
		                                 .type(transaction.getType()
		                                                  .toString())
		                                 .amount(transaction.getAmount()
		                                                    .toString())
		                                 .accountNumber(transaction.getAccountNumberFrom()
		                                                           .toString())
		                                 .clientFullName(
				                                 String.format("%s, %s", clientSaved.getLastName(), clientSaved.getFirstName()))
		                                 .createdAt(transaction.getCreatedAt()
		                                                       .toString())
		                                 
		                                 .build();
		
	}
	
	public TransactionWithdrawalResponse toWithdrawalResponse(Transaction withdraw,
	                                                          Client clientSaved) {
		log.info("Transaction: {}", withdraw);
		return TransactionWithdrawalResponse.builder()
		                                    .id(withdraw.getId()
		                                                .toString())
		                                    .type(withdraw.getType()
		                                                  .toString())
		                                    .amountExtracted(withdraw.getAmount()
		                                                             .toString())
		                                    .accountNumberFrom(withdraw.getAccountNumberFrom()
		                                                               .toString())
		                                    .clientFullName(String.format("%s, %s", clientSaved.getLastName(),
		                                                                  clientSaved.getFirstName()))
		                                    .createdAt(withdraw.getCreatedAt()
		                                                       .toString())
		                                    .build();
	}
	
	public TransactionTransferResponse toTransferResponse(Transaction transfer,
	                                                      Client clientSaved) {
		log.info("Transaction: {}", transfer);
		return TransactionTransferResponse.builder()
		                                  .id(transfer.getId()
		                                              .toString())
		                                  .type(transfer.getType()
		                                                .toString())
		                                  .amount(transfer.getAmount()
		                                                  .toString())
		                                  .accountNumberFrom(transfer.getAccountNumberFrom()
		                                                             .toString())
		                                  .accountNumberTo(transfer.getAccountNumberTo()
		                                                           .toString())
		                                  .clientFullName(String.format("%s, %s", clientSaved.getLastName(),
		                                                                clientSaved.getFirstName()))
		                                  .createdAt(transfer.getCreatedAt()
		                                                     .toString())
		                                  .build();
	}
	
	public TransactionResponse toResponse(Transaction transaction) {
		log.info("Transaction: {}", transaction);
		return TransactionResponse.builder()
		                          .id(UUID.fromString(transaction.getId()
		                                                         .toString()))
		                          .type(TransactionTypeEnum.valueOf(transaction.getType()
		                                                                       .toString()))
		                          .amount(transaction.getAmount())
		                          .accountNumberFrom(transaction.getAccountNumberFrom())
		                          .accountNumberTo(transaction.getAccountNumberTo())
		                          .createdAt(transaction.getCreatedAt())
		                          .build();
	}
}
