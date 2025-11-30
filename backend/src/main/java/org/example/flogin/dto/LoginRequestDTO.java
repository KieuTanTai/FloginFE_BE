package org.example.flogin.dto;

public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO(String Username,String Password) {
        this.username=Username;
        this.password=Password;
    }


    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }
    
}
