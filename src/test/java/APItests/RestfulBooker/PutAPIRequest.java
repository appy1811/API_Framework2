package APItests.RestfulBooker;

import APItests.utilities.FileNameConstatnts;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class PutAPIRequest {
    @Test
    public void PUTapiReq(){
        try {
            String PostAPIBodyReq = FileUtils.readFileToString(new File(FileNameConstatnts.Post_API_Body_Req),"UTF-8");
            String TokenAPIBodyReq = FileUtils.readFileToString(new File(FileNameConstatnts.TOKEN_API_REQ_BODY),"UTF-8");
            String PutAPIBodyReq = FileUtils.readFileToString(new File(FileNameConstatnts.PUT_API_REQ_BODY),"UTF-8");
            //System.out.println(PostAPIBodyReq);

            //post api
            Response response =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .body(PostAPIBodyReq)
                            .baseUri("https://restful-booker.herokuapp.com/booking")

                            .when()
                            .post()

                            .then()
                            .assertThat()
                            .statusCode(200)

                            .extract().response();
            JSONArray jsonArray = JsonPath.read(response.body().asString(),"$.booking..firstname");
            //System.out.println(jsonArray.get(0));
            String firstName = (String) jsonArray.get(0);
            Assert.assertEquals(firstName,"Testers");
            int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");

            //get api
            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                    .when()
                    .get("/{bookingId}",bookingId)
                    .then()
                    .assertThat()
                    .statusCode(200);

            //token
            Response tokenAPIResponse =
            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(TokenAPIBodyReq)
                    .baseUri("https://restful-booker.herokuapp.com/auth")
                    .when()
                    .post()
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract().response();
            String token = JsonPath.read(tokenAPIResponse.body().asString(),"$.token");

            //put api
            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(PutAPIBodyReq)
                    .headers("cookie","token="+token)
                    .baseUri("https://restful-booker.herokuapp.com/booking")
                    .when()
                    .put("/{bookingId}",bookingId)
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo("tan"))
                    .body("lastname",Matchers.equalTo("Swift"));


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
