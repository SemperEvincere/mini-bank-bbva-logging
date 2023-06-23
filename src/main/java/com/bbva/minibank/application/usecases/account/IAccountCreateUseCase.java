package com.bbva.minibank.application.usecases.account;

import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.enums.CurrencyEnum;

public interface IAccountCreateUseCase {
	
	Account create(CurrencyEnum currency,
	               Client holder,
	               Client secondHolder);
	
	
}
