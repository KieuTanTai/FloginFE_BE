package org.example.flogin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.flogin.dto.AccountDTO;
import org.example.flogin.entity.Account;
import org.example.flogin.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

@Service
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create new account
    public AccountDTO createAccount(AccountDTO accountDTO) {
        // Business validation
        validateAccountBusinessRules(accountDTO);

        if (accountRepository.existsByUsername(accountDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setCreatedDate(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);
        logger.info("Created account username={}", savedAccount.getUsername());
        return convertToDTO(savedAccount);
    }

    // Business validation rules for Account
    private void validateAccountBusinessRules(AccountDTO accountDTO) {
        // Username validation (3-50 characters, not empty)
        if (accountDTO.getUsername() == null || accountDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (accountDTO.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username must have at least 3 characters");
        }
        if (accountDTO.getUsername().length() > 50) {
            throw new IllegalArgumentException("Username cannot exceed 50 characters");
        }

        // Password validation (min 6 characters)
        if (accountDTO.getPassword() == null || accountDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (accountDTO.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must have at least 6 characters");
        }
    }

    // Get all accounts
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get account by ID
    public Optional<AccountDTO> getAccountById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return accountRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Get account by username
    public Optional<AccountDTO> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    // Update account (only password)
    public AccountDTO updateAccount(@NonNull Long id, AccountDTO accountDTO) {
        if (id == null) {
            throw new IllegalArgumentException("Account id must not be null");
        }

        // Business validation
        validateAccountBusinessRules(accountDTO);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (accountDTO.getPassword() != null && !accountDTO.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            logger.info("Updated password for account id={} username={}", id, account.getUsername());
        }
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }

        Account updatedAccount = accountRepository.save(account);
        return convertToDTO(updatedAccount);
    }

    // Delete account (hard delete for accounts)
    public void deleteAccount(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Account id must not be null");
        if (!accountRepository.existsById(id))
            throw new RuntimeException("Account not found");
        accountRepository.deleteById(id);
    }

    // Login validation
    public boolean validateLogin(String username, String password) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            return passwordEncoder.matches(password, account.getPassword());        
        }        
        return false;
    }

    // Convert Entity to DTO
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setUsername(account.getUsername());
        // Don't send password in DTO
        dto.setCreatedDate(account.getCreatedDate());
        return dto;
    }
}
