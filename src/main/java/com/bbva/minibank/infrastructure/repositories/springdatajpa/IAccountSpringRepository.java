package com.bbva.minibank.infrastructure.repositories.springdatajpa;

import com.bbva.minibank.infrastructure.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAccountSpringRepository extends JpaRepository<AccountEntity, UUID> {

}
