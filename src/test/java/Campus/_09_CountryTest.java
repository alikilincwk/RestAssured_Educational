package Campus;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _09_CountryTest {
    RequestSpecification reqSpec;
    Faker randomUretici = new Faker();
    String countryId = "";
    String rndName="";
    String rndCode="";

    @BeforeClass
    public void Setup() {
        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().detailedCookies();
        System.out.println("cookies = " + cookies);

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void CreateCountry() {

         rndName = randomUretici.country().name()+randomUretici.number().digits(5);
         rndCode = randomUretici.country().countryCode2()+randomUretici.number().digits(5);
        Map<String, String> newCountry = new HashMap<>();

        newCountry.put("name",  rndName);
        newCountry.put("code",  rndCode);

        countryId =
                given()
                        .spec(reqSpec)
                        .body(newCountry)

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("countryId = " + countryId);

    }
    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative(){

        Map<String, String> newCountry = new HashMap<>();

        newCountry.put("name",rndName);
        newCountry.put("code",rndCode);

                given()
                        .spec(reqSpec)
                        .body(newCountry)

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",containsString("already exists"))
                ;
    }

    @Test(dependsOnMethods = "CreateCountry")
    public void UpdateCountry(){
        Map<String, String> updatedCountry = new HashMap<>();
        updatedCountry.put("id", countryId);
        updatedCountry.put("name", "Updated " + rndName);
        updatedCountry.put("code", "Updated " + rndCode);
        given()
                .spec(reqSpec)
                .body(updatedCountry)
                .when()
                .put("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(countryId))
                .body("name", equalTo("Updated " + rndName))
                .body("code", equalTo("Updated " + rndCode))
        ;
    }

    @Test(dependsOnMethods = "UpdateCountry")
    public void DeleteCountry() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/countries/" + countryId)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "DeleteCountry")
    public void DeleteCountryNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/countries/" + countryId)
                .then()
                .log().body()
                .statusCode(400)
                .body("message",containsStringIgnoringCase("Country not found"))
        ;
    }

}
