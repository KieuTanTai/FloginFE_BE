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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Account success/error flow integration tests")
public class AccountSuccessErrorIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create account success then duplicate returns error")
    void testCreateSuccessThenDuplicateError() {
        String username = "flow_user_" + System.currentTimeMillis();
        String password = "Password123";

        Map<String, Object> payload = Map.of(
                "username", username,
                "password", password
        );

        // First create should succeed
        ResponseEntity<AccountDTO> createResp = restTemplate.postForEntity("/api/accounts", payload, AccountDTO.class);
        assertEquals(HttpStatus.OK, createResp.getStatusCode());
        assertNotNull(createResp.getBody());
        assertEquals(username, createResp.getBody().getUsername());
        assertNotNull(createResp.getBody().getId());

        // Second create (same username) should return 400 with a message indicating existence
        ResponseEntity<String> dupResp = restTemplate.postForEntity("/api/accounts", payload, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, dupResp.getStatusCode());
        assertTrue(dupResp.getBody().contains("đã tồn tại") || dupResp.getBody().toLowerCase().contains("exists"));
    }

    @Test
    @DisplayName("Login success and login wrong password flows")
    void testLoginSuccessAndWrongPassword() {
        String username = "flow_login_" + System.currentTimeMillis();
        String password = "Password123";

        Map<String, Object> createPayload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<AccountDTO> createResp = restTemplate.postForEntity("/api/accounts", createPayload, AccountDTO.class);
        assertEquals(HttpStatus.OK, createResp.getStatusCode());

        // Login with correct credentials
        Map<String, String> loginPayload = Map.of(
                "username", username,
                "password", password
        );

        ResponseEntity<AccountDTO> loginResp = restTemplate.postForEntity("/api/accounts/login", loginPayload, AccountDTO.class);
        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        assertNotNull(loginResp.getBody());
        assertEquals(username, loginResp.getBody().getUsername());

        // Login with wrong password -> 401
        Map<String, String> badLogin = Map.of(
                "username", username,
                "password", "wrong-pass"
        );
        HttpEntity<Map<String, String>> badReq = new HttpEntity<>(badLogin);
        ResponseEntity<String> badResp = restTemplate.postForEntity("/api/accounts/login", badReq, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, badResp.getStatusCode());
        assertTrue(badResp.getBody().contains("Tên người dùng hoặc mật khẩu không đúng") || badResp.getBody().toLowerCase().contains("wrong"));
    }
}
