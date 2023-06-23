package com.bbva.minibank.infrastructure.repositories.springdatajpa;

import com.bbva.minibank.infrastructure.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IClientSpringRepository extends JpaRepository<ClientEntity, UUID> {
	
	boolean existsByEmail(String email);
	
	boolean existsByEmailAndLastNameAndFirstName(String email,
	                                             String lastName,
	                                             String firstName);
	
	//	@Query("SELECT c FROM ClientEntity c WHERE c.id = :id AND c.isActive = :isActive")
	Optional<ClientEntity> findByIdAndIsActive(UUID id, boolean isActive);
}
