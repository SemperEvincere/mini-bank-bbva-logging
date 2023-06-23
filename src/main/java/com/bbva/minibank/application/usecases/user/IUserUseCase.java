package com.bbva.minibank.application.usecases.user;

import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface IUserUseCase {
	void save(UserEntity userEntity);
	
	
	UserEntity createUser(@Valid CreateUserRequest userRequest);
	
	UserEntity findUserById(UUID userId);
}
