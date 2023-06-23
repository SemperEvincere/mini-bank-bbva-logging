package com.bbva.minibank.presentation.mappers;

import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.client.ClientCreateRequest;
import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import com.bbva.minibank.presentation.response.client.ClientAllDataResponse;
import com.bbva.minibank.presentation.response.client.ClientResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class ClientPresentationMapper {
	
	public ClientResponse domainToResponse(Client client) {
		log.info("Client: {}", client);
		return ClientResponse
				       .builder()
				       .id(client.getId())
				       .createDate(client.getCreatedAt())
				       .firstName(client.getFirstName())
				       .lastName(client.getLastName())
				       .email(client.getEmail())
				       .phone(client.getPhone())
				       .address(client.getAddress())
				       .updatedAt(client.getUpdatedAt())
				       
				       .build();
	}
	
	public Client requestToDomain(ClientCreateRequest request, UserEntity userEntity) {
		log.info("Client request: {}", request);
		return Client.builder()
		             .id(UUID.randomUUID())
		             .firstName(request.getFirstName())
		             .lastName(request.getLastName())
		             .email(userEntity.getEmail())
		             .phone(request.getPhone())
		             .address(request.getAddress())
		             .accounts(new ArrayList<UUID>())
		             .userId(userEntity.getId())
		             .build();
	}
	
	public ClientAllDataResponse domainToAllDataResponse(Client client,
	                                                     List<AccountDetailsResponse> accountResponse) {
		log.info("Client: {}", client);
		return ClientAllDataResponse
				       .builder()
				       .id(client.getId())
				       .createDate(client.getCreatedAt())
				       .firstName(client.getFirstName())
				       .lastName(client.getLastName())
				       .email(client.getEmail())
				       .phone(client.getPhone())
				       .address(client.getAddress())
				       .accounts(accountResponse)
				       .updatedAt(client.getUpdatedAt())
				       .build();
	}
	
	public Client updateDomainFromRequest(Client client, ClientCreateRequest request, UUID id) {
		log.info("Client request: {}", request);
		client.setId(id);
		client.setFirstName(request.getFirstName());
		client.setLastName(request.getLastName());
		client.setEmail(request.getEmail());
		client.setPhone(request.getPhone());
		client.setAddress(request.getAddress());
		log.info("Client: {}", client);
		return client;
	}
	
	
}
