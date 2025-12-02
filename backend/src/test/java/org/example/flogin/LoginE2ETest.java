package org.example.flogin;
// 6.1.2 a/
import java.util.Map;

import org.example.flogin.dto.AccountDTO;
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
@DisplayName("Login End-to-End Test")
public class LoginE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Complete login flow: create account -> login success -> login fail")
    void testCompleteLoginFlow() {
                String username = "e2e_user_" + System.currentTimeMillis();
                String password = "Password123";

                boolean createOk = false;
                boolean loginOk = false;
                boolean badLoginOk = false;
                StringBuilder summary = new StringBuilder();

                try {
                        // 1) Create account
                        Map<String, Object> createPayload = Map.of(
                                        "username", username,
                                        "password", password
                        );

                        ResponseEntity<AccountDTO> createResp = restTemplate.postForEntity("/api/accounts", createPayload, AccountDTO.class);
                        AccountDTO created = createResp.getBody();
                        summary.append("[Step 1] Create account -> status=").append(createResp.getStatusCode()).append("\n");
                        if (createResp.getStatusCode() == HttpStatus.OK && created != null && username.equals(created.getUsername())) {
                                createOk = true;
                                summary.append("  -> Created id=").append(created.getId()).append(" username=").append(created.getUsername()).append("\n");
                        } else {
                                summary.append("  -> Creation response body or status unexpected: ").append(created).append("\n");
                        }

                        // 2) Login with correct credentials
                        Map<String, String> loginPayload = Map.of(
                                        "username", username,
                                        "password", password
                        );
                        ResponseEntity<AccountDTO> loginResp = restTemplate.postForEntity("/api/accounts/login", loginPayload, AccountDTO.class);
                        AccountDTO logged = loginResp.getBody();
                        summary.append("[Step 2] Login correct creds -> status=").append(loginResp.getStatusCode()).append("\n");
                        if (loginResp.getStatusCode() == HttpStatus.OK && logged != null && username.equals(logged.getUsername())) {
                                loginOk = true;
                                summary.append("  -> Logged id=").append(logged.getId()).append(" username=").append(logged.getUsername()).append("\n");
                        } else {
                                summary.append("  -> Login response unexpected: body=").append(logged).append("\n");
                        }

                        // 3) Login with incorrect password -> should be 401 Unauthorized
                        Map<String, String> badLogin = Map.of(
                                        "username", username,
                                        "password", "wrong-password"
                        );
                        HttpEntity<Map<String, String>> badReq = new HttpEntity<>(badLogin);
                        ResponseEntity<String> badResp = restTemplate.postForEntity("/api/accounts/login", badReq, String.class);
                        summary.append("[Step 3] Login wrong password -> status=").append(badResp.getStatusCode()).append(" body=").append(badResp.getBody()).append("\n");
                        if (badResp.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                                badLoginOk = true;
                        }

                        // Print interim messages so CI logs show progress
                        System.out.println(summary.toString());

                        // Assertions (keeps test framework semantics)
                        assertTrue(createOk, "Account creation should succeed and return created username");
                        assertTrue(loginOk, "Login with correct credentials should succeed");
                        assertTrue(badLoginOk, "Login with wrong password should return UNAUTHORIZED");

                } finally {
                        // Always print a clear summary at the end of the test, even on failures
                        String finalSummary = new StringBuilder()
                                        .append("===== LoginE2ETest Summary =====\n")
                                        .append("Username: ").append(username).append("\n")
                                        .append("Create: ").append(createOk ? "OK" : "FAILED").append("\n")
                                        .append("Login(correct): ").append(loginOk ? "OK" : "FAILED").append("\n")
                                        .append("Login(wrong): ").append(badLoginOk ? "OK" : "FAILED").append("\n")
                                        .append("================================\n").toString();
                        System.out.println(finalSummary);
                }
    }
}
