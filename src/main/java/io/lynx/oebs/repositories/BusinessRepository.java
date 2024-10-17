package io.lynx.oebs.repositories;


import io.lynx.oebs.entities.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    List<Business> findByOwner_AccountId(UUID ownerId);
    Page<Business> findAll(Pageable pageable);

}
