package com.bbva.minibank.presentation.response.account;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDetailsResponse {
	private UUID accountNumber;
	private LocalDate creationDate;
	private UUID holderId;
	private String holderName;
	private Map<UUID, String> mapSecondsHolderId;
	private CurrencyEnum currency;
	private BigDecimal balance;
	private boolean isLocked;
}
