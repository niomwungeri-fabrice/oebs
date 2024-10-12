package io.lynx.oebs.services;

import io.lynx.oebs.configs.JwtTokenProvider;
import io.lynx.oebs.dtos.CreateAccountRequest;
import io.lynx.oebs.dtos.LoginRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AccountService(AccountRepository accountRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Account saveAccount(CreateAccountRequest createAccountRequest) {

        Account account = Account.builder()
                .email(createAccountRequest.getEmail())
                .password(createAccountRequest.getPassword())
                .phoneNumber(createAccountRequest.getPhoneNumber())
                .lang(createAccountRequest.getLang())
                .build();
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (accountRepository.existsByPhoneNumber(account.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }
        return accountRepository.save(account);
    }


    public String authenticateAndGenerateToken(LoginRequest loginRequest) {
        // Check if the account exists
        Account account = accountRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with email: " + loginRequest.getEmail()));

        // Check if the account is verified
        if (!account.isEmailVerified()) {
            throw new IllegalArgumentException("Account is not verified.");
        }

        // Attempt to authenticate with the provided credentials
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            return jwtTokenProvider.generateToken(authentication);
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Incorrect password.");
        }
    }
}

