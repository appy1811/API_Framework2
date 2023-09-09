package APItests.RestfulBooker;

import APItests.utilities.FileNameConstatnts;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import APItests.pojo.Booking;
import APItests.pojo.BookingDates;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;




public class PostApiRequsingPOJO {
    @Test
    public void postAPIRequest(){
        try{
            String jsonSchema = FileUtils.readFileToString(new File(FileNameConstatnts.JSON_SCHEMA),"UTF-8");
        BookingDates bookingDates= new BookingDates("2023-09-25","2020-09-25");
        Booking booking =new Booking("Testers" , "Brown" , "breakfast" ,2000, true, bookingDates);

        //serialization
        ObjectMapper objectMapper = new ObjectMapper();
        String  requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);

        //deserialization
        Booking bookingDetails = objectMapper.readValue(requestBody, Booking.class);
            System.out.println(bookingDetails.getFirstname());
            System.out.println(bookingDetails.getBookingdates().getCheckin());

            Response response =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .baseUri("https://restful-booker.herokuapp.com/booking")
                            .when()
                            .post()
                            .then()
                            .assertThat()
                            .statusCode(200)
                            .extract()
                            .response();

            int bookingId = response.path("bookingid");

            //System.out.println(jsonSchema);

            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                    .when()
                    .get("/{bookingId}",bookingId)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));

    }catch (JsonProcessingException e){
        e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
