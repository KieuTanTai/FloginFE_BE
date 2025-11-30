package org.example.flogin;

import org.example.flogin.dto.AccountDTO;
import org.example.flogin.entity.Account;
import org.example.flogin.repository.AccountRepository;
import org.example.flogin.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("AccountService Unit Test")
class AccountServiceUnitTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("validateLogin - success")
    void testValidateLoginSuccess() {
        Account acc = new Account("john_doe", "encoded_password123");
        acc.setId(1L);
        when(accountRepository.findByUsername("john_doe")).thenReturn(Optional.of(acc));
        when(passwordEncoder.matches("password123", "encoded_password123")).thenReturn(true);
        assertTrue(accountService.validateLogin("john_doe", "password123"));
    }

    @Test
    @DisplayName("validateLogin - username not found")
    void testValidateLoginUsernameNotFound() {
        when(accountRepository.findByUsername("admin")).thenReturn(Optional.empty());
        assertFalse(accountService.validateLogin("admin", "password123"));
    }

    @Test
    @DisplayName("validateLogin - wrong password")
    void testValidateLoginWrongPassword() {
        Account acc = new Account("john_doe", "encoded_password123");
        acc.setId(1L);
        when(accountRepository.findByUsername("john_doe")).thenReturn(Optional.of(acc));
        when(passwordEncoder.matches("password1234121", "password1234121")).thenReturn(false);
        assertFalse(accountService.validateLogin("john_doe", "password1234121"));
    }

    @Test
    @DisplayName("createAccount - validation error: username empty")
    void testCreateAccountValidationErrorUsernameEmpty() {
        AccountDTO dto = new AccountDTO(null, "", "password123", LocalDateTime.now());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto));
        assertTrue(ex.getMessage().contains("Tên người dùng không được để trống"));
    }

    @Test
    @DisplayName("createAccount - validation error: username too short")
    void testCreateAccountValidationErrorUsernameShort() {
        AccountDTO dto = new AccountDTO(null, "ab", "password123", LocalDateTime.now());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto));
        assertTrue(ex.getMessage().contains("ít nhất 3 ký tự"));
    }

    @Test
    @DisplayName("createAccount - validation error: password empty")
    void testCreateAccountValidationErrorPasswordEmpty() {
        AccountDTO dto = new AccountDTO(null, "john_doe", "", LocalDateTime.now());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto));
        assertTrue(ex.getMessage().contains("Mật khẩu không được để trống"));
    }

    @Test
    @DisplayName("createAccount - validation error: password too short")
    void testCreateAccountValidationErrorPasswordShort() {
        AccountDTO dto = new AccountDTO(null, "john_doe", "123", LocalDateTime.now());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(dto));
        assertTrue(ex.getMessage().contains("ít nhất 6 ký tự"));
    }

    @Test
    @DisplayName("createAccount - username exists")
    void testCreateAccountUsernameExists() {
        AccountDTO dto = new AccountDTO(null, "john_doe", "password123", LocalDateTime.now());
        when(accountRepository.existsByUsername("john_doe")).thenReturn(true);
        Exception ex = assertThrows(RuntimeException.class, () -> accountService.createAccount(dto));
        assertTrue(ex.getMessage().contains("đã tồn tại"));
    }

    @Test
    @DisplayName("createAccount - success")
    void testCreateAccountSuccess() {
        AccountDTO dto = new AccountDTO(null, "john_doe", "password123", LocalDateTime.now());
        when(accountRepository.existsByUsername("john_doe")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password123");
        Account saved = new Account("john_doe", "encoded_password123");
        saved.setId(1L);
        when(accountRepository.save(any(Account.class))).thenReturn(saved);
        AccountDTO result = accountService.createAccount(dto);
        assertEquals("john_doe", result.getUsername());
        assertEquals(1L, result.getId());
    }
}
