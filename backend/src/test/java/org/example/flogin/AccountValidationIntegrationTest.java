package org.example.flogin;
//6.1.2 b/
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Account validation integration tests")
public class AccountValidationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create account - username empty returns 400 with message")
    void testCreateAccountUsernameEmpty() {
        System.out.println("\n========== Test: Create account with empty username ==========");
        Map<String, Object> payload = Map.of(
                "username", "",
                "password", "password123"
        );

        System.out.println("Sending POST request to /api/accounts with empty username...");
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts",
        payload, String.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Username cannot be empty"));
        System.out.println("✓ Test PASSED: Empty username validation works correctly");
    }

    @Test
    @DisplayName("Create account - password too short returns 400 with message")
    void testCreateAccountPasswordShort() {
        System.out.println("\n========== Test: Create account with short password ==========");
        Map<String, Object> payload = Map.of(
                "username", "valid_user",
                "password", "123"
        );

        System.out.println("Sending POST request to /api/accounts with password length 3...");
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts", payload,
         String.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Password must have at least 6 characters"));
        System.out.println("✓ Test PASSED: Short password validation works correctly");
    }

    @Test
    @DisplayName("Login - username empty returns 400 with message")
    void testLoginUsernameEmpty() {
        System.out.println("\n========== Test: Login with empty username ==========");
        Map<String, String> payload = Map.of(
                "username", "",
                "password", "whatever"
        );

        System.out.println("Sending POST request to /api/accounts/login with empty username...");
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts/login",
         payload, String.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Username cannot be empty"));
        System.out.println("✓ Test PASSED: Empty username validation works correctly in login");
    }

    @Test
    @DisplayName("Login - password empty returns 400 with message")
    void testLoginPasswordEmpty() {
        System.out.println("\n========== Test: Login with empty password ==========");
        Map<String, String> payload = Map.of(
                "username", "someuser",
                "password", ""
        );
        
        System.out.println("Sending POST request to /api/accounts/login with empty password...");
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts/login",
         payload, String.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Password cannot be empty"));
        System.out.println("✓ Test PASSED: Empty password validation works correctly in login");
        System.out.println("\n========== All tests completed successfully! ==========\n");
    }
}
