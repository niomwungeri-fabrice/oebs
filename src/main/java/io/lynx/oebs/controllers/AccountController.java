package io.lynx.oebs.controllers;

import io.lynx.oebs.dtos.CreateAccountRequest;
import io.lynx.oebs.dtos.LoginRequest;
import io.lynx.oebs.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> createAccount(@RequestBody CreateAccountRequest account) {
        Map<String, String> response = new HashMap<>();
        try {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountService.saveAccount(account);
            // TODO: sent email/Phone Number for vertical
            response.put("message", "success! email sent for verification, please check your email.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            String token = accountService.authenticateAndGenerateToken(loginRequest);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
}
