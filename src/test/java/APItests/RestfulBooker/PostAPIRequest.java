package APItests.RestfulBooker;

import APItests.utilities.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class PostAPIRequest extends BaseTest {
    @Test
    public void Createbooking(){
        JSONObject booking = new JSONObject();
        JSONObject bookingDates = new JSONObject();

        booking.put("firstname","Testers");
        booking.put("lastname","Brown");
        booking.put("totalprice",2000);
        booking.put("depositpaid",true);
        booking.put("additionalneeds","Breakfast");
        booking.put("bookingdates",bookingDates);

        bookingDates.put("checkin","2018-01-01");
        bookingDates.put("checkout","2019-01-01");

        Response response =

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(booking.toString())
                .baseUri("https://restful-booker.herokuapp.com/booking")
                //.log().all()

                .when()
                .post()

                .then()
                .assertThat()
                //.log().body()
                .statusCode(200)
                .body("booking.firstname", Matchers.equalTo("Testers"))
                .body("booking.bookingdates.checkin", Matchers.equalTo("2018-01-01"))
                .extract().response();

        int bookingId = response.path("bookingid");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("bookingID",bookingId)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                .get("{bookingID}")

                .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", Matchers.equalTo("Testers"))
                .body("lastname", Matchers.equalTo("Brown"))

                .extract().response();

    }
}
