package org.example.flogin;
// 6.1.2 a/
import java.util.Map;

import org.example.flogin.dto.AccountDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("Login End-to-End integration tests")
public class LoginE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String testUsername;
    private String testPassword = "Password123";

    @BeforeEach
    void setUp() {
        // Generate unique username for each test to avoid duplicates
        testUsername = "e2e_user_" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("Create account for E2E login flow")
    void testCreateAccountForE2E() {
        System.out.println("\n========== Test: Create account for E2E login flow ==========");
        Map<String, Object> payload = Map.of(
                "username", testUsername,
                "password", testPassword
        );

        System.out.println("Sending POST request to /api/accounts with username: " + testUsername);
        ResponseEntity<AccountDTO> resp = restTemplate.postForEntity("/api/accounts", payload, AccountDTO.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(testUsername, resp.getBody().getUsername());
        System.out.println("✓ Test PASSED: Account created successfully with id=" + resp.getBody().getId());
    }

        
    @Test
    @DisplayName("Login with correct credentials returns 200")
    void testLoginWithCorrectCredentials() {
        System.out.println("\n========== Test: Login with correct credentials ==========");
        
        // First ensure account exists
        Map<String, Object> createPayload = Map.of(
                "username", testUsername,
                "password", testPassword
        );
        restTemplate.postForEntity("/api/accounts", createPayload, AccountDTO.class);

        Map<String, String> payload = Map.of(
                "username", testUsername,
                "password", testPassword
        );

        System.out.println("Sending POST request to /api/accounts/login with correct credentials...");
        ResponseEntity<AccountDTO> resp = restTemplate.postForEntity("/api/accounts/login", payload, AccountDTO.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(testUsername, resp.getBody().getUsername());
        System.out.println("✓ Test PASSED: Login with correct credentials works");
    }

    @Test
    @DisplayName("Login with wrong password returns 401")
    void testLoginWithWrongPassword() {
        System.out.println("\n========== Test: Login with wrong password ==========");
        
        // First ensure account exists
        Map<String, Object> createPayload = Map.of(
                "username", testUsername,
                "password", testPassword
        );
        restTemplate.postForEntity("/api/accounts", createPayload, AccountDTO.class);

        Map<String, String> payload = Map.of(
                "username", testUsername,
                "password", "wrong-password"
        );

        System.out.println("Sending POST request to /api/accounts/login with wrong password...");
        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts/login", payload, String.class);
        
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Invalid username or password"));
        System.out.println("✓ Test PASSED: Wrong password correctly returns 401 with error message");
        System.out.println("\n========== All E2E tests completed successfully! ==========\n");
    }
}
