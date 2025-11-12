package com.bazaarstores.utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiUtilities {

    public static RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + getToken())
                .build();
    }

    private static String getToken() {
        Map payload = new HashMap();
        payload.put("email", ConfigReader.getAdminEmail());
        payload.put("password", ConfigReader.getDefaultPassword());
        Response response = given()
                .body(payload)
                .contentType(ContentType.JSON)
                .post(ConfigReader.getApiBaseUrl() + "/login");
        return response.jsonPath().getString("authorisation.token");
    }

    public static List<String> getStoreList() {
        Response response = given(spec())
                .when()
                .get("/stores")
                .then()
                .statusCode(200)
                .extract().response();

        return response.jsonPath().getList("data.name");
    }


}
