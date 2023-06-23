package com.bbva.minibank.presentation.response.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionWithdrawalResponse {
	
	private String id;
	private String type;
	private String amountExtracted;
	private String accountNumberFrom;
	private String clientFullName;
	private String createdAt;
}
