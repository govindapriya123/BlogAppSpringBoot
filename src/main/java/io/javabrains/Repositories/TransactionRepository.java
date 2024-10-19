package io.javabrains.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javabrains.Entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}
