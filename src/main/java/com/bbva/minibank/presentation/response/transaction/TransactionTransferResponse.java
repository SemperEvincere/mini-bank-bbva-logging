package com.bbva.minibank.presentation.response.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionTransferResponse {
	
	private String id;
	private String type;
	private String amount;
	private String accountNumberFrom;
	private String accountNumberTo;
	private String clientFullName;
	private String createdAt;
}
