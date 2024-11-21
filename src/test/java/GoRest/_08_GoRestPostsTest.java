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


//GoRest Posts kaynağındaki API'leri test ediniz

public class _08_GoRestPostsTest {
    Faker randomUretici = new Faker();
    RequestSpecification reqSpec;
    int postID = 0;
    @BeforeClass
    public void Setup() {
        baseURI = "https://gorest.co.in/public/v2/posts";
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 7a13fb61eb499a61c7e2abac035c4b6d2c95b81f5bd9995dff299eaa67d37708")
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void PostCreate() {
        String rndParagraph = randomUretici.lorem().paragraph();
        String rndSentence = randomUretici.lorem().sentence();
        Map<String, String> newPost = new HashMap<>();
        newPost.put("user_id", "7531173");
        newPost.put("title", rndSentence);
        newPost.put("body", rndParagraph);
        postID =
                given()
                        .spec(reqSpec)
                        .body(newPost)
                        .when()
                        .post("")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("postID = " + postID);
    }
    @Test(dependsOnMethods = "PostCreate")
    public void GetPostById() {
        given()
                .spec(reqSpec)
                .when()
                .get("/" + postID)
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(postID))
        ;
    }
    @Test(dependsOnMethods = "GetPostById")
    public void UpdatePost() {
        String rndSentence = randomUretici.lorem().sentence();
        Map<String, String> updatedPost = new HashMap<>();
        updatedPost.put("title", "Updated " + rndSentence);
        given()
                .spec(reqSpec)
                .body(updatedPost)
                .when()
                .put("/" + postID)
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(postID))
                .body("title", equalTo("Updated " + rndSentence))
        ;
    }
    @Test(dependsOnMethods = "UpdatePost")
    public void DeletePost() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/" + postID)
                .then()
                .log().body()
                .statusCode(204)
        ;
    }
    @Test(dependsOnMethods = "UpdatePost")
    public void NegativeDeletePost() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/" + postID)
                .then()
                .log().body()
                .statusCode(404)
        ;
    }
}
