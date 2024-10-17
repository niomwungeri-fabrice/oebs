package io.lynx.oebs.services;

import io.lynx.oebs.configs.JwtTokenProvider;
import io.lynx.oebs.dtos.LoginRequest;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.exceptions.InternalServerException;
import io.lynx.oebs.exceptions.ResourceNotFoundException;
import io.lynx.oebs.exceptions.ResourceUnAuthorizedException;
import io.lynx.oebs.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountRepository = accountRepository;
    }

    public String authenticateAndGenerateToken(LoginRequest loginRequest) {
        try {
            Account account = accountRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("user", "email", loginRequest.getEmail()));
            if (!account.isEmailVerified()) {
                throw new ResourceUnAuthorizedException("account is not verified.");
            }
            return tokenGenerator(loginRequest);
        } catch (BadCredentialsException ex) {
            throw new ResourceUnAuthorizedException("incorrect email or password.");
        } catch (InternalServerException e) {
            throw new InternalServerException();
        }
    }

    public String tokenGenerator(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            return jwtTokenProvider.generateToken(authentication);
        } catch (InternalServerException e) {

            throw new InternalServerException();
        }
    }
}
