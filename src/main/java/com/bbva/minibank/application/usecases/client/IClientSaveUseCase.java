package com.bbva.minibank.application.usecases.client;

import com.bbva.minibank.domain.models.Client;

public interface IClientSaveUseCase {
	
	Client save(Client client);
}
