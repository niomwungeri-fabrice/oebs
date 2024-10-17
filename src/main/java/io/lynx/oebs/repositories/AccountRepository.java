package io.lynx.oebs.repositories;

import io.lynx.oebs.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByAccountId(UUID uuid);

    boolean existsByPhoneNumber(String phoneNumber);
}
