package org.example.flogin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        // Password to hash
        String password = "password123";

        // Create BCryptPasswordEncoder instance
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Generate hash
        String hashedPassword = passwordEncoder.encode(password);

        // Print the hashed password
        System.out.println("Hashed password: " + hashedPassword);
    }
}