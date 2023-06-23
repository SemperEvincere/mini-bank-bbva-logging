package com.bbva.minibank.application.usecases.client;

import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.domain.models.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientFindByUseCase {
	
	List<Client> getAll();
	
	Optional<Client> findById(UUID id);
	
	UUID getAccountClient(Transaction transaction,
	                      Client client);
	
	Optional<Client> findByIdAndIsActive(UUID id);
}
