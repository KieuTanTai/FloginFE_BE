package org.example.flogin.dto;

import java.time.LocalDateTime;

public class AccountDTO {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createdDate;

    // Constructors
    public AccountDTO() {
    }

    public AccountDTO(Long id, String username, String password, LocalDateTime createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
