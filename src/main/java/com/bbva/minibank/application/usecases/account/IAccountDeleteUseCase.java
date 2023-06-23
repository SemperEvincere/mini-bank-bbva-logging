package com.bbva.minibank.application.usecases.account;

import com.bbva.minibank.domain.models.Account;

public interface IAccountDeleteUseCase {
	void blockAccount(Account account);
}
