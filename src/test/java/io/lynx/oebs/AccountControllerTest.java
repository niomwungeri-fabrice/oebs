package io.lynx.oebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lynx.oebs.configs.CustomUserDetailsService;
import io.lynx.oebs.configs.JwtTokenProvider;
import io.lynx.oebs.configs.SecurityConfig;
import io.lynx.oebs.entities.Account;
import io.lynx.oebs.repositories.AccountRepository;
import io.lynx.oebs.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // Uses application-test.properties for test environment
@Import({TestConfig.class,SecurityConfig.class})  // Ensures SecurityConfig is used
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // To encode the password

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setupUser() {
        // Clear any existing data to avoid conflicts
        accountRepository.deleteAll();
        // Create a new Account entity and set all required fields
        Account account = new Account();
        account.setEmail("verifieduser@example.com");
        account.setPassword( passwordEncoder.encode("password123")); // Encode the password
        account.setEmailVerified(true); // Set is_verified to true
        account.setPhoneNumber("1234567890"); // Additional fields can be set here

        // Save the account directly with the repository
        accountRepository.save(account);
    }

    @Test
    @WithMockUser
    public void createAndAuthenticateAccount_ShouldReturnAccountNotVerified() throws Exception {
        // Step 1: Create the account using the /api/accounts endpoint
        Map<String, String> createAccountRequest = new HashMap<>();
        createAccountRequest.put("email", "newuser@example.com");
        createAccountRequest.put("password", "password123");

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.message").value("success! email sent for verification, please check your email."));
        // Step 2: Authenticate the account using the /api/token endpoint
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "newuser@example.com");
        loginRequest.put("password", "password123");

        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("account is not verified."));
    }


    @Test
    public void login_ShouldReturnToken() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "verifieduser@example.com");
        request.put("password", "password123");

        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists());
    }

//    @Test
//    public void login_ShouldReturnError_WhenPasswordIncorrect() throws Exception {
//        Map<String, String> request = new HashMap<>();
//        request.put("email", "verifieduser@example.com");
//        request.put("password", "wrongPassword123");
//
//        mockMvc.perform(post("/api/token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message").value("Incorrect password."));
//    }

}
