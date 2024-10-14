package io.lynx.oebs.services;

import io.lynx.oebs.dtos.CreateAccountRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.exceptions.ResourceConflictException;
import io.lynx.oebs.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Account account = Account.builder()
                .email(createAccountRequest.getEmail())
                .password(createAccountRequest.getPassword())
                .phoneNumber(createAccountRequest.getPhoneNumber())
                .lang(createAccountRequest.getLang())
                .build();
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new ResourceConflictException("email is already in use.");
        }
        if (accountRepository.existsByPhoneNumber(account.getPhoneNumber())) {
            throw new ResourceConflictException("phone number is already in use.");
        }
        return accountRepository.save(account);
    }

}

