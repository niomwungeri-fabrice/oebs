package io.lynx.oebs.repositories;

import io.lynx.oebs.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    // Find an account by email
    Optional<Account> findByEmail(String email);

    // Check if an account exists by email
    boolean existsByEmail(String email);

    // Check if an account exists by phone number
    boolean existsByPhoneNumber(String phoneNumber);
}
