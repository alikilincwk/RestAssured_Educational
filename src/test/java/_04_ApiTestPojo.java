import Model.Location;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class _04_ApiTestPojo {
    @Test
    public void extractJsonAll_POJO(){
        Model.Location locationNesnesi=
                given()


                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().body().as(Location.class)  // Tüm body all Location.class (kalıba göre) çevir

                ;

        System.out.println("locationNesnesi.getCountry() = " + locationNesnesi.getCountry());
        System.out.println("locationNesnesi = " + locationNesnesi);

    }
}
