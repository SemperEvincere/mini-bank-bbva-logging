package com.bbva.minibank.infrastructure.repositories;

import com.bbva.minibank.application.repository.IClientRepository;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.AccountEntity;
import com.bbva.minibank.infrastructure.entities.ClientEntity;
import com.bbva.minibank.infrastructure.mappers.AccountEntityMapper;
import com.bbva.minibank.infrastructure.mappers.ClientEntityMapper;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.IClientSpringRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ClientRepositoryImpl implements IClientRepository {
	
	private final IClientSpringRepository clientSpringRepository;
	private final ClientEntityMapper clientEntityMapper;
	private final AccountEntityMapper accountEntityMapper;
	private final AccountRepositoryImpl accountFindUseCase;
	
	
	@Override
	public Client saveClient(Client client) {
		log.info("Saving client: {}", client);
		ClientEntity clientEntity = clientEntityMapper.domainToEntity(client);
		List<AccountEntity> accountEntities =
				client.getAccounts()
				      .stream()
				      .map(accountNumber -> accountEntityMapper.domainToEntity(
						      accountFindUseCase.findByAccountNumber(accountNumber)))
				      .toList();
		log.info("Accounts found: {}", accountEntities);
		clientEntity.setAccounts(new HashSet<>(accountEntities));
		log.info("Client to save: {}", clientEntity);
		clientSpringRepository.save(clientEntity);
		log.info("Client saved: {}", clientEntity);
		return clientEntityMapper.entityToDomain(clientEntity);
	}
	
	@Override
	public List<Client> getAll() {
		log.info("Getting all clients");
		return clientSpringRepository
				       .findAll()
				       .stream()
				       .filter(ClientEntity::getIsActive)
				       .map(clientEntityMapper::entityToDomain)
				       .collect(Collectors.toList());
	}
	
	@Override
	public Optional<Client> findById(UUID id) {
		log.info("Getting client by id: {}", id);
		Optional<ClientEntity> optionalClient = clientSpringRepository.findById(id);
		if (optionalClient.isEmpty()) {
			log.info("Client not found");
			return Optional.empty();
		}
		log.info("Client found: {}", optionalClient.get());
		return optionalClient.map(clientEntityMapper::entityToDomain);
	}
	
	@Override
	public boolean existsByEmail(String email) {
		log.info("Checking if client exists by email: {}", email);
		return clientSpringRepository.existsByEmail(email);
	}
	
	@Override
	public boolean existsByEmailAndLastNameAndFirstName(String email,
	                                                    String lastName,
	                                                    String firstName) {
		log.info("Checking if client exists by email: {}, lastName: {}, firstName: {}", email, lastName, firstName);
		return clientSpringRepository.existsByEmailAndLastNameAndFirstName(email, lastName, firstName);
	}
	
	@Override
	@Transactional
	public Client update(Client client) {
		log.info("Updating client: {}", client);
		if (client == null) {
			log.error("Client can not be null");
			throw new IllegalArgumentException("Client can not be null");
		}
		ClientEntity clientEntity = clientEntityMapper.domainToEntity(client);
		List<AccountEntity> accountEntities =
				client.getAccounts()
				      .stream()
				      .map(accountNumber -> accountEntityMapper.domainToEntity(
						      accountFindUseCase.findByAccountNumber(accountNumber)))
				      .toList();
		log.info("Accounts found: {}", accountEntities);
		clientEntity.setAccounts(new HashSet<>(accountEntities));
		clientEntity.setUpdatedAt(LocalDate.now());
		log.info("Client to update: {}", clientEntity);
		return clientEntityMapper.entityToDomain(clientSpringRepository.save(clientEntity));
	}
	
	@Override
	public void addAccount(Client client, Account account) {
		log.info("Adding account: {} to client: {}", account, client);
		ClientEntity clientEntity = clientEntityMapper.domainToEntity(client);
		AccountEntity accountEntity = accountEntityMapper.domainToEntity(account);
		clientEntity.getAccounts()
		            .add(accountEntity);
		log.info("Client to update: {}", clientEntity);
		clientSpringRepository.save(clientEntity);
	}
	
	@Override
	public boolean existsById(UUID id) {
		log.info("Checking if client exists by id: {}", id);
		return clientSpringRepository.existsById(id);
	}
	
	@Override
	public void delete(Client client) {
		log.info("Deleting client: {}", client);
		ClientEntity clientEntity = clientEntityMapper.domainToEntity(client);
		clientEntity.setIsActive(false);
		log.info("Client to delete: {}", clientEntity);
		clientSpringRepository.save(clientEntity);
	}
	
	@Override
	public Client restoreDeletedClient(Client client) {
		log.info("Restoring client: {}", client);
		ClientEntity clientEntity = clientEntityMapper.domainToEntity(client);
		clientEntity.setIsActive(true);
		log.info("Client to restore: {}", clientEntity);
		return clientEntityMapper.entityToDomain(clientSpringRepository.save(clientEntity));
	}
	
	@Override
	public Optional<Client> findByIdAndIsActive(UUID id) {
		log.info("Getting client by id: {}", id);
		return clientSpringRepository.findByIdAndIsActive(id, true)
		                             .map(clientEntityMapper::entityToDomain);
	}
}
