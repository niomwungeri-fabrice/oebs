package io.lynx.oebs.services;

import io.lynx.oebs.configs.JwtTokenProvider;
import io.lynx.oebs.dtos.CreateAccountRequest;
import io.lynx.oebs.dtos.GenericAPIResponse;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.exceptions.InternalServerException;
import io.lynx.oebs.exceptions.ResourceConflictException;
import io.lynx.oebs.exceptions.ResourceNotFoundException;
import io.lynx.oebs.exceptions.ResourceUnAuthorizedException;
import io.lynx.oebs.repositories.AccountRepository;
import io.lynx.oebs.services.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AccountService(AccountRepository accountRepository, EmailService emailService, JwtTokenProvider jwtTokenProvider) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Account createAccount(CreateAccountRequest createAccountRequest) {
        try {
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
//        emailService.sendEmail(
//                new String[]{account.getEmail()},
//                "d-e8e83baf8a8c4c849fbd810716656909", /// TODO: should be env variable
//                model
//        );
            return savedAccount;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String verifyAccount(String otp, String email) {
        try {
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));
            if (account.getOtp().equals(otp)) {
                account.setEmailVerified(true);
                accountRepository.save(account);
                return jwtTokenProvider.getToken(email);
            }
            throw new ResourceUnAuthorizedException("otp verification failed.");
        } catch (InternalServerException e) {
            throw new InternalServerException();
        }
    }
}

