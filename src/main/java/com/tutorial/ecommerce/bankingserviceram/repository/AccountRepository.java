package com.tutorial.ecommerce.bankingserviceram.repository;


import com.tutorial.ecommerce.bankingserviceram.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAadhaar(String aadhaar);
    Optional<Account> findById(Long id);
}
