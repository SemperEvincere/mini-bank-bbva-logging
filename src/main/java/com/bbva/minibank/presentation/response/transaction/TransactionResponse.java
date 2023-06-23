package com.bbva.minibank.presentation.response.transaction;

import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
	
	private UUID id;
	private LocalDateTime createdAt;
	private TransactionTypeEnum type;
	private BigDecimal amount;
	private UUID accountNumberFrom;
	private UUID accountNumberTo;
}
