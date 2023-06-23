package com.bbva.minibank.domain.models;

import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
	
	private UUID id;
	private LocalDateTime createdAt;
	private TransactionTypeEnum type;
	private BigDecimal amount;
	private UUID accountNumberFrom;
	private UUID accountNumberTo;
	
}
