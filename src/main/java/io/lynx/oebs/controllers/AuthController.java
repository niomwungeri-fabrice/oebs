package io.lynx.oebs.controllers;

import io.lynx.oebs.dtos.GenericAPIResponse;
import io.lynx.oebs.dtos.JwtTokenResponse;
import io.lynx.oebs.dtos.LoginRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.exceptions.ResourceUnAuthorizedException;
import io.lynx.oebs.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws ResourceUnAuthorizedException {
        return new ResponseEntity<Object>(
                GenericAPIResponse.builder().data(JwtTokenResponse.builder().token(authService.authenticateAndGenerateToken(loginRequest))
                                .build())
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Account account) throws ResourceUnAuthorizedException {
        return new ResponseEntity<Object>(
                GenericAPIResponse.builder().data(account)
                        .build(),
                HttpStatus.OK
        );
    }
}
