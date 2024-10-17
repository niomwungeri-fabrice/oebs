package io.lynx.oebs.services;

import io.lynx.oebs.entities.Offer;
import io.lynx.oebs.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer createOffer(Offer offer) {
        try {
            return offerRepository.save(offer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Offer> getOffersByBusiness(UUID businessId) {
        try {
            return offerRepository.findByBusiness_BusinessId(businessId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
