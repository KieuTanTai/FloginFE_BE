package org.example.flogin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Standalone utility to check BCrypt password hashes
 * Run this file independently to verify password hashes in database
 */
public class PasswordHashChecker {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=".repeat(60));
        System.out.println("PASSWORD HASH CHECKER");
        System.out.println("=".repeat(60));

        // Password to check
        String plainPassword = "admin123";

        // Hash from your SQL file
        String hashFromSQL = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";

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

        System.out.println("\n" + "=".repeat(60));

        // Test additional passwords if needed
        System.out.println("\n🔧 Additional Password Tests:");
        System.out.println("-".repeat(60));

        String[] testPasswords = { "admin123", "password123", "test123" };

        for (String testPwd : testPasswords) {
            String testHash = encoder.encode(testPwd);
            System.out.println("\nPassword: " + testPwd);
            System.out.println("Hash:     " + testHash);
            System.out.println("Matches:  " + encoder.matches(testPwd, testHash) + " ✓");
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("DONE!");
        System.out.println("=".repeat(60));
    }
}
