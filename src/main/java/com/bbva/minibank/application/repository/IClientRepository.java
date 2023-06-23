package com.bbva.minibank.application.repository;

import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientRepository {
	
	Client saveClient(Client client);
	
	List<Client> getAll();
	
	Optional<Client> findById(UUID id);
	
	boolean existsByEmail(String email);
	
	boolean existsByEmailAndLastNameAndFirstName(String email,
	                                             String lastName,
	                                             String firstName);
	
	Client update(Client client);
	
	void addAccount(Client client, Account account);
	
	boolean existsById(UUID email);
	
	void delete(Client client);
	
	Client restoreDeletedClient(Client client);
	
	Optional<Client> findByIdAndIsActive(UUID id);
}
