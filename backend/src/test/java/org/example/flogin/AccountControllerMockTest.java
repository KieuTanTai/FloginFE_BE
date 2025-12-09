package org.example.flogin;

import org.example.flogin.controller.AccountController;
import org.example.flogin.dto.AccountDTO;
import org.example.flogin.service.AccountService;
import org.example.flogin.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("4.1 Login Mock Testing - AccountController với Mocked Service")
class AccountControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    // a) Mock AccountService với @MockBean
    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    // ========================================
    // b) Test controller với mocked service
    // ========================================

    @Test
    @DisplayName("Mock Test - Login thành công với mocked service")
    void testLoginSuccess_WithMockedService() throws Exception {
        // Arrange: Setup mock data
        String username = "admin";
        String password = "admin123";
        
        AccountDTO mockAccount = new AccountDTO(
            1L, 
            username, 
            null,  // Password không được trả về
            LocalDateTime.now()
        );

        Map<String, String> credentials = Map.of(
            "username", username,
            "password", password
        );

        // Mock service behavior
        when(accountService.validateLogin(eq(username), eq(password)))
            .thenReturn(true);
        
        when(accountService.getAccountByUsername(eq(username)))
            .thenReturn(Optional.of(mockAccount));

        // Act & Assert: Perform request and verify response
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.createdDate").exists());

        // c) Verify mock interactions
        verify(accountService, times(1)).validateLogin(eq(username), eq(password));
        verify(accountService, times(1)).getAccountByUsername(eq(username));
        verifyNoMoreInteractions(accountService);
    }

    @Test
    @DisplayName("Mock Test - Login thất bại - sai mật khẩu")
    void testLoginFailure_WrongPassword_WithMockedService() throws Exception {
        // Arrange
        String username = "admin";
        String wrongPassword = "wrongpass";
        
        Map<String, String> credentials = Map.of(
            "username", username,
            "password", wrongPassword
        );

        // Mock service to return false for wrong password
        when(accountService.validateLogin(eq(username), eq(wrongPassword)))
            .thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Tên người dùng hoặc mật khẩu không đúng"));

        // c) Verify mock interactions
        verify(accountService, times(1)).validateLogin(eq(username), eq(wrongPassword));
        verify(accountService, never()).getAccountByUsername(anyString());
        verifyNoMoreInteractions(accountService);
    }

    @Test
    @DisplayName("Mock Test - Login thất bại - username không tồn tại")
    void testLoginFailure_UserNotFound_WithMockedService() throws Exception {
        // Arrange
        String username = "nonexistent";
        String password = "password123";
        
        Map<String, String> credentials = Map.of(
            "username", username,
            "password", password
        );

        // Mock service to return false for non-existent user
        when(accountService.validateLogin(eq(username), eq(password)))
            .thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Tên người dùng hoặc mật khẩu không đúng"));

        // c) Verify mock interactions
        verify(accountService, times(1)).validateLogin(eq(username), eq(password));
        verify(accountService, never()).getAccountByUsername(anyString());
    }

    @Test
    @DisplayName("Mock Test - Login thất bại - thiếu username")
    void testLoginFailure_MissingUsername_WithMockedService() throws Exception {
        // Arrange
        Map<String, String> credentials = Map.of(
            "username", "",
            "password", "password123"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Tên người dùng không được để trống"));

        // c) Verify no service interaction when validation fails early
        verify(accountService, never()).validateLogin(anyString(), anyString());
        verify(accountService, never()).getAccountByUsername(anyString());
    }

    @Test
    @DisplayName("Mock Test - Login thất bại - thiếu password")
    void testLoginFailure_MissingPassword_WithMockedService() throws Exception {
        // Arrange
        Map<String, String> credentials = Map.of(
            "username", "admin",
            "password", ""
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Mật khẩu không được để trống"));

        // c) Verify no service interaction when validation fails early
        verify(accountService, never()).validateLogin(anyString(), anyString());
        verify(accountService, never()).getAccountByUsername(anyString());
    }

    @Test
    @DisplayName("Mock Test - Verify multiple login attempts")
    void testMultipleLoginAttempts_WithMockedService() throws Exception {
        // Arrange
        String username = "testuser";
        String password = "test123";
        
        AccountDTO mockAccount = new AccountDTO(
            2L, 
            username, 
            null, 
            LocalDateTime.now()
        );

        Map<String, String> credentials = Map.of(
            "username", username,
            "password", password
        );

        // Mock service for successful login
        when(accountService.validateLogin(eq(username), eq(password)))
            .thenReturn(true);
        when(accountService.getAccountByUsername(eq(username)))
            .thenReturn(Optional.of(mockAccount));

        // Act: Perform 3 login attempts
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/accounts/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(credentials)))
                    .andExpect(status().isOk());
        }

        // c) Verify service was called exactly 3 times for each method
        verify(accountService, times(3)).validateLogin(eq(username), eq(password));
        verify(accountService, times(3)).getAccountByUsername(eq(username));
    }

    @Test
    @DisplayName("Mock Test - ArgumentCaptor example")
    void testLoginWithArgumentCaptor_WithMockedService() throws Exception {
        // Arrange
        String username = "captortest";
        String password = "pass123";
        
        AccountDTO mockAccount = new AccountDTO(
            3L, 
            username, 
            null, 
            LocalDateTime.now()
        );

        Map<String, String> credentials = Map.of(
            "username", username,
            "password", password
        );

        when(accountService.validateLogin(anyString(), anyString()))
            .thenReturn(true);
        when(accountService.getAccountByUsername(anyString()))
            .thenReturn(Optional.of(mockAccount));

        // Act
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk());

        // c) Verify with exact argument values
        verify(accountService).validateLogin(eq(username), eq(password));
        verify(accountService).getAccountByUsername(eq(username));
    }

    @Test
    @DisplayName("Mock Test - Service throws exception")
    void testLoginServiceException_WithMockedService() throws Exception {
        // Arrange
        String username = "erroruser";
        String password = "error123";
        
        Map<String, String> credentials = Map.of(
            "username", username,
            "password", password
        );

        // Mock service to throw exception
        when(accountService.validateLogin(eq(username), eq(password)))
            .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().is5xxServerError());

        // c) Verify service was called
        verify(accountService, times(1)).validateLogin(eq(username), eq(password));
    }
}