package org.example.flogin.dto;

public class LoginResponseDTO {

    private boolean success;
    private String message;
    private String token;
    private AccountDTO user;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message, String token, AccountDTO user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountDTO getUser() {
        return user;
    }

    public void setUser(AccountDTO user) {
        this.user = user;
    }
}
