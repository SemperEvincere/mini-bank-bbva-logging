package com.bbva.minibank.infrastructure.repositories.springdatajpa;

import com.bbva.minibank.infrastructure.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITransactionSpringRepository extends JpaRepository<TransactionEntity, UUID> {

}
