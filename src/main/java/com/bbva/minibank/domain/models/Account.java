package com.bbva.minibank.domain.models;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	
	@NotNull
	private UUID accountNumber;
	@PositiveOrZero
	private BigDecimal balance;
	@Enumerated(EnumType.STRING)
	@NotNull
	private CurrencyEnum currency;
	@NotNull
	private UUID clientHolder;
	@Nullable
	private List<UUID> listSecondsHolders;
	@CreationTimestamp
	private LocalDate creationDate;
	
	private List<Transaction> transactions;
	@NotNull
	private boolean isLocked;
	
	
}
