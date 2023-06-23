package com.bbva.minibank.presentation.response.account;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@JsonInclude
@Getter
@Setter
public class AccountResponse {
	
	private UUID id;
	private UUID clientHolderId;
	private BigDecimal balance;
	private CurrencyEnum currency;
	
}
