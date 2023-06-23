package com.bbva.minibank.application.usecases.client;

import com.bbva.minibank.domain.models.Client;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.client.ClientCreateRequest;
import jakarta.validation.Valid;

public interface IClientCreateUseCase {
	
	Client create(@Valid ClientCreateRequest request, UserEntity userEntity);
}
