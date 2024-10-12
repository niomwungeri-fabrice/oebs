package io.lynx.oebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lynx.oebs.configs.CustomUserDetailsService;
import io.lynx.oebs.configs.JwtTokenProvider;
import io.lynx.oebs.configs.SecurityConfig;
import io.lynx.oebs.controllers.AccountController;
import io.lynx.oebs.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")  // Ensures application-test.properties is used if present
// Ensure SecurityConfig is used in the test
@Import({TestConfig.class, SecurityConfig.class})

public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    public void createAccount_ShouldReturnToken() throws Exception {
        Mockito.when(accountService.saveAccount(any())).thenReturn(null); // Mock account creation
        Mockito.when(jwtTokenProvider.generateToken(any())).thenReturn("sample_jwt_token");

        Map<String, String> request = new HashMap<>();
        request.put("email", "newuser@example.com");
        request.put("password", "password123");

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success! email sent for verification, please check your email."));
    }

    @Test
    public void login_ShouldReturnToken() throws Exception {
        Mockito.when(accountService.authenticateAndGenerateToken(any())).thenReturn("sample_jwt_token");

        Map<String, String> request = new HashMap<>();
        request.put("email", "existinguser@example.com");
        request.put("password", "password123");

        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sample_jwt_token"));
    }

    @Test
    public void login_ShouldReturnError_WhenAccountNotVerified() throws Exception {
        Mockito.when(accountService.authenticateAndGenerateToken(any())).thenThrow(new IllegalArgumentException("Account is not verified."));

        Map<String, String> request = new HashMap<>();
        request.put("email", "unverifieduser@example.com");
        request.put("password", "password123");

        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Account is not verified."));
    }

    @Test
    public void login_ShouldReturnError_WhenPasswordIncorrect() throws Exception {
        Mockito.when(accountService.authenticateAndGenerateToken(any())).thenThrow(new IllegalArgumentException("Incorrect password."));

        Map<String, String> request = new HashMap<>();
        request.put("email", "existinguser@example.com");
        request.put("password", "wrongpassword");

        mockMvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Incorrect password."));
    }

}
