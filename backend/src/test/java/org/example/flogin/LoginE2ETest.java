package org.example.flogin;

import java.util.Map;

import org.example.flogin.dto.AccountDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Login End-to-End Test")
public class LoginE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Complete login flow: create account -> login success -> login fail")
    void testCompleteLoginFlow() {
        String username = "e2e_user_" + System.currentTimeMillis();
        String password = "Password123";

        // 1) Create account
        Map<String, Object> createPayload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<AccountDTO> createResp = restTemplate.postForEntity("/api/accounts", createPayload, AccountDTO.class);
        assertEquals(HttpStatus.OK, createResp.getStatusCode(), "Account creation should return 200 OK");
        assertNotNull(createResp.getBody(), "Created account body should not be null");
        assertEquals(username, createResp.getBody().getUsername(), "Created username should match");

        // 2) Login with correct credentials
        Map<String, String> loginPayload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<AccountDTO> loginResp = restTemplate.postForEntity("/api/accounts/login", loginPayload, AccountDTO.class);
        // Print specific DTO fields (id, username) for easier debugging in CI logs
        AccountDTO logged = loginResp.getBody();
        System.out.println("[LoginE2ETest] loginResp.status=" + loginResp.getStatusCode() + " id=" + (logged != null ? logged.getId() : null) + " username=" + (logged != null ? logged.getUsername() : null));
        assertEquals(HttpStatus.OK, loginResp.getStatusCode(), "Login should succeed with correct credentials");
        assertNotNull(loginResp.getBody(), "Login response body should not be null");
        assertEquals(username, loginResp.getBody().getUsername(), "Logged-in username should match");

        // 3) Login with incorrect password -> should be 401 Unauthorized
        Map<String, String> badLogin = Map.of(
                "username", username,
                "password", "wrong-password"
        );

        HttpEntity<Map<String, String>> badReq = new HttpEntity<>(badLogin);
        ResponseEntity<String> badResp = restTemplate.postForEntity("/api/accounts/login", badReq, String.class);
        // Print the failed login response (status + body) so it's visible in test output
        System.out.println("[LoginE2ETest] badLoginResp.status=" + badResp.getStatusCode() + " body=" + badResp.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, badResp.getStatusCode(), "Login with wrong password should be UNAUTHORIZED");
    }
}
