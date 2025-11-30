package org.api;

import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class LoginApiTest {

    @Test
    void testLoginSuccess() {
        RestAssured.baseURI = "http://localhost:8080/api/accounts";

        given()
            .contentType(ContentType.JSON)
            .body("{\"username\":\"admin\", \"password\":\"admin123\"}")
        .when()
            .post("/login")
        .then()
            .statusCode(200);
    }

    @Test
    void testLoginFailWrongPassword() {
        RestAssured.baseURI = "http://localhost:8080/api/accounts";

        given()
            .contentType(ContentType.JSON)
            .body("{\"username\":\"admin\", \"password\":\"wrong\"}")
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body("error", equalTo("Tên người dùng hoặc mật khẩu không đúng"));
    }
}
