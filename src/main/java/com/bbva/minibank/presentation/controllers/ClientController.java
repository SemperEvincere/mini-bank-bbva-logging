package com.bbva.minibank.presentation.controllers;

import com.bbva.minibank.application.usecases.account.IAccountFindUseCase;
import com.bbva.minibank.application.usecases.client.*;
import com.bbva.minibank.application.usecases.user.IUserUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.mappers.AccountPresentationMapper;
import com.bbva.minibank.presentation.mappers.ClientPresentationMapper;
import com.bbva.minibank.presentation.request.client.ClientCreateRequest;
import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import com.bbva.minibank.presentation.response.client.ClientAllDataResponse;
import com.bbva.minibank.presentation.response.client.ClientResponse;
import com.bbva.minibank.presentation.response.errors.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Log4j2
public class ClientController {
	
	private final IClientCreateUseCase clientCreateUseCase;
	private final IClientSaveUseCase clientSaveUseCase;
	private final IClientFindByUseCase clientFindByUseCase;
	private final ClientPresentationMapper clientMapper;
	private final AccountPresentationMapper accountMapper;
	private final IAccountFindUseCase accountFindUseCase;
	private final IClientUpdateUseCase clientUpdateUseCase;
	private final IClientDeleteUseCase clientDeleteUseCase;
	private final IUserUseCase userUseCase;
	
	private static ResponseEntity<ErrorResponse> getErrorResponseEntity(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getFieldErrors()
			                                   .stream()
			                                   .map(FieldError::getDefaultMessage)
			                                   .collect(Collectors.toList());
			
			ErrorResponse errorResponse = new ErrorResponse("Error de validación", errors);
			log.error("Error de validación: {}", errorResponse);
			return ResponseEntity.badRequest()
			                     .body(errorResponse);
		}
		return null;
	}
	
	@PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> create(@Valid @RequestBody ClientCreateRequest request,
	                                BindingResult bindingResult) {
		ResponseEntity<ErrorResponse> errorResponse = getErrorResponseEntity(bindingResult);
		if (errorResponse != null) {
			log.error("Error de validación: {}", errorResponse);
			return errorResponse;
		}
		UserEntity userEntity = userUseCase.findUserById(request.getUserId());
		if (userEntity == null) {
			log.error("Error de validación: {}", "El usuario no existe");
			return ResponseEntity.notFound()
			                     .build();
		}
		log.warn("Creando cliente: {}", request);
		Client client = clientCreateUseCase.create(request, userEntity);
		Client savedClient = clientSaveUseCase.save(client);
		ClientResponse response = clientMapper.domainToResponse(savedClient);
		log.info("Cliente creado: {}", response);
		return ResponseEntity.status(HttpStatus.CREATED)
		                     .body(response);
		
	}
	
	@GetMapping(value = "/", produces = "application/json")
	@ResponseBody
	public List<ClientResponse> getAll() {
		log.info("Obteniendo todos los clientes");
		return clientFindByUseCase.getAll()
		                          .stream()
		                          .map(clientMapper::domainToResponse)
		                          .collect(Collectors.toList());
		
	}
	
	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseBody
	public ClientAllDataResponse getOne(@PathVariable("id") UUID id) {
		Optional<Client> clientOptional = clientFindByUseCase.findByIdAndIsActive(id);
		log.info("Obteniendo cliente con id: {}", id);
		return clientOptional.map(client -> {
			                     List<Account> accounts = client.getAccounts()
			                                                    .stream()
			                                                    .map(accountFindUseCase::findByAccountNumber)
			                                                    .toList();
			                     List<AccountDetailsResponse> accountDetailsResponses = accountMapper.domainToDetailResponseList(accounts);
			                     return clientMapper.domainToAllDataResponse(client, accountDetailsResponses);
		                     })
		                     .orElse(null);
	}
	
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> update(@PathVariable("id") UUID id, @Valid @RequestBody ClientCreateRequest request,
	                                BindingResult bindingResult) {
		ResponseEntity<ErrorResponse> errorResponse = getErrorResponseEntity(bindingResult);
		log.info("Actualizando cliente con id: {}", id);
		if (errorResponse != null) {
			log.error("Error de validación: {}", errorResponse);
			return ResponseEntity.badRequest()
			                     .body(errorResponse);
		}
		Client client = clientFindByUseCase.findById(id)
		                                   .orElse(null);
		if (client == null) {
			log.error("Error de validación: {}", "El cliente no existe");
			return ResponseEntity.notFound()
			                     .build();
		}
		Client clientUpdated = clientMapper.updateDomainFromRequest(client, request, id);
		Client savedUpdatedClient = clientUpdateUseCase.update(clientUpdated);
		ClientResponse response = clientMapper.domainToResponse(savedUpdatedClient);
		log.info("Cliente actualizado: {}", response);
		return ResponseEntity.ok(response);
		
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
		log.info("Eliminando cliente con id: {}", id);
		Client client = clientFindByUseCase.findByIdAndIsActive(id)
		                                   .orElse(null);
		if (client == null) {
			log.warn("Cliente no encontrado con id: {}", id);
			return ResponseEntity.notFound()
			                     .build();
		}
		clientDeleteUseCase.delete(client);
		log.info("Cliente eliminado con id: {}", id);
		return ResponseEntity.ok("Client delete");
	}
	
	@PostMapping(value = "/restore/{id}", produces = "application/json")
	public ResponseEntity<?> restoreDeletedClient(@PathVariable("id") UUID id) {
		log.info("Restaurando cliente con id: {}", id);
		Client client = clientFindByUseCase.findById(id)
		                                   .orElse(null);
		if (client == null) {
			log.warn("Cliente no encontrado con id: {}", id);
			return ResponseEntity.notFound()
			                     .build();
		}
		Client clienteRestored = clientUpdateUseCase.restoreDeletedClient(client);
		ClientResponse response = clientMapper.domainToResponse(clienteRestored);
		log.info("Cliente restaurado: {}", response);
		return ResponseEntity.ok(response);
	}
	
	
}
