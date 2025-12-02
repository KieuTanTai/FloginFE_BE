package org.example.flogin;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.example.flogin.service.AccountService;
import org.example.flogin.repository.AccountRepository;
import org.example.flogin.model.Account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AccountServiceIntegrationTest {

    @Autowired
    private SomeOtherComponent thatUsesAccountService; // component under test

    @MockBean
    private AccountService accountService; // mock injected into context

    @Test
    void someFlow_usesMockedAccountService() {
        Account a = new Account(2L, "alice@example.com", "Alice");
        when(accountService.findById(2L)).thenReturn(java.util.Optional.of(a));

        // gọi method trên component thực tế, sẽ dùng mock accountService bên trong
        var result = thatUsesAccountService.doSomethingWithAccount(2L);

        assertThat(result).isEqualTo("expected");
        verify(accountService).findById(2L);
    }
}
