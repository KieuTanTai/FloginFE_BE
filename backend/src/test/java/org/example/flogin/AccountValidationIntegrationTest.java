package org.example.flogin;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Account validation integration tests")
public class AccountValidationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Create account - username empty returns 400 with message")
    void testCreateAccountUsernameEmpty() {
        Map<String, Object> payload = Map.of(
                "username", "",
                "password", "password123"
        );

        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts", payload, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Tên người dùng không được để trống"));
    }

    @Test
    @DisplayName("Create account - password too short returns 400 with message")
    void testCreateAccountPasswordShort() {
        Map<String, Object> payload = Map.of(
                "username", "valid_user",
                "password", "123"
        );

        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts", payload, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Mật khẩu phải có ít nhất 6 ký tự"));
    }

    @Test
    @DisplayName("Login - username empty returns 400 with message")
    void testLoginUsernameEmpty() {
        Map<String, String> payload = Map.of(
                "username", "",
                "password", "whatever"
        );

        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts/login", payload, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Tên người dùng không được để trống"));
    }

    @Test
    @DisplayName("Login - password empty returns 400 with message")
    void testLoginPasswordEmpty() {
        Map<String, String> payload = Map.of(
                "username", "someuser",
                "password", ""
        );

        ResponseEntity<String> resp = restTemplate.postForEntity("/api/accounts/login", payload, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().contains("Mật khẩu không được để trống"));
    }
}
