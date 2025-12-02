package org.example.flogin;

import org.example.flogin.controller.AccountController;
import org.example.flogin.dto.LoginRequestDTO;
import org.example.flogin.dto.AccountDTO;
import org.example.flogin.service.AccountService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.flogin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.example.flogin.service.CustomUserDetailsService;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AccountController.class)
@DisplayName("Login API Controller Test")
@Import(SecurityConfig.class)
class LoginAPIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/accounts/login - Login Success")
    void testLoginSuccess() throws Exception {

        LoginRequestDTO testAcc = new LoginRequestDTO("testuser","123456");
        
        AccountDTO acc = new AccountDTO(1L,"testuser","123456",LocalDateTime.now());
        

        when(accountService.validateLogin(eq("testuser"),eq("123456"))).thenReturn(true);
        
        when(accountService.getAccountByUsername(anyString()))
            .thenReturn(Optional.of(acc));

        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAcc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("POST /api/accounts/login - Wrong Password")
    void testLoginFailWrongPassword() throws Exception {

        LoginRequestDTO testAcc = new LoginRequestDTO("testuser","1234567");
        
        AccountDTO acc = new AccountDTO(1L,"testuser","123456",LocalDateTime.now());
        

        when(accountService.validateLogin(anyString(),eq("123456"))).thenReturn(true);
        
        when(accountService.getAccountByUsername(anyString()))
            .thenReturn(Optional.of(acc));


        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAcc)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Tên người dùng hoặc mật khẩu không đúng"));
    }

    @Test
    @DisplayName("Test response structure và status codes")
    void testResponseStructureAndStatus() throws Exception{
        LoginRequestDTO testAcc = new LoginRequestDTO("testuser","123456");
        
        AccountDTO acc = new AccountDTO(1L,"testuser","123456",LocalDateTime.now());
   
        when(accountService.validateLogin(anyString(),anyString())).thenReturn(true);
        
        when(accountService.getAccountByUsername(anyString()))
            .thenReturn(Optional.of(acc));

        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAcc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").value("123456"))
                .andExpect(jsonPath("$.createdDate").exists());
    }

    @Test
    @DisplayName("Test CORS và headers")
    void testLoginResponseHeaders() throws Exception {

        LoginRequestDTO req = new LoginRequestDTO("testuser", "123456");

        AccountDTO acc = new AccountDTO(1L,"testuser","123456",LocalDateTime.now());

        when(accountService.validateLogin("testuser", "123456")).thenReturn(true);
        when(accountService.getAccountByUsername("testuser")).thenReturn(Optional.of(acc));

        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                // Must return JSON
                .andExpect(header().string("Content-Type", containsString("application/json")))
                // Security headers
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-XSS-Protection", "0"))
                .andExpect(header().string("Cache-Control", containsString("no-cache")))
                .andExpect(jsonPath("$.username").value("testuser"));  // extra check
    }

}
