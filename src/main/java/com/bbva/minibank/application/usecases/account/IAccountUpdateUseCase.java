package com.bbva.minibank.application.usecases.account;

import com.bbva.minibank.domain.models.Account;

public interface IAccountUpdateUseCase {
	
	Account update(Account account);
	
}
