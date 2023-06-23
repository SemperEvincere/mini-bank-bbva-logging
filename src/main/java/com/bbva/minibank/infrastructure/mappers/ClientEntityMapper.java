package com.bbva.minibank.infrastructure.mappers;

import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.AccountEntity;
import com.bbva.minibank.infrastructure.entities.ClientEntity;
import com.bbva.minibank.infrastructure.repositories.AccountRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component()
@RequiredArgsConstructor
public class ClientEntityMapper {
	
	private final AccountEntityMapper accountEntityMapper;
	private final AccountRepositoryImpl accountFindUseCase;
	
	public ClientEntity domainToEntity(Client client) {
		return ClientEntity
				       .builder()
				       .id(client.getId())
				       .createdAt((client.getCreatedAt() == null) ? LocalDate.now() : client.getCreatedAt())
				       .firstName(client.getFirstName())
				       .lastName(client.getLastName())
				       .email(client.getEmail())
				       .phone(client.getPhone())
				       .address(client.getAddress())
				       .accounts(client.getAccounts()
				                       .stream()
				                       .map(accountFindUseCase::findByAccountNumber)
				                       .map(accountEntityMapper::domainToEntity)
				                       .collect(Collectors.toSet()))
				       .isActive(true)
				       .build();
	}
	
	
	public Client entityToDomain(ClientEntity clientEntity) {
		
		Client.ClientBuilder clientBuilder =
				Client.builder()
				      .id(clientEntity.getId())
				      .createdAt(clientEntity.getCreatedAt())
				      .firstName(clientEntity.getFirstName())
				      .lastName(clientEntity.getLastName())
				      .email(clientEntity.getEmail())
				      .phone(clientEntity.getPhone())
				      .address(clientEntity.getAddress())
				      .accounts(new ArrayList<UUID>())
				      .updatedAt(clientEntity.getUpdatedAt());
		
		
		Set<AccountEntity> accountEntities = new HashSet<>(clientEntity.getAccounts());
		List<UUID> accountList = new ArrayList<>(); // Creamos una nueva lista mutable
		
		if (!accountEntities.isEmpty()) {
			for (AccountEntity accountEntity : accountEntities) {
				if (accountEntity != null) {
					accountList.add(accountEntity.getAccountNumber()); // Agregamos cada cuenta a la lista mutable
				}
			}
		}
		
		clientBuilder.accounts(accountList); // Asignamos la lista mutable al builder
		
		return clientBuilder.build();
		
	}
	
	
}
