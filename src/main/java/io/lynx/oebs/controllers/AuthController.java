package io.lynx.oebs.controllers;

import io.lynx.oebs.constants.ErrorMessages;
import io.lynx.oebs.dtos.GenericAPIResponse;
import io.lynx.oebs.dtos.JwtTokenResponse;
import io.lynx.oebs.dtos.LoginRequest;
import io.lynx.oebs.exceptions.ResourceUnAuthorizedException;
import io.lynx.oebs.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String token = authService.authenticateAndGenerateToken(loginRequest);
        return new ResponseEntity<Object>(
                GenericAPIResponse.builder().data(JwtTokenResponse.builder().token(token)
                                .build())
                        .build(),
                HttpStatus.OK
        );
    }
}
