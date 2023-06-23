package com.bbva.minibank.infrastructure.init;

import com.bbva.minibank.application.services.UserService;
import com.bbva.minibank.domain.models.enums.ERole;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.presentation.request.user.CreateUserRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Log4j2
public class DataInitializer implements ApplicationRunner {
	
	private final UserService userService;
	
	@Autowired
	public DataInitializer(UserService userService) {
		log.info("Initializing data...");
		this.userService = userService;
	}
	
	@Override
	public void run(ApplicationArguments args) {
		// Crear un usuario ADMIN
		log.info("Creating admin user...");
		Set<ERole> roles = new HashSet<>();
		roles.add(ERole.valueOf("ADMIN"));
		
		CreateUserRequest adminUserRequest = new CreateUserRequest();
		adminUserRequest.setUsername("admin");
		adminUserRequest.setPassword("admin");
		adminUserRequest.setEmail("admin@example.com");
		adminUserRequest.setRoles(roles);
		
		UserEntity adminUser = userService.createUser(adminUserRequest);
		log.info("Admin user created: {}", adminUser);
	}
}
