package io.lynx.oebs.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // Injected secret key and expiration time from application properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key key;

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtTokenProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // Initialize the key after the secret is injected
    @PostConstruct
    protected void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    // Generate a JWT token for an authenticated user
    public String generateToken(Authentication authentication) {
        return getToken(authentication.getName());
    }

    public String getToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Parse JWT token to extract user email
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate a JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("error validating token -:{}", e.getMessage());
            return false;
        }
    }
}
