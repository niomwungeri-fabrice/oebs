package io.lynx.oebs.controllers;

import io.lynx.oebs.constants.ErrorMessages;
import io.lynx.oebs.dtos.CreateOfferRequest;
import io.lynx.oebs.dtos.GenericAPIResponse;
import io.lynx.oebs.entities.Offer;
import io.lynx.oebs.services.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody CreateOfferRequest offerRequest) {
        Offer offer = offerService.createOffer(offerRequest.toOffer());
        log.info("created offer: {}", offer);
        return new ResponseEntity<Object>(GenericAPIResponse.builder().data(offer).build(), HttpStatus.CREATED);
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<?> getOffersByBusiness(@PathVariable UUID businessId) {
        List<Offer> offers = offerService.getOffersByBusiness(businessId);
        return new ResponseEntity<Object>(GenericAPIResponse.builder().data(offers).build(), HttpStatus.OK);
    }
}
