import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UserSignIn {

    public static String userToken;
    public static String cardToken;
    public static String fundingToken;
    public static String funToken;

    @BeforeClass
    public void setup() {

        RestAssured.authentication =    RestAssured.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d");


    }



    @Test
    public void Test01(){

         userToken = given().relaxedHTTPSValidation()
                //.auth()
                //.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d")
                .headers("Content-Type", "application/json")
                .headers("Authorization","Basic NzY1YzRmM2QtNGQ0Zi00YjQ2LTk5NGEtMWM0NGY0ZDdmZWUwOjEzYzQ3MzdiLTNjYTktNDNiMS1hMjUxLTJkZGE2YjFiMTk3ZA==")
                .body("{ \"first_name\": \"shuba\", \"last_name\": \"raj\", \"active\": true }")
                .post("https://sandbox-api.marqeta.com/v3/users")
                .then().extract().response().body().path("token");

       System.out.println(userToken);


    }

    @Test
    public void Test02(){

        cardToken = given().relaxedHTTPSValidation()
                //.auth()
                //.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d")
                .headers("Content-Type", "application/json")
                .headers("Authorization","Basic NzY1YzRmM2QtNGQ0Zi00YjQ2LTk5NGEtMWM0NGY0ZDdmZWUwOjEzYzQ3MzdiLTNjYTktNDNiMS1hMjUxLTJkZGE2YjFiMTk3ZA==")
                .body("{\"start_date\": \"2019-01-01\",\"name\": \"Example Card Product\",\"config\":{\"fulfillment\":{\"payment_instrument\":\"VIRTUAL_PAN\"},\"poi\": {\"ecommerce\": true},\"card_life_cycle\": {\"activate_upon_issue\": true}}}")
                .post("https://sandbox-api.marqeta.com/v3/cardproducts")
                .then().extract().response().body().path("token");

        System.out.println(cardToken);



    }

    @Test
    public void Test03(){

        fundingToken = given().relaxedHTTPSValidation()
                //.auth()
                //.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d")
                .headers("Content-Type", "application/json")
                .headers("Authorization","Basic NzY1YzRmM2QtNGQ0Zi00YjQ2LTk5NGEtMWM0NGY0ZDdmZWUwOjEzYzQ3MzdiLTNjYTktNDNiMS1hMjUxLTJkZGE2YjFiMTk3ZA==")
                .body("{\"name\": \"Program Funding\"}")
                .post("https://sandbox-api.marqeta.com/v3/fundingsources/program")
                .then().extract().response().body().path("token");

        System.out.println(fundingToken);


    }



    @Test
    public void Test04(){

        JSONObject requestParams = new JSONObject();
        requestParams.put("user_token", userToken);
        requestParams.put("card_product_token", cardToken);

        funToken  = given().relaxedHTTPSValidation()
                //.auth()
                //.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d")
                .headers("Content-Type", "application/json")
                .headers("Authorization","Basic NzY1YzRmM2QtNGQ0Zi00YjQ2LTk5NGEtMWM0NGY0ZDdmZWUwOjEzYzQ3MzdiLTNjYTktNDNiMS1hMjUxLTJkZGE2YjFiMTk3ZA==")
                .body(requestParams.toJSONString())
                .post("https://sandbox-api.marqeta.com/v3/cards?show_cvv_number=true&show_pan=true")
                .then().extract().response().body().path("token");;

        System.out.println(funToken);


    }

    @Test
    public void Test05(){

        JSONObject requestParams = new JSONObject();
        requestParams.put("user_token", userToken);
        requestParams.put("amount", "1000");
        requestParams.put("currency_code", "USD");
        requestParams.put("funding_source_token", fundingToken);

        Response response = given().relaxedHTTPSValidation()
                //.auth()
                //.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d")
                .headers("Content-Type", "application/json")
                .headers("Authorization","Basic NzY1YzRmM2QtNGQ0Zi00YjQ2LTk5NGEtMWM0NGY0ZDdmZWUwOjEzYzQ3MzdiLTNjYTktNDNiMS1hMjUxLTJkZGE2YjFiMTk3ZA==")
                .body(requestParams.toJSONString())
                .post("https://sandbox-api.marqeta.com/v3/gpaorders")
                .then().extract().response();

        System.out.println(response.asString());


    }


    @Test
    public void Test06(){

        JSONObject requestParams = new JSONObject();
        requestParams.put("amount", "10");
        requestParams.put("mid", "123456890");
        requestParams.put("card_token", funToken);

        Response response  = given().relaxedHTTPSValidation()
                //.auth()
                //.basic("765c4f3d-4d4f-4b46-994a-1c44f4d7fee0","13c4737b-3ca9-43b1-a251-2dda6b1b197d")
                .headers("Content-Type", "application/json")
                .headers("Authorization","Basic NzY1YzRmM2QtNGQ0Zi00YjQ2LTk5NGEtMWM0NGY0ZDdmZWUwOjEzYzQ3MzdiLTNjYTktNDNiMS1hMjUxLTJkZGE2YjFiMTk3ZA==")
                .body(requestParams.toJSONString())
                .post("https://sandbox-api.marqeta.com/v3/simulate/authorization")
                .then().extract().response();

        System.out.println(response.asString());


    }





}
