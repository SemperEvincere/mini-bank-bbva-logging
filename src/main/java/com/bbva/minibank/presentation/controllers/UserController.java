package com.bbva.minibank.presentation.controllers;

import com.bbva.minibank.application.usecases.user.IUserUseCase;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
	
	private final IUserUseCase userUseCase;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
		UserEntity userEntity = userUseCase.createUser(createUserRequest);
		log.info("User created: {}", userEntity);
		return ResponseEntity.ok(userEntity);
	}
}
