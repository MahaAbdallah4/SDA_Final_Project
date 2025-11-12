package com.bazaarstores.utilities;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class TokenGenerator {

    private static String token;

    public static String generateToken() {
        if (token == null) {
            Response response = given()
                    .contentType("application/json")
                    .body("{\"email\": \"" + ConfigReader.getAdminEmail() + "\", " +
                            "\"password\": \"" + ConfigReader.getDefaultPassword() + "\"}")
                    .when()
                    .post(ConfigReader.getApiBaseUrl() + "/login");

            token = response.jsonPath().getString("authorisation.token");
            System.out.println("Generated Token: " + token);
        }
        return token;
    }

    public static void clearToken() {
        token = null;
    }
}
