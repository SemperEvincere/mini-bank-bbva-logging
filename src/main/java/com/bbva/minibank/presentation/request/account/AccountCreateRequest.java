package com.bbva.minibank.presentation.request.account;

import com.bbva.minibank.domain.models.enums.CurrencyEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class AccountCreateRequest {
	
	private UUID holderId;
	private CurrencyEnum currency;
	private UUID secondHolderId;
	
}
