package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _07_GoRestUsersTest {

    Faker randomUretici = new Faker();
    RequestSpecification reqSpec;
    int userID = 0;

    @BeforeClass
    public void Setup() {
        baseURI = "https://gorest.co.in/public/v2/users";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 7a13fb61eb499a61c7e2abac035c4b6d2c95b81f5bd9995dff299eaa67d37708")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void CreateUser() {

        String rndFullName = randomUretici.name().fullName();
        String rndEmail = randomUretici.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>(); //postmandaki body kısmı map olarak düzenlendi
        newUser.put("name", rndFullName);
        newUser.put("gender", "Male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")

        ;
        System.out.println("userID = " + userID);


    }

    @Test(dependsOnMethods = "CreateUser")
    public void GetUserById() {
        given()
                .spec(reqSpec)

                .when()
                .get("/" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))

        ;
    }

    @Test(dependsOnMethods = "GetUserById")
    public void UpdateUser() {
        String rndFullName = randomUretici.name().fullName();

        Map<String, String> updatedUser = new HashMap<>();
        updatedUser.put("name", "Updated " + rndFullName);
        given()
                .spec(reqSpec)
                .body(updatedUser)
                .when()
                .put("/" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo("Updated " + rndFullName))
        ;
    }

    @Test(dependsOnMethods = "UpdateUser")
    public void DeleteUser() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/" + userID)

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "DeleteUser")
    public void NegativeDeleteUser() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/" + userID)

                .then()
                .log().body()
                .statusCode(404)
        ;
    }
}
