package io.lynx.oebs.controllers;

import io.lynx.oebs.dtos.CreateAccountRequest;
import io.lynx.oebs.dtos.GenericAPIResponse;
import io.lynx.oebs.dtos.JwtTokenResponse;
import io.lynx.oebs.dtos.OTPVerificationRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.helpers.Helpers;
import io.lynx.oebs.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account createdAccount = accountService.createAccount(account);
        log.info("created account {}", Helpers.toJson(createdAccount));
        return new ResponseEntity<Object>(GenericAPIResponse.builder().data("success! email sent for verification, please check your email.").build(), HttpStatus.CREATED);
    }


    @PostMapping("/accounts/verify")
    public ResponseEntity<?> verifyAccount(@RequestBody OTPVerificationRequest otpVerificationRequest) {
        return new ResponseEntity<Object>(
                GenericAPIResponse.builder().data(JwtTokenResponse.builder().token(accountService.verifyAccount(otpVerificationRequest.getOtp(), otpVerificationRequest.getEmail()))
                                .build())
                        .build(),
                HttpStatus.OK
        );
    }
}


