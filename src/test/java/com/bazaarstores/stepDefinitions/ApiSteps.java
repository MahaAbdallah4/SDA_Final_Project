package com.bazaarstores.stepDefinitions;


import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static com.bazaarstores.stepDefinitions.RegistrationSteps.email;
import static com.bazaarstores.stepDefinitions.RegistrationSteps.fullName;
import static com.bazaarstores.utilities.ApiUtilities.spec;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ApiSteps {

    Response response;
    JsonPath jsonPath;

    @When("assert the registration via API")
    public void assertTheRegistrationViaAPI() {
        response = given(spec()).get("/users");
        response.prettyPrint();
        jsonPath = response.jsonPath();
        String actualName = jsonPath.getString("find{it.email=='" + email + "'}.name");
        String actualEmail = jsonPath.getString("find{it.email=='" + email + "'}.email");
        assertEquals(email, actualEmail);
        assertEquals(fullName, actualName);
    }


    @And("assert the negative registration via API using email {string}")
    public void assertTheNegativeRegistrationViaAPIUsingEmail(String email) {
        response = given(spec()).get("/users");
        response.prettyPrint();
        jsonPath = response.jsonPath();
        assertNull(jsonPath.getString("find{it.email=='" + email + "'}.name"));
        assertNull(jsonPath.getString("find{it.email=='" + email + "'}.email"));
    }

    @And("assert the negative registration invalid name via API using name {string}")
    public void assertTheNegativeRegistrationInvalidNameViaAPIUsingName(String name) {
        response = given(spec()).get( "/users");
        response.prettyPrint();
        jsonPath = response.jsonPath();
        String actualName =  jsonPath.getString("find{it.name=='"+ name +"'}.name");
        assertEquals(fullName, actualName);
//        System.out.println("Full Name: " + fullName);
//        System.out.println("Actual Name: " + actualName);

        //This is a bug!, Should not accept invalid name
        assert false;
    }
}
