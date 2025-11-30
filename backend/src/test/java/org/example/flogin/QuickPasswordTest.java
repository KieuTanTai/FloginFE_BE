package org.example.flogin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class QuickPasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Hash đã lưu trong DB
        String hashFromDB = "$2a$10$Dskyoq6HmReLPO.oQzHW9Ou.ErYJdntI5R88qbcgGidELa77kSXHy";

        // Password bạn muốn kiểm tra
        String plainPassword = "admin123";

        // Kiểm tra
        boolean matches = encoder.matches(plainPassword, hashFromDB);
        System.out.println("Password match result: " + matches); // phải là true
    }
}
