package io.lynx.oebs.services;

import io.lynx.oebs.dtos.CreateAccountRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.exceptions.ResourceConflictException;
import io.lynx.oebs.exceptions.ResourceNotFoundException;
import io.lynx.oebs.repositories.AccountRepository;
import io.lynx.oebs.services.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;

    @Autowired
    public AccountService(AccountRepository accountRepository, EmailService emailService) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
    }

    public Account createAccount(CreateAccountRequest createAccountRequest) {
        String otp = RandomStringUtils.randomNumeric(6); // Generate a 6-digit OTP

        Account account = Account.builder()
                .email(createAccountRequest.getEmail())
                .password(createAccountRequest.getPassword())
                .phoneNumber(createAccountRequest.getPhoneNumber())
                .lang(createAccountRequest.getLang())
                .otp(otp)
                .build();
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new ResourceConflictException("email is already in use.");
        }
        if (accountRepository.existsByPhoneNumber(account.getPhoneNumber())) {
            throw new ResourceConflictException("phone number is already in use.");
        }
        Account savedAccount = accountRepository.save(account);

        // Prepare the email model and send asynchronously
        Map<String, String> model = new HashMap<>();
        model.put("otp", otp);
        emailService.sendEmail(
                new String[]{account.getEmail()},
                "d-e8e83baf8a8c4c849fbd810716656909",
                model
        );
        return savedAccount;
    }

    public boolean verifyAccount(String otp, String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        if (account.getOtp().equals(otp)) {
            account.setEmailVerified(true);
            accountRepository.save(account);
            return true;

        }
        return false;
    }

}

