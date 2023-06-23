package com.bbva.minibank.infrastructure.entities;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
	
	@Id
	@Column(nullable = false, unique = true)
	private UUID accountNumber;
	
	@Column(nullable = false)
	@PositiveOrZero
	private BigDecimal balance;
	
	@CreationTimestamp
	private LocalDate creationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CurrencyEnum currency;
	
	@OneToOne(fetch = FetchType.LAZY)
	private ClientEntity owner;
	
	@ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
	private List<ClientEntity> coHolders;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<TransactionEntity> transactions;
	
	private boolean isLocked;
	
	public List<ClientEntity> getCoHolders() {
		// Si la lista es nula, devolver una lista vacía en su lugar
		return coHolders != null ? coHolders : new ArrayList<>();
	}
}
