package com.bbva.minibank.application.usecases.client;

import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;

public interface IClientUpdateUseCase {
	
	Client update(Client client);
	
	void addAccount(Client client, Account account);
	
	Client restoreDeletedClient(Client client);
}
