package io.lynx.oebs.controllers;


import io.lynx.oebs.constants.ErrorMessages;
import io.lynx.oebs.dtos.CreateBusinessRequest;
import io.lynx.oebs.dtos.CustomPage;
import io.lynx.oebs.dtos.GenericAPIResponse;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.entities.Business;
import io.lynx.oebs.helpers.Helpers;
import io.lynx.oebs.services.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<?> createBusiness(@RequestBody CreateBusinessRequest businessRequest, @AuthenticationPrincipal Account account) {
        log.info("logged in user: {}", Helpers.toJson(account));
        Business business = businessService.createBusiness(businessRequest, account);
        log.info("created business: {}", Helpers.toJson(business));
        return new ResponseEntity<Object>(GenericAPIResponse.builder().data("success! business has been created").build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllBusinesses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Business> businessPage = businessService.getAllBusinesses(pageable);
        CustomPage<Business> customPage = new CustomPage<>(
                businessPage.getContent(),
                businessPage.getNumber(),
                businessPage.getSize(),
                businessPage.getTotalElements(),
                businessPage.getTotalPages(),
                businessPage.isLast()
        );
        return new ResponseEntity<Object>(GenericAPIResponse.builder().data(customPage).build(), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getBusinessesByOwner(@PathVariable UUID ownerId) {
        return new ResponseEntity<Object>(GenericAPIResponse.builder().data(businessService.getBusinessesByOwner(ownerId)).build(), HttpStatus.OK);
    }
}
