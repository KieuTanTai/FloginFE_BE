package org.example.flogin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashChecker {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=".repeat(60));
        System.out.println("PASSWORD HASH CHECKER");
        System.out.println("=".repeat(60));

        // Password to check
        String plainPassword = "admin123";

        // Hash from your SQL file
        String hashFromSQL = "$2a$10$Dskyoq6HmReLPO.oQzHW9Ou.ErYJdntI5R88qbcgGidELa77kSXHy";

        System.out.println("\n📝 Testing Password: " + plainPassword);
        System.out.println("\n🔐 Hash from SQL file:");
        System.out.println(hashFromSQL);

        // Check if password matches the hash
        boolean matches = encoder.matches(plainPassword, hashFromSQL);
        System.out.println("\n✅ Password Match Result: " + (matches ? "✓ CORRECT" : "✗ INCORRECT"));

        if (!matches) {
            System.out.println("\n⚠️  WARNING: Password does not match the hash in SQL file!");
            System.out.println("Generating new hash for password: " + plainPassword);
            String newHash = encoder.encode(plainPassword);
            System.out.println("\n🔑 New BCrypt hash:");
            System.out.println(newHash);
            System.out.println("\n💡 Copy this hash to your SQL file if needed.");
        } else {
            System.out.println("\n✓ Hash in SQL file is correct for password: " + plainPassword);
        }

        System.out.println("\n🔧 Quick test with the same password and hash:");
        boolean quickTest = encoder.matches("admin123", hashFromSQL);
        System.out.println("Quick test result: " + quickTest + " (should be true)");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("DONE!");
        System.out.println("=".repeat(60));
    }
}
