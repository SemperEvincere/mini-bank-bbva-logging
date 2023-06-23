package com.bbva.minibank.presentation.request.account;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class AccountAddCoholder {
	
	private UUID holderId;
	private UUID accountId;
	private CurrencyEnum currency;
	private UUID secondHolderId;
}
