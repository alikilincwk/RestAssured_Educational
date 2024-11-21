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

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _10_CitizenshipTest {

    RequestSpecification reqSpec;
    Faker randomUretici = new Faker();
    String ctznshpId = "";
    String rndName = "";
    String rndCode = "";

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
    public void CreateCitizenship() {

        rndName = randomUretici.nation().nationality() + randomUretici.number().digits(5);
        rndCode = randomUretici.nation().flag() + randomUretici.number().digits(5);
        Map<String, String> newCitizenship = new HashMap<>();

        newCitizenship.put("name", rndName);
        newCitizenship.put("shortName", rndCode);

        ctznshpId =
                given()
                        .spec(reqSpec)
                        .body(newCitizenship)

                        .when()
                        .post("/school-service/api/citizenships")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("countryId = " + ctznshpId);

    }

    @Test(dependsOnMethods = "CreateCitizenship")
    public void CreateCitizenshipNegative() {

        Map<String, String> newCitizenship = new HashMap<>();

        newCitizenship.put("name", rndName);
        newCitizenship.put("shortName", rndCode);

        given()
                .spec(reqSpec)
                .body(newCitizenship)

                .when()
                .post("/school-service/api/citizenships")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"))
        ;
    }

    @Test(dependsOnMethods = "CreateCitizenship")
    public void UpdateCitizenship() {
        Map<String, String> updatedCitizenship = new HashMap<>();
        updatedCitizenship.put("id", ctznshpId);
        updatedCitizenship.put("name", "Updated " + rndName);
        updatedCitizenship.put("shortName", "Updated " + rndCode);
        given()
                .spec(reqSpec)
                .body(updatedCitizenship)
                .when()
                .put("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(ctznshpId))
                .body("name", equalTo("Updated " + rndName))
                .body("shortName", equalTo("Updated " + rndCode))
        ;
    }

    @Test(dependsOnMethods = "UpdateCitizenship")
    public void DeleteCitizenship() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/citizenships/" + ctznshpId)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "DeleteCitizenship")
    public void DeleteCitizenshipNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/citizenships/" + ctznshpId)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("not found"))
        ;
    }

}
