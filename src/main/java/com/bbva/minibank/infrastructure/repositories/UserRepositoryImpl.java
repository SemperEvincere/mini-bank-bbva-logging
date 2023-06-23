package com.bbva.minibank.infrastructure.repositories;

import com.bbva.minibank.application.repository.IUserRepository;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.infrastructure.repositories.springdatajpa.IUserSpringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Log4j2
public class UserRepositoryImpl implements IUserRepository {
	
	private final IUserSpringRepository userSpringRepository;
	
	@Override
	public Optional<UserEntity> findByUsername(String username) {
		log.info("Getting user by username: {}", username);
		return userSpringRepository.findByUsername(username);
	}
	
	@Override
	public void save(UserEntity userEntity) {
		log.info("Saving user: {}", userEntity);
		userSpringRepository.save(userEntity);
	}
	
	@Override
	public UserEntity findUserById(UUID userId) {
		log.info("Getting user by id: {}", userId);
		return userSpringRepository.findById(userId)
		                           .orElseThrow(() -> new RuntimeException("El usuario no existe."));
	}
}
