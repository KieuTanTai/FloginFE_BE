package org.example.flogin.controller;

import org.example.flogin.dto.AccountDTO;
import org.example.flogin.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend origin
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Validate account input
    private void validateAccountInput(AccountDTO accountDTO) {
        // Validate username (not empty, 3-50 characters)
        if (accountDTO.getUsername() == null || accountDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống");
        }
        if (accountDTO.getUsername().length() < 3) {
            throw new IllegalArgumentException("Tên người dùng phải có ít nhất 3 ký tự");
        }
        if (accountDTO.getUsername().length() > 50) {
            throw new IllegalArgumentException("Tên người dùng không được vượt quá 50 ký tự");
        }

        // Validate password (not empty, min 6 characters)
        if (accountDTO.getPassword() == null || accountDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        if (accountDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) {
        try {
            validateAccountInput(accountDTO);
            AccountDTO created = accountService.createAccount(accountDTO);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        try {
            validateAccountInput(accountDTO);
            AccountDTO updated = accountService.updateAccount(id, accountDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Validate login input
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tên người dùng không được để trống"));
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mật khẩu không được để trống"));
        }

        boolean isValid = accountService.validateLogin(username, password);

        if (isValid) {
            return accountService.getAccountByUsername(username)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Tên người dùng hoặc mật khẩu không đúng"));
        }
    }
}
