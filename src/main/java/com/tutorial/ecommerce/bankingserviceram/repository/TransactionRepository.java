package com.tutorial.ecommerce.bankingserviceram.repository;


import com.tutorial.ecommerce.bankingserviceram.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdAndTimestampBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Transaction> findById(Long accountId);
    List<Transaction> findByAccountId(Long accountId);
}

