package APItests.RestfulBooker;

import APItests.utilities.BaseTest;
import APItests.utilities.FileNameConstatnts;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class PostAPIRequsingFile extends BaseTest {

    @Test
    public void POSTapiReq(){
        try {
            String PostAPIBodyReq = FileUtils.readFileToString(new File(FileNameConstatnts.Post_API_Body_Req));
            //System.out.println(PostAPIBodyReq);

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

                    RestAssured
                            .given()
                               .contentType(ContentType.JSON)
                               .baseUri("https://restful-booker.herokuapp.com/booking")
                            .when()
                               .get("/{bookingId}",bookingId)
                            .then()
                               .assertThat()
                               .statusCode(200);


        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
