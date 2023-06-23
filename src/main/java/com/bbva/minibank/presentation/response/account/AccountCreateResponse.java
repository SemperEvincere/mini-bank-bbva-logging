package com.bbva.minibank.presentation.response.account;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountCreateResponse {
	
	private UUID accountId;
	private UUID holderId;
	private List<UUID> secondHolderId;
	private CurrencyEnum currency;
	private BigDecimal balance;
	
	
}
