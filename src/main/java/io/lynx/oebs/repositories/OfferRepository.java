package io.lynx.oebs.repositories;


import io.lynx.oebs.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findByBusiness_BusinessId(UUID businessId);

}
