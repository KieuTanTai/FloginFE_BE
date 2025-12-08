package org.example.flogin;
//6.1.2 c/
import java.util.Map;

import org.example.flogin.dto.AccountDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Account success/error flow integration tests")
public class AccountSuccessErrorIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create account success then duplicate returns error")
    void testCreateSuccessThenDuplicateError() {
        System.out.println("\n========== Test: Create account success then duplicate error ==========");
        String username = "flow_user_" + System.currentTimeMillis();
        String password = "Password123";

        Map<String, Object> payload = Map.of(
                "username", username,
                "password", password
        );

        // First create should succeed
        System.out.println("Step 1: Creating account with username: " + username);
        ResponseEntity<AccountDTO> createResp = restTemplate.postForEntity("/api/accounts"
        , payload, AccountDTO.class);
        System.out.println("  Response Status: " + createResp.getStatusCode());
        System.out.println("  Response Body ID: " + createResp.getBody().getId());
        System.out.println("  Response Body Username: " + createResp.getBody().getUsername());
        
        assertEquals(HttpStatus.OK, createResp.getStatusCode());
        assertNotNull(createResp.getBody());
        assertEquals(username, createResp.getBody().getUsername());
        assertNotNull(createResp.getBody().getId());
        System.out.println("  ✓ First account creation successful");

        // Second create (same username) should return 400 with a message indicating existence
        System.out.println("\nStep 2: Attempting to create duplicate account with same username");
        ResponseEntity<String> dupResp = restTemplate.postForEntity("/api/accounts",
         payload, String.class);
        System.out.println("  Response Status: " + dupResp.getStatusCode());
        System.out.println("  Response Body: " + dupResp.getBody());
        
        assertEquals(HttpStatus.BAD_REQUEST, dupResp.getStatusCode());
        assertTrue(dupResp.getBody().contains("already exists") || dupResp.getBody().toLowerCase().contains("exists"));
        System.out.println("  ✓ Duplicate account creation correctly rejected");
        System.out.println("\n========== Test PASSED ==========\n");
    }

    @Test
    @DisplayName("Login success and login wrong password flows")
    void testLoginSuccessAndWrongPassword() {
        System.out.println("\n========== Test: Login success and wrong password flows ==========");
        String username = "flow_login_" + System.currentTimeMillis();
        String password = "Password123";

        Map<String, Object> createPayload = Map.of(
                "username", username,
                "password", password
        );

        System.out.println("Step 1: Creating account with username: " + username);
        ResponseEntity<AccountDTO> createResp = restTemplate.postForEntity("/api/accounts",
         createPayload, AccountDTO.class);
        System.out.println("  Response Status: " + createResp.getStatusCode());
        assertEquals(HttpStatus.OK, createResp.getStatusCode());
        System.out.println("  ✓ Account created successfully");

        // Login with correct credentials
        System.out.println("\nStep 2: Logging in with correct credentials");
        Map<String, String> loginPayload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<AccountDTO> loginResp = restTemplate.postForEntity("/api/accounts/login",
         loginPayload, AccountDTO.class);
        System.out.println("  Response Status: " + loginResp.getStatusCode());
        System.out.println("  Response Body Username: " + loginResp.getBody().getUsername());
        
        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        assertNotNull(loginResp.getBody());
        assertEquals(username, loginResp.getBody().getUsername());
        System.out.println("  ✓ Login with correct credentials successful");

        // Login with wrong password -> 401
        System.out.println("\nStep 3: Attempting login with wrong password");
        Map<String, String> badLogin = Map.of(
                "username", username,
                "password", "wrong-pass"
        );
        HttpEntity<Map<String, String>> badReq = new HttpEntity<>(badLogin);
        ResponseEntity<String> badResp = restTemplate.postForEntity("/api/accounts/login", 
        badReq, String.class);
        System.out.println("  Response Status: " + badResp.getStatusCode());
        System.out.println("  Response Body: " + badResp.getBody());
        
        assertEquals(HttpStatus.UNAUTHORIZED, badResp.getStatusCode());
        assertTrue(badResp.getBody().contains("Invalid username or password") ||
         badResp.getBody().toLowerCase().contains("invalid"));
        System.out.println("  ✓ Login with wrong password correctly rejected");
        System.out.println("\n========== Test PASSED ==========\n");
    }
}
