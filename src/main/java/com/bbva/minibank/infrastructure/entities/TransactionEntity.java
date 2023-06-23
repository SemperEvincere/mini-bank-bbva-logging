package com.bbva.minibank.infrastructure.entities;

import com.bbva.minibank.domain.models.enums.TransactionTypeEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	UUID id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime timestamp;
	
	@Enumerated(value = EnumType.STRING)
	private TransactionTypeEnum type;
	
	@PositiveOrZero
	private BigDecimal amount;
	
	@NotNull
	private UUID accountNumberFrom;
	
	@Nullable
	private UUID accountNumberTo;
	
}
