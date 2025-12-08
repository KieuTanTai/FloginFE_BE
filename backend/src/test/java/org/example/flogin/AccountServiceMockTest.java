package org.example.flogin;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.example.flogin.service.AccountService;
import org.example.flogin.repository.AccountRepository;
import org.example.flogin.entity.Account;
import org.example.flogin.dto.AccountDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AccountServiceIntegrationTest {

    @MockBean
    private AccountService accountService; // mock injected into context

    @Test
    void someFlow_usesMockedAccountService() {
        Account a = new Account("alice", "password123");
        a.setId(2L);
        AccountDTO dto = new AccountDTO(2L, "alice", "password123", java.time.LocalDateTime.now());
        when(accountService.getAccountById(2L)).thenReturn(java.util.Optional.of(dto));

        // Test that the mock works correctly
        assertThat(accountService.getAccountById(2L)).isPresent();
        verify(accountService).getAccountById(2L);
    }
}
