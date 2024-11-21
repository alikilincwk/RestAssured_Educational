import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _03_ApiTestExtract {
    @Test
    public void extractingJsonPath() {

        String ulkeAdi =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country");

        System.out.println("ulkeAdi = " + ulkeAdi);
        Assert.assertEquals(ulkeAdi, "United States");
    }

    @Test
    public void extractingJsonPath2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız
        String stateName =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].state");
        System.out.println("stateName = " + stateName);
        Assert.assertEquals(stateName,"California");

    }

    @Test
    public void extractingJsonPath3() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.
        int limit =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
        Assert.assertTrue(limit==10);
    }

    @Test
    public void extractingJsonPath4() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün idleri yazdırınız.
        ArrayList<Integer> ids=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.id")

                ;
        System.out.println("ids = " + ids);

    }

    @Test
    public void extractingJsonPath5() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün name leri yazdırınız.
        ArrayList<String> names=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name")

                ;
        System.out.println("names = " + names);

    }

    @Test
    public void extractingJsonPathResponseAll() {

        Response donenData=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response()

                ;
        List<Integer> ids=donenData.path("data.id");
        List<String> names=donenData.path("data.name");
        int limit=donenData.path("meta.pagination.limit");

        System.out.println("ids = " + ids);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(ids.contains(7522200));
        Assert.assertTrue(names.contains("Naval Achari"));
        Assert.assertTrue(limit==10);

    }
}
